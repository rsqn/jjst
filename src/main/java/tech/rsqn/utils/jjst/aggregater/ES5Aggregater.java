package tech.rsqn.utils.jjst.aggregater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.rsqn.utils.jjst.util.ResourceUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;

import static tech.rsqn.utils.jjst.util.FileUtil.resolveFile;

/**
 */
public class ES5Aggregater {

    private static Logger log = LoggerFactory.getLogger(ES5Aggregater.class);

    static final String INCLUDE_TOKEN = "#include";
    static final String IF_PROFILE_TOKEN = "#ifprofile";
    static final String IF_NOT_PROFILE_TOKEN = "#ifnot_profile";
    static final String END_IF_TOKEN = "#endif";

    //
    // Process state used to define in normal process, in profile, or profile ignored
    //
    static final int DEFAULT_STATE = 0;
    static final int IN_PROFILE_STATE_IGNORE = 1;
    static final int IN_PROFILE_STATE_PROCESS = 2;

    public static void aggregateFromFile(final StringBuffer buffer,
                                         final File cwd,
                                         final String fileName,
                                         final Collection<String> profiles) throws IOException {

        final File f = resolveFile(cwd, fileName);
        final File nCwd = f.getParentFile();
        final String baseContents = ResourceUtil.loadContentFromResource(fileName);

        final BufferedReader reader = new BufferedReader(new StringReader(baseContents));

        String line;
        String includeFileName;

        int state = DEFAULT_STATE;

        while ((line = reader.readLine()) != null) {
            if (state == IN_PROFILE_STATE_IGNORE || state == IN_PROFILE_STATE_PROCESS) {
                if (line.trim().startsWith(END_IF_TOKEN)) {
                    state = DEFAULT_STATE;
                    continue;
                }
            }
            if (state == IN_PROFILE_STATE_IGNORE) {
                continue;
            }

            if (line.trim().startsWith(INCLUDE_TOKEN)) {
                includeFileName = line.replace(INCLUDE_TOKEN, "").trim();
                buffer.append("\n\n\n");
                buffer.append("/* " + includeFileName + " */");
                buffer.append("\n\n");
                aggregateFromFile(buffer, nCwd, includeFileName, profiles);
            }
//            else if (line.trim().containsKey("#template")) {
//                String templateInclude = parseTemplateToken(line);
//                log.debug("Template include " + templateInclude);
//                String templateContent = loadTemplate(parseTemplateInclude(templateInclude));
//                line = line.replace(templateInclude, templateContent);
//                buffer.append("// " + templateInclude);
//                buffer.append("\n\n");
//                buffer.append(line);
//                buffer.append("\n\n");
//            }
            else if (line.trim().startsWith(IF_PROFILE_TOKEN)) {
                String profileName = line.replace(IF_PROFILE_TOKEN, "").trim();
                log.debug("IfProfile [" + profileName + "]");
                if (profiles.contains(profileName)) {
                    state = IN_PROFILE_STATE_PROCESS;
                    log.debug("IfProfile process [" + profileName + "]");
                } else {
                    log.debug("IfProfile ignore [" + profileName + "]");
                    state = IN_PROFILE_STATE_IGNORE;
                }
            } else if (line.trim().startsWith(IF_NOT_PROFILE_TOKEN)) {
                String profileName = line.replace(IF_NOT_PROFILE_TOKEN, "").trim();
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
