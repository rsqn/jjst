package tech.rsqn.utils.jjst.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.rsqn.utils.jjst.aggregater.Aggregater;
import tech.rsqn.utils.jjst.aggregater.ES6Aggregater;

/**
 * @author Andy Chau on 15/9/20.
 */
public class JavascriptES6ContentService extends AbstractContentService {
    private static Logger log = LoggerFactory.getLogger(JavascriptES6ContentService.class);

    private Aggregater aggregater = new ES6Aggregater();

    public JavascriptES6ContentService(final Profiles baseProfile) {
        super(baseProfile);
        log.info("Javascript spec ES6 content service created.");
    }


    @Override
    protected String getSpec() {
        return aggregater.getSpec();
    }

    @Override
    protected Aggregater getAggregater() {
        return aggregater;
    }

    @Override
    protected String processFileContent(final String content) {
        return content;
    }

    @Override
    protected String postProcess(final String content) {
        return content;
    }
}
