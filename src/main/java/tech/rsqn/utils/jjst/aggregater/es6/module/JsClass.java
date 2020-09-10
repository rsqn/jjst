package tech.rsqn.utils.jjst.aggregater.es6.module;

import java.util.List;
import java.util.Map;

/**
 * @author Andy Chau on 10/9/20.
 */
public class JsClass extends BaseJsObject {


    // Key is constructor declaration string e.g. constructor(firstName), value is the full body from include { and }
    private Map<String, String> constructors;

    // List of class functions
    private List<JsFunction> functions;

    private List<String> members;

    public JsClass(String name) {
        super(Type.CLASS, name);
    }

    public Map<String, String> getConstructors() {
        return constructors;
    }

    public List<JsFunction> getFunctions() {
        return functions;
    }

    public List<String> getMembers() {
        return members;
    }

}
