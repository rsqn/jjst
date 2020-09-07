package tech.rsqn.utils.jjst.util;

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
    void shouldMatchExportLines() {
        final Pattern pDefinition = ExportDefinition.P_DEFINITION;

        List<String> rst;
        // function
        rst = RegexHelper.match(pDefinition, "export function myFun () {");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("function"));
        assertThat(rst.get(1), equalTo("myFun"));

        rst = RegexHelper.match(pDefinition, "export function myFun(){");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("function"));
        assertThat(rst.get(1), equalTo("myFun"));

        rst = RegexHelper.match(pDefinition, "export function myFun()");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("function"));
        assertThat(rst.get(1), equalTo("myFun"));

        rst = RegexHelper.match(pDefinition, "function myFun()");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("function"));
        assertThat(rst.get(1), equalTo("myFun"));


        // class
        rst = RegexHelper.match(pDefinition, "export class MyFun {");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("class"));
        assertThat(rst.get(1), equalTo("MyFun"));

        rst = RegexHelper.match(pDefinition, "class MyFun {");
        assertThat(rst.size(), equalTo(2));
        assertThat(rst.get(0), equalTo("class"));
        assertThat(rst.get(1), equalTo("MyFun"));


        rst = RegexHelper.match(pDefinition, "export MyFun {");
        assertThat(rst.size(), equalTo(0));

        rst = RegexHelper.match(pDefinition, null);
        assertThat(rst.size(), equalTo(0));

        rst = RegexHelper.match(pDefinition, "");
        assertThat(rst.size(), equalTo(0));

    }

}
