package com.rsqn.utils.jjst.servlets;

import com.rsqn.utils.jjst.util.JavaScriptMinifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class JavascriptAggregationServlet extends HttpServlet {
    public static final String NOCOMPILE = "nocompile";

    private static Logger log = LoggerFactory.getLogger(JavascriptAggregationServlet.class);
    private static final Map<String, String> cache = new Hashtable<>();
    private String baseProfiles;

    private String generateCacheKey(List profiles) {
        return "cached-" + profiles;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (config.getInitParameter("baseProfiles") != null) {
            baseProfiles = config.getInitParameter("baseProfiles");
        } else {
            baseProfiles = "";
        }
    }

    protected File resolveFile(File cwd, String relativePath) throws IOException {
        File f = new File(cwd, relativePath);
        return f;
    }

    protected String getFileContents(File f) throws IOException {
        String content = new String(Files.readAllBytes(f.toPath()));
        return content;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/javascript");
        PrintWriter out = response.getWriter();
        String contents;

        String bootPath = request.getRequestURI();
        File tld = new File(getServletContext().getRealPath("/"));
        File bootFile = new File(getServletContext().getRealPath(bootPath));

        if ( ! bootFile.getAbsolutePath().contains(tld.getAbsolutePath())) {
            throw new ServletException("Invalid Path");
        }

        try {
            List<String> profileList = new ArrayList<>();

            if (baseProfiles != null) {
                String[] profileSplit = baseProfiles.split(",");
                Collections.addAll(profileList, profileSplit);
            }

            if (profileList.contains("clearcache")) {
                cache.clear();
            }

            contents = cache.get(generateCacheKey(profileList));
            if (contents == null || profileList.contains("nocache")) {
                log.info("Profiles = " + profileList);
                StringBuffer buffer = new StringBuffer();
                aggregateFromFile(buffer, bootFile.getParentFile(), bootFile.getName(), profileList);
                contents = buffer.toString();

//                Map<String, String> tags = getTagsToReplace();
//                for (String key : tags.keySet()) {
//                    String tagValue = tags.get(key);
//                    log.debug("replacing #" + key + "# = " + tagValue);
//                    contents = contents.replaceAll("#" + key + "#", tagValue);
//                }

                if (!profileList.contains(NOCOMPILE)) {
                    JavaScriptMinifier minifier = new JavaScriptMinifier();
                    contents = minifier.minify(contents, false);
                }
                cache.put(generateCacheKey(profileList), contents);
            }
            log.info("Aggregation complete");
            out.write(contents);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.err.println(e);
            e.printStackTrace();
        } finally {
            out.close();  // Always close the output writer
        }
    }

    /*
    private String loadTemplate(String path) throws IOException {
        log.debug("loading template " + path);
        ClassPathResource resource = new ClassPathResource(path);

        String s = IOUtil.readToString(resource.getInputStream());

        s = s.replaceAll("\\r", "");
        s = s.replaceAll("\\n", "");
        s = s.replaceAll("\\t", "");
        s = StringEscapeUtils.escapeEcmaScript(s);
        return s;
    }

    private String parseTemplateToken(String line) {
        int start = line.indexOf("#template");
        int end = line.indexOf("#", start + 1);
        return line.substring(start, end + 1);
    }

    private String parseTemplateInclude(String line) {
        String path = line.split("=")[1];
        path = path.replace('#', ' ').trim();
        return path;
    }
    */

    protected void aggregateFromFile(StringBuffer buffer, File cwd, String fileName, List<String> profiles) throws IOException {
        String includeToken = "#include";
//        String templateToken = "#template"; // #template=filePathInClassPath id document.write('#template=filePath');
        String ifProfileToken = "#ifprofile";
        String ifNotProfileToken = "#ifnot_profile";
        String endIfToken = "#endif";
        File f = resolveFile(cwd, fileName);
        File nCwd = f.getParentFile();
        String baseContents = getFileContents(f);
        BufferedReader reader = new BufferedReader(new StringReader(baseContents));
        String line;
        String includeFileName;

        final int DEFAULT_STATE = 0;
        final int IN_PROFILE_STATE_IGNORE = 1;
        final int IN_PROFILE_STATE_PROCESS = 2;
        int state = DEFAULT_STATE;

        while ((line = reader.readLine()) != null) {
            if (state == IN_PROFILE_STATE_IGNORE || state == IN_PROFILE_STATE_PROCESS) {
                if (line.trim().startsWith(endIfToken)) {
                    state = DEFAULT_STATE;
                    continue;
                }
            }
            if (state == IN_PROFILE_STATE_IGNORE) {
                continue;
            }

            if (line.trim().startsWith(includeToken)) {
                includeFileName = line.replace(includeToken, "").trim();
                buffer.append("\n\n\n");
                buffer.append("// " + includeFileName);
                buffer.append("\n\n");
                aggregateFromFile(buffer, nCwd, includeFileName, profiles);
            }
//            else if (line.trim().contains("#template")) {
//                String templateInclude = parseTemplateToken(line);
//                log.debug("Template include " + templateInclude);
//                String templateContent = loadTemplate(parseTemplateInclude(templateInclude));
//                line = line.replace(templateInclude, templateContent);
//                buffer.append("// " + templateInclude);
//                buffer.append("\n\n");
//                buffer.append(line);
//                buffer.append("\n\n");
//            }
            else if (line.trim().startsWith(ifProfileToken)) {
                String profileName = line.replace(ifProfileToken, "").trim();
                log.debug("IfProfile [" + profileName + "]");
                if (profiles.contains(profileName)) {
                    state = IN_PROFILE_STATE_PROCESS;
                    log.debug("IfProfile process [" + profileName + "]");
                } else {
                    log.debug("IfProfile ignore [" + profileName + "]");
                    state = IN_PROFILE_STATE_IGNORE;
                }
            } else if (line.trim().startsWith(ifNotProfileToken)) {
                String profileName = line.replace(ifNotProfileToken, "").trim();
                log.debug("IfNotProfile [" + profileName + "]");
                if (profiles.contains(profileName)) {
                    log.debug("IfNotProfile ignoring [" + profileName + "]");
                    state = IN_PROFILE_STATE_IGNORE;
                } else {
                    state = IN_PROFILE_STATE_PROCESS;
                    log.debug("IfNotProfile processing [" + profileName + "]");
                }
            } else {
                buffer.append(line);
                buffer.append("\n");
            }
        }
    }
}

