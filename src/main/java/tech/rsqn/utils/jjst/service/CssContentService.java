package tech.rsqn.utils.jjst.service;

import tech.rsqn.utils.jjst.aggregater.Aggregater;
import tech.rsqn.utils.jjst.aggregater.CssAggregater;

/**
 * @author Andy Chau on 15/9/20.
 */
public class CssContentService extends AbstractContentService {

    public CssContentService(Profiles baseProfile) {
        super(baseProfile);
    }

    @Override
    protected String getType() {
        return "css";
    }

    @Override
    protected String processFileContent(String content) {
        return content;
    }

    @Override
    protected String postProcess(String content) {
        return content;
    }

    @Override
    protected Aggregater getAggregater() {
        return new CssAggregater();
    }


}
