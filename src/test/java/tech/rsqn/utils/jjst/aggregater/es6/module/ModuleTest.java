package tech.rsqn.utils.jjst.aggregater.es6.module;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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
                , "private func"
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
        assertThat(f3.getBodyLines().get(0), equalTo("private func"));
    }

    private String concatList(List<String> list) {
        return list.stream().collect(Collectors.joining(System.lineSeparator()));
    }
}
