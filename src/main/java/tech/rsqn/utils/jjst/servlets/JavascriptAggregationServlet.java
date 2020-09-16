package tech.rsqn.utils.jjst.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.rsqn.utils.jjst.service.AbstractContentService;
import tech.rsqn.utils.jjst.service.JavascriptES6ContentService;
import tech.rsqn.utils.jjst.service.JavascriptNoCompileContentService;
import tech.rsqn.utils.jjst.service.Profiles;

public class JavascriptAggregationServlet extends AbstractAggregationServlet {
    private static Logger log = LoggerFactory.getLogger(JavascriptAggregationServlet.class);

    @Override
    protected String getContentType() {
        return "text/javascript";
    }

    @Override
    protected AbstractContentService createContextService(Profiles profiles, String customProfile) {
        if (customProfile.contains(Profiles.COMPILE) ||profiles.contains(Profiles.COMPILE)) {
            return new JavascriptES6ContentService(profiles);
        } else {
            return new JavascriptNoCompileContentService(profiles);
        }
    }
}

