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
        });

        contentLines = Arrays.asList(
                "export function myFun1()"
                , "{"
                , "something..."
                , "};"
                , "export function myFun2()"
                , "{"
                , "something..."
                , "};"
        );

        ep = new ExportDefinition(contentLines);
        map = ep.getExports();

        assertThat(map.isEmpty(), equalTo(false));
        assertThat(map.size(), equalTo(2));

        assertThat(map.containsKey("myFun1"), equalTo(true));
        assertThat(map.containsKey("myFun2"), equalTo(true));

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
}
