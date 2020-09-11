package tech.rsqn.utils.jjst.util;

import org.junit.jupiter.api.Test;
import tech.rsqn.utils.jjst.aggregater.es6.module.ES6Regexs;

import java.util.List;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Andy Chau on 7/9/20.
 */
public class RegexHelperTest {

    @Test
    void shouldMatchFunctionExportLines() {
        final Pattern p = ES6Regexs.P_FUNCTION_DEFINITION;

        List<String> rst;
        // function
        rst = RegexHelper.match(p, "export function myFun (p1,p2,p3) {");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("myFun"));
        assertThat(rst.get(1), equalTo("(p1,p2,p3)"));

        rst = RegexHelper.match(p, "export function myFun(){");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("myFun"));
        assertThat(rst.get(1), equalTo("()"));

        rst = RegexHelper.match(p, "export function myFun()");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("myFun"));
        assertThat(rst.get(1), equalTo("()"));

        rst = RegexHelper.match(p, "function myFun()");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("myFun"));
        assertThat(rst.get(1), equalTo("()"));
    }

    @Test
    void shouldMatchClassExportLines() {
        final Pattern p = ES6Regexs.P_CLASS_DEFINITION;

        List<String> rst;
        // class
        rst = RegexHelper.match(p, "export class MyClass {");
        assertThat(rst.size(), equalTo(1));
        assertThat(rst.get(0), equalTo("MyClass"));

        rst = RegexHelper.match(p, "class MyClass {");
        assertThat(rst.size(), equalTo(1));
        assertThat(rst.get(0), equalTo("MyClass"));

    }

    @Test
    void shouldMatchClassConstructor() {
        final Pattern p = ES6Regexs.P_CLASS_CONSTRUCTOR;

        // class
        List<String> rst;
        rst = RegexHelper.match(p, " constructor() {");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("constructor"));
        assertThat(rst.get(1), equalTo("()"));

        rst = RegexHelper.match(p, " constructor (p1, p2) {");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("constructor"));
        assertThat(rst.get(1), equalTo("(p1, p2)"));
    }

    @Test
    void shouldMatchInnerClassFunctions() {
        final Pattern p = ES6Regexs.P_CLASS_FUNCTION;

        // functions
        List<String> rst;
        rst = RegexHelper.match(p, "myFun1() {");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("myFun1"));
        assertThat(rst.get(1), equalTo("()"));

        rst = RegexHelper.match(p, "  myFun2( p1, p2)");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("myFun2"));
        assertThat(rst.get(1), equalTo("( p1, p2)"));

        rst = RegexHelper.match(p, " constructor MyClass (p1, p2) {");
        assertThat(rst.size(), equalTo(0));

        rst = RegexHelper.match(p, " function func (p1, p2) {");
        assertThat(rst.size(), equalTo(0));
    }

    @Test
    void shouldMatchNamedImport() {
        final Pattern p = ES6Regexs.P_NAMED_IMPORT;

        List<String> rst;
        rst = RegexHelper.match(p, "import {named} from './path/file.js'");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("named"));
        assertThat(rst.get(1), equalTo("./path/file.js"));

        rst = RegexHelper.match(p, "import {name1, name2} from './path/file.js'");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("name1, name2"));
        assertThat(rst.get(1), equalTo("./path/file.js"));
    }
}
