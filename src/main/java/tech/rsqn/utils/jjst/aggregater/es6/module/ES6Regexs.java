package tech.rsqn.utils.jjst.aggregater.es6.module;

import java.util.regex.Pattern;

/**
 * @author Andy Chau on 10/9/20.
 */
public interface ES6Regexs {
    // Inline export support class and function, group 1 as type, group 2 as the name of class or function
    String REGEX_FUNCTION_DEFINITION = "^.*(function)\\s([\\w]*).*(\\(.*\\))";
    Pattern P_FUNCTION_DEFINITION = Pattern.compile(REGEX_FUNCTION_DEFINITION);

    String REGEX_CLASS_DEFINITION = "^.*(class)\\s([\\w]*).*";
    Pattern P_CLASS_DEFINITION = Pattern.compile(REGEX_CLASS_DEFINITION);

    String REGEX_EXPORT_INLINE = ".*(export)\\s";
    Pattern P_EXPORT_INLINE = Pattern.compile(REGEX_EXPORT_INLINE);
}
