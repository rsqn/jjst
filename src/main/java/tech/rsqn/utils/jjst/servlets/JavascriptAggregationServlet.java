package tech.rsqn.utils.jjst.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavascriptAggregationServlet extends AbstractAggregationServlet {
    private static Logger log = LoggerFactory.getLogger(TemplateServlet.class);

    @Override
    protected String getContentType() {
        return "text/javascript";
    }
}

