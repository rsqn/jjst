package tech.rsqn.utils.jjst.aggregater.es6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.rsqn.utils.jjst.util.ResourceUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Andy Chau on 4/9/20.
 */
public class ModuleScannerTest {

    Path es6Root;
    Path indexPath;
    Path fullIndexPath;

    @BeforeEach
    void before() throws IOException {
        es6Root = Paths.get(ResourceUtil.getResourcePath(), "ES6");
        indexPath = Paths.get("/", "js", "index.js");
        fullIndexPath = Paths.get(es6Root.toString(), indexPath.toString());
    }

    @Test
    void shouldHandleConstruction() {

        final ModuleScanner mr = new ModuleScanner(fullIndexPath.toString());
        assertThat(mr.getRootPath(), equalTo(Paths.get(es6Root.toString(), "js")));
        assertThat(mr.getIndexFile(), equalTo("index.js"));

        // negative cases
        assertThrows(NullPointerException.class, () -> new ModuleScanner(null));
        assertThrows(IllegalArgumentException.class, () -> new ModuleScanner("/js/index.js"));
    }

    @Test
    void shouldLoadModules() throws IOException {
        final ModuleScanner mr = new ModuleScanner(fullIndexPath.toString());

        final Map<String, String> rst = mr.scan();

        assertThat(rst.get("index.js").length(), greaterThan(1));
        assertThat(rst.get("js/tools.js").length(), greaterThan(1));
        assertThat(rst.get("js/user.js").length(), greaterThan(1));
        assertThat(rst.get("js/api/tools.js").length(), greaterThan(1));

        assertThat(mr.getMap(), equalTo(rst));
    }

}
