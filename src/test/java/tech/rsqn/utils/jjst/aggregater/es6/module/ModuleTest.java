package tech.rsqn.utils.jjst.aggregater.es6.module;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Andy Chau on 10/9/20.
 */
public class ModuleTest {

    @Test
    void shouldProcessMultiFunctions() {
        final List<String> contentLines = Arrays.asList(
                "export function myFun1() {"
                , "something one..."
                , "};"
                , "export function myFun2(p1, p2)"
                , "{"
                , "log two"
                , "something two..."
                , "};"
                , "function private(p1, p2, p3)"
                , "{"
                , "private..."
                , "};"
        );

        final String fullContent = concatList(contentLines);

        final Module module = new Module("Test2", fullContent);
        assertThat(module.getName(), equalTo("Test2"));

        final List<JsFunction> jsFuncs = module.getJsFuncs();
        assertThat(jsFuncs.size(), equalTo(3));

        final JsFunction f1 = jsFuncs.get(0);
        assertThat(f1.getName(), equalTo("myFun1"));
        assertThat(f1.getParams(), equalTo("()"));
        assertThat(f1.isExported(), equalTo(true));
        assertThat(f1.getBodyLines().size(), equalTo(1));
        assertThat(f1.getBodyLines().get(0), equalTo("something one..."));

        final JsFunction f2 = jsFuncs.get(1);
        assertThat(f2.getName(), equalTo("myFun2"));
        assertThat(f2.getParams(), equalTo("(p1, p2)"));
        assertThat(f2.isExported(), equalTo(true));
        assertThat(f2.getBodyLines().size(), equalTo(2));
        assertThat(f2.getBodyLines().get(0), equalTo("log two"));
        assertThat(f2.getBodyLines().get(1), equalTo("something two..."));
        assertThat(f2.getBody(), equalTo(concatList(Arrays.asList("log two", "something two..."))));

        final JsFunction f3 = jsFuncs.get(2);
        assertThat(f3.getName(), equalTo("private"));
        assertThat(f3.getParams(), equalTo("(p1, p2, p3)"));
        assertThat(f3.isExported(), equalTo(false));
        assertThat(f3.getBodyLines().size(), equalTo(1));
        assertThat(f3.getBodyLines().get(0), equalTo("private..."));
    }

    @Test
    void shouldProcessClassModule() {
        final List<String> contentLines = Arrays.asList(
                "import {MyImport} from './js/import.js'; "
                , ""
                ,"export class MyClass {"
                , "    let p1;"
                , "    let p2;"
                , ""
                , "    constructor (p1, p2) {"
                , "        console.log(p1);"
                , "        console.log(p2);"
                , "    }"
                , "    getP1() {"
                , "        return checkP1(p1);"
                , "    }"
                , "    getP2(show) {"
                , "        return p2;"
                , "    }"
                , "};"
                , "function addDot(value) {"
                , "    return value + '.';"
                , "};"

        );

        final String fullContent = concatList(contentLines);


        final Module module = new Module("MyClass", fullContent);
        assertThat(module.getName(), equalTo("MyClass"));

        //
        // test import has been registered
        //
        assertThat(module.getImports(), notNullValue());
        assertThat(module.getImports().size(), equalTo(1));
        assertThat(module.getImports().get(0).getType(), equalTo(Import.Type.NAMED));

        //
        // test class
        //
        final JsClass jsClass = module.getJsClass();
        assertThat(jsClass, notNullValue());
        assertThat(jsClass.getType(), equalTo(BaseJsObject.Type.CLASS));

        //
        // test class constructor
        //
        JsFunction jsFunc;
        jsFunc = jsClass.getConstructor();
        assertThat(jsFunc.getName(), equalTo("constructor"));
        assertThat(jsFunc.getParams(), equalTo("(p1, p2)"));
        assertThat(jsFunc.getBodyLines().size(), equalTo(2));

        //
        // test import
        //
        assertThat(jsClass.getClassFunctions().size(), equalTo(2));

        //
        // test class functions
        //
        assertThat(jsClass.getClassFunctions().size(), equalTo(2));

        jsFunc = jsClass.getClassFunctions().get(0);
        assertThat(jsFunc.getName(), equalTo("getP1"));
        assertThat(jsFunc.getParams(), equalTo("()"));
        assertThat(jsFunc.getBodyLines().size(), equalTo(1));

        jsFunc = jsClass.getClassFunctions().get(1);
        assertThat(jsFunc.getName(), equalTo("getP2"));
        assertThat(jsFunc.getParams(), equalTo("(show)"));
        assertThat(jsFunc.getBodyLines().size(), equalTo(1));

        //
        // test none class function
        //
        assertThat(jsClass.getNoneClassFunctions().size(), equalTo(1));

        jsFunc = jsClass.getNoneClassFunctions().get(0);
        assertThat(jsFunc.getName(), equalTo("addDot"));
        assertThat(jsFunc.getParams(), equalTo("(value)"));
        assertThat(jsFunc.getBodyLines().size(), equalTo(1));

        //
        // test class members
        //
        assertThat(jsClass.getClassMembers().size(), equalTo(2));

        List<String> members = jsClass.getClassMembers();
        assertThat(members.get(0), equalTo("let p1;"));
        assertThat(members.get(1), equalTo("let p2;"));

        assertThat(module.getJsFuncs(), notNullValue());
    }

    private String concatList(List<String> list) {
        return list.stream().collect(Collectors.joining(System.lineSeparator()));
    }
}
