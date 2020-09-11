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
        assertThat(rst.size(), equalTo(3));
        assertThat(rst.get(0), equalTo("function"));
        assertThat(rst.get(1), equalTo("myFun"));
        assertThat(rst.get(2), equalTo("(p1,p2,p3)"));

        rst = RegexHelper.match(p, "export function myFun(){");
        assertThat(rst.size(), equalTo(3));
        assertThat(rst.get(0), equalTo("function"));
        assertThat(rst.get(1), equalTo("myFun"));
        assertThat(rst.get(2), equalTo("()"));

        rst = RegexHelper.match(p, "export function myFun()");
        assertThat(rst.size(), equalTo(3));
        assertThat(rst.get(0), equalTo("function"));
        assertThat(rst.get(1), equalTo("myFun"));
        assertThat(rst.get(2), equalTo("()"));

        rst = RegexHelper.match(p, "function myFun()");
        assertThat(rst.size(), equalTo(3));
        assertThat(rst.get(0), equalTo("function"));
        assertThat(rst.get(1), equalTo("myFun"));
        assertThat(rst.get(2), equalTo("()"));
    }

    @Test
    void shouldMatchClassExportLines() {
        final Pattern p = ES6Regexs.P_CLASS_DEFINITION;

        List<String> rst;
        // class
        rst = RegexHelper.match(p, "export class MyFun {");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("class"));
        assertThat(rst.get(1), equalTo("MyFun"));

        rst = RegexHelper.match(p, "class MyFun {");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("class"));
        assertThat(rst.get(1), equalTo("MyFun"));

    }

    @Test
    void shouldMatchClassConstructor() {
        final Pattern p = ES6Regexs.P_CLASS_CONSTRUCTOR;

        List<String> rst;
        // class
        rst = RegexHelper.match(p, " constructor MyClass() {");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("MyClass"));
        assertThat(rst.get(1), equalTo("()"));

        rst = RegexHelper.match(p, " constructor MyClass (p1, p2) {");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("MyClass"));
        assertThat(rst.get(1), equalTo("(p1, p2)"));
    }

    @Test
    void shouldMatchInnerClassFunctions() {
        final Pattern p = ES6Regexs.P_CLASS_FUNCTION;

        List<String> rst;
        // class
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
}
