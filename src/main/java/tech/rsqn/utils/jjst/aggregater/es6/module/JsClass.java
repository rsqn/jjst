package tech.rsqn.utils.jjst.aggregater.es6.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Andy Chau on 10/9/20.
 */
public class JsClass extends BaseJsObject {


    // Key is constructor declaration string e.g. constructor(firstName), value is the full body from include { and }
    private JsFunction constructor;

    // List of class functions
    private List<String> classMembers = new ArrayList<>();
    private List<JsFunction> classFunctions = new ArrayList<>();
    private List<JsFunction> noneClassFunctions = new ArrayList<>();


    public JsClass(String name) {
        super(Type.CLASS, name);
    }

    public JsFunction getConstructor() {
        return constructor;
    }

    public void setConstructor(JsFunction constructor) {
        this.constructor = constructor;
    }

    public List<String> getClassMembers() {
        return classMembers;
    }

    public List<JsFunction> getClassFunctions() {
        return classFunctions;
    }

    public List<JsFunction> getNoneClassFunctions() {
        return noneClassFunctions;
    }

    public void addClassMember(final String classMember) {
       classMembers.add(classMember);
    }

    public void addClassFunction(final JsFunction jsFunction) {
        Objects.requireNonNull(jsFunction, "Parameter jsFunction is required");
        classFunctions.add(jsFunction);
    }

    public void addNoneClassFunction(final JsFunction jsFunction) {
        Objects.requireNonNull(jsFunction, "Parameter jsFunction is required");
        noneClassFunctions.add(jsFunction);
    }


    /**
     * Return a combined functions including
     * @return
     */
    public List<JsFunction> getAllFuncs() {
        final List<JsFunction> all = new ArrayList<>();
        all.add(constructor);
        all.addAll(classFunctions);
        all.addAll(noneClassFunctions);
        return all;
    }
}
