package tech.rsqn.utils.jjst.aggregater.es6;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.rsqn.utils.jjst.aggregater.es6.module.Module;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static tech.rsqn.utils.jjst.TestUtils.concatList;

/**
 * @author Andy Chau on 21/9/20.
 */
public class RootModuleTemplateTest {

    Logger log = LoggerFactory.getLogger(RootModuleTemplateTest.class);

    @Test
    void shouldConvertRootModule() throws IOException {
        final List<String> contentLines = Arrays.asList(
                "import {doubleIt} from './js/import.js'; "
                , ""
                , "function sayHello() {"
                , "    const h1 = '<h1>Hello world</h1>';"
                , "    const _double = `<p>Double of 10 is: ${doubleIt(10)}</p>`;"
                , "    const root = document.getElementById('root');"
                , "    root.innerHTML = h1 + _double;"
                , "}"
        );

        final Module module = new Module("index.js", concatList(contentLines));

        final RootModuleTemplate template = new RootModuleTemplate();
        String rst = template.generateFunction(module);

        log.info(System.lineSeparator() + rst);


        // TODO add more test case validate line by line
    }
}
