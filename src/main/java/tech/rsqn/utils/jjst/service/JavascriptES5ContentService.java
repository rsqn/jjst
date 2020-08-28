package tech.rsqn.utils.jjst.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.rsqn.utils.jjst.aggregater.Aggregater;
import tech.rsqn.utils.jjst.aggregater.ES5Aggregater;

/**
 * Javascript ES5 Content service.
 * @author Andy Chau on 28/8/20.
 */
public class JavascriptES5ContentService extends AbstractContentService {

    private static Logger log = LoggerFactory.getLogger(JavascriptES5ContentService.class);

    private Aggregater aggregater = new ES5Aggregater();

    public JavascriptES5ContentService(final Profiles baseProfile) {
        super(baseProfile);
        log.info("Javascript spec ES5 content service created.");
    }


    @Override
    protected String jsSpec() {
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
