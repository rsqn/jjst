package tech.rsqn.utils.jjst.aggregater.es6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.rsqn.utils.jjst.aggregater.ES6Aggregater;
import tech.rsqn.utils.jjst.util.ResourceUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * @author Andy Chau on 8/9/20.
 */
public class ES6AggregaterTest {

    private static Logger log = LoggerFactory.getLogger(ES6AggregaterTest.class);

    Path es6Root;
    Path indexPath;
    Path fullIndexPath;

    @BeforeEach
    void before() throws IOException {
        es6Root = Paths.get(ResourceUtil.getResourceRoot(), "ES6");
        indexPath = Paths.get("/", "js", "index.js");
        fullIndexPath = Paths.get(es6Root.toString(), indexPath.toString());
    }
    @Test
    void shouldPass() throws IOException {
        ES6Aggregater es6 = new ES6Aggregater();

        final StringBuffer buf = new StringBuffer();

        es6.aggregateFromFile(buf, es6Root.toFile(), indexPath.toString(), null);

        assertThat(buf.toString(), containsString("class ModuleRegistry {"));

        log.info(buf.toString());
    }
}
