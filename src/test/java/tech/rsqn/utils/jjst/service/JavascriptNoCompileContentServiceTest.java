package tech.rsqn.utils.jjst.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.rsqn.utils.jjst.util.ResourceUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Andy Chau on 28/8/20.
 */
public class JavascriptNoCompileContentServiceTest {

    private static Logger log = LoggerFactory.getLogger(JavascriptNoCompileContentServiceTest.class);

    private JavascriptNoCompileContentService service;

    private File es5Root = Paths.get( "ES5").toFile();

    private File resourceRoot;
    private String indexPath;

    @BeforeEach
    void before() throws IOException {
        resourceRoot = Paths.get(ResourceUtil.getResourceRoot(), es5Root.getPath()).toFile();
        indexPath = Paths.get("/", "js", "index.js").toString();
    }

    @Test
    void shouldConstructAllMethodPass() throws IOException  {
        final Profiles emptyProfile = new Profiles();

        service = new JavascriptNoCompileContentService(emptyProfile);

        // test override methods
        assertThat(service.getSpec(), equalTo("ES5"));
        assertThat(service.getAggregater(), notNullValue());
        assertThat(service.getAggregater().getSpec(), equalTo("ES5"));
        assertThat(service.processFileContent("abc"), equalTo("abc"));
        assertThat(service.postProcess("post"), equalTo("post"));


        // test empty profile, except cached
        String c = service.getContent(resourceRoot, indexPath, null);
        log.info("Content: {}", c);

        assertThat(c, containsString("/* /js/utils/util.js */"));
        assertThat(c, containsString("/* /js/controller/controller.js */"));
        assertThat(c, not(containsString("DEBUG ME")));


        c = service.getContent(resourceRoot, indexPath, "debug");
        assertThat(c, containsString("DEBUG ME"));
    }

    @Test
    void shouldUseBaseProfileAndAbleToClearCache() throws IOException {
        // Base profile will cache the contents
        // request to clean the cache then should clean.
        final Profiles noCompile = new Profiles(Profiles.NO_COMPILE);

        service = new JavascriptNoCompileContentService(noCompile);

        // to load the content into cache
        service.getContent(resourceRoot, indexPath, null);
        assertThat(service.cachedKeys().size(), greaterThan(0));

        // to load the content to get no-cache version, cache should still the same
        service.getContent(resourceRoot, indexPath, Profiles.NO_CACHE);
        assertThat(service.cachedKeys().size(), greaterThan(0));

        // to load the content into cache to clear cache, can't really test because it will be remove and add back
        service.getContent(resourceRoot, indexPath, Profiles.CLEAR_CACHE);
        assertThat(service.cachedKeys().size(), equalTo(1));

    }

}
