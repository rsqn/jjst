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

    Path es6Root;
    Path indexPath;
    Path fullIndexPath;

    @BeforeEach
    void before() throws IOException {
        es6Root = Paths.get(ResourceUtil.getResourceRoot(), "compile");
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

        final Map<String, Module> rst = mr.scan();

        assertThat(rst.get("index.js").getLines().size(), greaterThan(1));
        // There is comment line which will be trigger
        //assertThat(rst.get("index.js").getDefinitions().getExports().size(), equalTo(0));

        assertThat(rst.get("js/tools.js"), notNullValue());
        assertThat(rst.get("js/api/tools.js"), notNullValue());

        assertThat(rst.get("js/user.js").getLines().size(), greaterThan(1));

        assertThat(mr.getMap(), equalTo(rst));
    }
}
