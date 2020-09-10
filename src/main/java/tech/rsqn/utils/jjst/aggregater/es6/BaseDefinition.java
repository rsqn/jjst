package tech.rsqn.utils.jjst.aggregater.es6;

/**
 * @author Andy Chau on 10/9/20.
 */
public abstract class BaseDefinition {

    // Type of definition
    enum Type {
        VALUE,  // not supported yet!
        FUNCTION,
        CLASS;
    }

    String name;
    int line;
    Type type;

}
