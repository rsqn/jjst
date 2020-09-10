package tech.rsqn.utils.jjst.aggregater.es6.module;

/**
 * @author Andy Chau on 10/9/20.
 */
public abstract class BaseJsObject {

    enum Type {
        VALUE,  // not supported yet!
        FUNCTION,
        CLASS;
    }

    private String name;
    private boolean exported;
    private Type type;

    protected BaseJsObject(final Type type, final String name) {
        this.type = type;
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isExported() {
        return exported;
    }

    public void setExported(boolean exported) {
        this.exported = exported;
    }
}
