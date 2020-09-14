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
 * @author Andy Chau on 14/9/20.
 */
public class FunctionTemplateTest {

    Logger log = LoggerFactory.getLogger(FunctionTemplateTest.class);

    @Test
    void shouldConvertClassModule() throws IOException {
        final List<String> contentLines = Arrays.asList(
                "import {MyImport} from './js/import.js'; "
                , ""
                ,"export class MyClass {"
                , "    let p1;"
                , "    let p2;"
                , ""
                , "    constructor (p1, p2) {"
                , "        this.p1 = p1;"
                , "        this.p2 = p2;"
                , "    }"
                , "    getP1() {"
                , "        return checkP1(p1);"
                , "    }"
                , "    getP2(show) {"
                , "        return addDot(p2) + show;"
                , "    }"
                , "};"
                , "function addDot(value) {"
                , "    return value + '.';"
                , "};"
        );

        final Module module = new Module("MyClass.js", concatList(contentLines));

        final FunctionTemplate template = new FunctionTemplate();
        String rst = template.generateFunction(module);

        log.info(System.lineSeparator() + rst);
    }
}
