package tech.rsqn.utils.jjst.aggregater.es6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.rsqn.utils.jjst.aggregater.es6.module.Module;
import tech.rsqn.utils.jjst.util.ResourceUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Andy Chau on 4/9/20.
 */
public class ModuleScannerTest {

    Path resourceRoot;
    Path indexPath;
    Path fullIndexPath;

    @BeforeEach
    void before() throws IOException {
        resourceRoot = Paths.get(ResourceUtil.getResourceRoot(), "compile");
        indexPath = Paths.get("/", "js", "index.js");
    }

    @Test
    void shouldHandleConstruction() {

        final ModuleScanner mr = new ModuleScanner(resourceRoot, indexPath);
        assertThat(mr.getIndexPath(), equalTo(Paths.get(resourceRoot.toString(), "js")));
        assertThat(mr.getIndexFile(), equalTo("index.js"));

        // negative cases
        assertThrows(NullPointerException.class, () -> new ModuleScanner(null, indexPath));
        assertThrows(NullPointerException.class, () -> new ModuleScanner(resourceRoot, null));
        assertThrows(IllegalArgumentException.class, () ->
                new ModuleScanner(resourceRoot, Paths.get("index.html")));
    }

    @Test
    void shouldLoadModules() throws IOException {
        final ModuleScanner mr = new ModuleScanner(resourceRoot, indexPath);

        assertThat(mr.getIndexFile(), equalTo("/js/index.js"));
        assertThat(mr.getIndexPath().toString(), endsWith("/compile/js"));

        final Map<String, Module> rst = mr.scan();

        assertThat(rst.get("js/index.js").getLines().size(), greaterThan(1));
        assertThat(rst.get("js/index.js").getFullContent(), startsWith("// File: index.js"));

        assertThat(rst.get("js/user.js").getLines().size(), greaterThan(1));
        assertThat(rst.get("js/tools.js"), notNullValue());

        assertThat(rst.get("js/api/tools.js"), notNullValue());
        assertThat(rst.get("js/api/math.js"), notNullValue());


        assertThat(mr.getMap(), equalTo(rst));
    }
}
