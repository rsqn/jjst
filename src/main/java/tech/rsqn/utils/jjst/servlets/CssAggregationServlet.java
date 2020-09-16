package tech.rsqn.utils.jjst.servlets;

import tech.rsqn.utils.jjst.service.AbstractContentService;
import tech.rsqn.utils.jjst.service.CssContentService;
import tech.rsqn.utils.jjst.service.Profiles;

public class CssAggregationServlet extends AbstractAggregationServlet {
    @Override
    protected String getContentType() {
        return "text/css";
    }

    @Override
    protected AbstractContentService createContextService(Profiles profiles, String customProfile) {
        return new CssContentService(profiles);
    }
}

