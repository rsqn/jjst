package tech.rsqn.utils.jjst.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.rsqn.utils.jjst.service.AbstractContentService;
import tech.rsqn.utils.jjst.service.Profiles;
import tech.rsqn.utils.jjst.util.ContentCache;
import tech.rsqn.utils.jjst.util.ResourceUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

public abstract class AbstractAggregationServlet extends AbstractContentServlet {

    private static Logger log = LoggerFactory.getLogger(AbstractAggregationServlet.class);
    private static final ContentCache<String, String> cache = new ContentCache<>();

    private Profiles baseProfiles;
    private AbstractContentService contentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (config.getInitParameter("baseProfiles") != null) {
            baseProfiles = new Profiles(config.getInitParameter("baseProfiles"));
        } else {
            baseProfiles = new Profiles();
        }

        contentService = this.createContextService(baseProfiles);

        log.info("Init completed with baseProfile: {}", baseProfiles);
    }

    protected abstract String getContentType();

    protected abstract AbstractContentService createContextService(Profiles profiles);

    @Override
    protected String getContent(HttpServletRequest request) throws IOException {
        // current class runner direct, because for servlet the root path is under webapp directory
        final File cwd = new File(ResourceUtil.getResourceRoot());
        final String contentPath = request.getRequestURI();

        final String profileArgs = request.getParameter("profiles");

        return contentService.getContent(cwd, contentPath, profileArgs);
    }
}

