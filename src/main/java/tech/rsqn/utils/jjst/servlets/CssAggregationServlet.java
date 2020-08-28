package tech.rsqn.utils.jjst.servlets;

public class CssAggregationServlet extends AbstractAggregationServlet {
    @Override
    protected String getContentType() {
        return "text/css";
    }

}

