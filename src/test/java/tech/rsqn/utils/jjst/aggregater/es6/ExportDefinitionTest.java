package tech.rsqn.utils.jjst.aggregater.es6;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Andy Chau on 7/9/20.
 */
public class ExportDefinitionTest {

    @Test
    void shouldFindInlineExportFunction() {
        List<String> contentLines = Arrays.asList(
                "export function myFun()"
                , "{"
                , "something..."
                , "};"
        );

        ExportDefinition ep = new ExportDefinition(contentLines);
        Map<String, ExportDefinition.Definition> map = ep.getExports();

        assertThat(map.isEmpty(), equalTo(false));
        assertThat(map.size(), equalTo(1));

        map.forEach((k, v) -> {
            assertThat(k, equalTo("myFun"));
            assertThat(v.type, equalTo(ExportDefinition.Definition.Type.FUNCTION));
            assertThat(v.line, equalTo(1));
        });

        contentLines = Arrays.asList(
                "export function myFun1()"
                , "{"
                , "something..."
                , "};"
                , "export function myFun2(p1, p2)"
                , "{"
                , "something..."
                , "};"
        );

        ep = new ExportDefinition(contentLines);
        map = ep.getExports();

        assertThat(map.isEmpty(), equalTo(false));
        assertThat(map.size(), equalTo(2));

        assertThat(map.containsKey("myFun1"), equalTo(true));
        assertThat(map.get("myFun1").line, equalTo(1));
        assertThat(map.get("myFun1").params, equalTo("()"));
        assertThat(map.containsKey("myFun2"), equalTo(true));
        assertThat(map.get("myFun2").line, equalTo(5));
        assertThat(map.get("myFun2").params, equalTo("(p1, p2)"));

    }

    @Test
    void shouldFindInlineExportClass() {
        List<String> contentLines = Arrays.asList(
                "export class MyClass"
                , "{"
                , "something..."
                , "};"
        );

        ExportDefinition ep = new ExportDefinition(contentLines);
        Map<String, ExportDefinition.Definition> map = ep.getExports();

        assertThat(map.isEmpty(), equalTo(false));
        assertThat(map.size(), equalTo(1));

        map.forEach((k, v) -> {
            assertThat(k, equalTo("MyClass"));
            assertThat(v.type, equalTo(ExportDefinition.Definition.Type.CLASS));
        });
    }

    @Test
    void todoHandleComment() {
        List<String> contentLines = Arrays.asList(
                "// Will export this function as myFunction"
                , "export function myFunction()"
                , "{"
                , "something..."
                , "};"
        );

        // comment should be ignored!!!!
        ExportDefinition ep = new ExportDefinition(contentLines);
        Map<String, ExportDefinition.Definition> map = ep.getExports();
        assertThat(map.size(), equalTo(1));

    }
}
