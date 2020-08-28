package tech.rsqn.utils.jjst.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.rsqn.utils.jjst.service.Profiles;
import tech.rsqn.utils.jjst.util.ContentCache;
import tech.rsqn.utils.jjst.aggregater.ES5Aggregater;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class AbstractAggregationServlet extends AbstractContentServlet {

    public static final String CLEAR_CACHE = "clearcache";
    public static final String NO_CACHE = "nocache";
    public static final String NO_COMPILE = "nocompile";


    private static Logger log = LoggerFactory.getLogger(AbstractAggregationServlet.class);
    private static final ContentCache<String, String> cache = new ContentCache<>();

    private Profiles baseProfiles;

    private String generateCacheKey(String path, Collection profiles) {
        return path + profiles;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (config.getInitParameter("baseProfiles") != null) {
            baseProfiles = new Profiles(config.getInitParameter("baseProfiles"));
        } else {
            baseProfiles = new Profiles();
        }
    }

    protected abstract String getContentType();

    protected abstract String processFileContent(String content);

    protected abstract String postProcess(String content);

    @Override
    protected String getContent(HttpServletRequest request) throws ServletException, IOException {
        String contents = null;

        File tld = new File(getServletContext().getRealPath("/"));
        String path = request.getRequestURI();

        final String profileArgs = request.getParameter("profiles");
        final Profiles profileList = new Profiles(baseProfiles, profileArgs);

        if (profileList.contains(CLEAR_CACHE)) {
            cache.clear();
        }

        String cacheKey = generateCacheKey(path, profileList.getProfiles());

        contents = cache.get(cacheKey);

        if (contents == null || profileList.contains(NO_CACHE)) {
            log.debug("Profiles = " + profileList);

            final StringBuffer buffer = new StringBuffer();
            ES5Aggregater.aggregateFromFile(buffer, tld, path, profileList.getProfiles());

            contents = buffer.toString();
            contents = postProcess(contents);

            cache.put(generateCacheKey(path, profileList.getProfiles()), contents);

            log.info("Aggregation of {} complete", path);
        } else {
            log.trace("Returning {} from cache ", path);
        }
        return contents;

    }
}

