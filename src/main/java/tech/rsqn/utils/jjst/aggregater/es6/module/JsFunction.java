package tech.rsqn.utils.jjst.aggregater.es6.module;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Andy Chau on 10/9/20.
 */
public class JsFunction extends BaseJsObject {

    private String params;
    private List<String> bodyLines = new ArrayList<>();

    public JsFunction(final String name, final String params) {
        super(Type.FUNCTION, name);
        this.params = params;
    }

    public String getParams() {
        return params;
    }

    public List<String> getBodyLines() {
        return bodyLines;
    }

    public String getBody() {
        return bodyLines.stream().collect(Collectors.joining(System.lineSeparator()));
    }

    public void addBodyLine(final String bodyLine) {
        bodyLines.add(bodyLine);
    }
}
