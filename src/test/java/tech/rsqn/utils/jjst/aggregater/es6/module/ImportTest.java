package tech.rsqn.utils.jjst.aggregater.es6.module;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Andy Chau on 14/9/20.
 */
public class ImportTest {

    @Test
    void shouldCreateNamedImport() {
        Import im;
        // test single name
        String _1name = "import {single} from './path/file.js';";
        im = Import.parseLine(_1name);
        assertThat(im, notNullValue());
        assertThat(im.getType(), equalTo(Import.Type.NAMED));
        assertThat(im.getPath(), equalTo("./path/file.js"));
        assertThat(im.getNames().size(), equalTo(1));
        assertThat(im.getNames().contains("single"), equalTo(true));


        // test multiple names
        String _2names = "import {name1, name2} from './path/file.js';";
        im = Import.parseLine(_2names);
        assertThat(im, notNullValue());
        assertThat(im.getType(), equalTo(Import.Type.NAMED));
        assertThat(im.getPath(), equalTo("./path/file.js"));
        assertThat(im.getNames().size(), equalTo(2));
        assertThat(im.getNames().contains("name1"), equalTo(true));
        assertThat(im.getNames().contains("name2"), equalTo(true));

        im = Import.parseLine("function fun1()");
        assertThat(im, nullValue());
    }

    @Test
    void shouldCreateWildcardImport() {
        Import im;
        String name = "import * as file from './path/file.js';";
        im = Import.parseLine(name);
        assertThat(im, notNullValue());
        assertThat(im.getType(), equalTo(Import.Type.WILDCARD));
        assertThat(im.getPath(), equalTo("./path/file.js"));
        assertThat(im.getNames().size(), equalTo(1));
        assertThat(im.getNames().contains("file"), equalTo(true));

    }
}
