package tech.rsqn.utils.jjst.service;

import org.junit.jupiter.api.Test;
import tech.rsqn.utils.jjst.util.ResourceUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Andy Chau on 28/8/20.
 */
public class JavascriptES5ContentServiceTest {

    private JavascriptES5ContentService service;

    private File es5Root = Paths.get("/", "ES5").toFile();

    @Test
    void shouldConstructAllMethodPass() throws IOException  {
        final Profiles emptyProfile = new Profiles();

        service = new JavascriptES5ContentService(emptyProfile);

        // test override methods
        assertThat(service.jsSpec(), equalTo("ES5"));
        assertThat(service.getAggregater(), notNullValue());
        assertThat(service.getAggregater().getSpec(), equalTo("ES5"));
        assertThat(service.processFileContent("abc"), equalTo("abc"));
        assertThat(service.postProcess("post"), equalTo("post"));


        // test empty profile, except cached
        final File resourceRoot = Paths.get(ResourceUtil.getResourcePath(), "ES5").toFile();
        String indexPath = Paths.get("/", "js", "index.js").toString();

        // TODO confirm if duplicated input should be ignored or not!
        assertThat(service.getContent(resourceRoot, indexPath, null), notNullValue());
    }
}
