package tech.rsqn.utils.jjst.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.rsqn.utils.jjst.service.AbstractContentService;
import tech.rsqn.utils.jjst.service.JavascriptES6ContentService;
import tech.rsqn.utils.jjst.service.Profiles;

/**
 * @author Andy Chau on 15/9/20.
 */
public class ES6JavascriptAggregationServlet extends AbstractAggregationServlet {
    private static Logger log = LoggerFactory.getLogger(JavascriptSourceServlet.class);

    @Override
    protected String getContentType() {
        return "text/javascript";
    }

    @Override
    protected AbstractContentService createContextService(Profiles profiles) {
        return new JavascriptES6ContentService(profiles);
    }
}
