package tech.rsqn.utils.jjst.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tech.rsqn.utils.jjst.aggregater.es6.ExportDefinition;

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
        final Pattern pFuncDef = ExportDefinition.P_FUNCTION_DEFINITION;

        List<String> rst;
        // function
        rst = RegexHelper.match(pFuncDef, "export function myFun (p1,p2,p3) {");
        assertThat(rst.size(), equalTo(3));
        assertThat(rst.get(0), equalTo("function"));
        assertThat(rst.get(1), equalTo("myFun"));
        assertThat(rst.get(2), equalTo("(p1,p2,p3)"));

        rst = RegexHelper.match(pFuncDef, "export function myFun(){");
        assertThat(rst.size(), equalTo(3));
        assertThat(rst.get(0), equalTo("function"));
        assertThat(rst.get(1), equalTo("myFun"));
        assertThat(rst.get(2), equalTo("()"));

        rst = RegexHelper.match(pFuncDef, "export function myFun()");
        assertThat(rst.size(), equalTo(3));
        assertThat(rst.get(0), equalTo("function"));
        assertThat(rst.get(1), equalTo("myFun"));
        assertThat(rst.get(2), equalTo("()"));

        rst = RegexHelper.match(pFuncDef, "function myFun()");
        assertThat(rst.size(), equalTo(3));
        assertThat(rst.get(0), equalTo("function"));
        assertThat(rst.get(1), equalTo("myFun"));
        assertThat(rst.get(2), equalTo("()"));



    }

    @Test
    void shouldMatchClassExportLines() {
        final Pattern pClassDef = ExportDefinition.P_CLASS_DEFINITION;

        List<String> rst;
        // class
        rst = RegexHelper.match(pClassDef, "export class MyFun {");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("class"));
        assertThat(rst.get(1), equalTo("MyFun"));

        rst = RegexHelper.match(pClassDef, "class MyFun {");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("class"));
        assertThat(rst.get(1), equalTo("MyFun"));

    }

    @Test
    @Disabled
    void shouldMatchExport() {

        /*
        // TODO add support of expert {}
        final Pattern pExpDef = ExportDefinition.P_EX_DEFINITION;
        List<String> rst;

        rst = RegexHelper.match(pFuncDef, "export MyFun {");
        assertThat(rst.size(), equalTo(0));

        rst = RegexHelper.match(pFuncDef, null);
        assertThat(rst.size(), equalTo(0));

        rst = RegexHelper.match(pFuncDef, "");
        assertThat(rst.size(), equalTo(0));
         */

    }

}
