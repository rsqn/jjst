package tech.rsqn.utils.jjst.aggregater.es6.module;

import java.util.regex.Pattern;

/**
 * @author Andy Chau on 10/9/20.
 */
public interface ES6Regexs {
    // Inline export support class and function, group 1 as type, group 2 as the name of class or function
    String REGEX_FUNCTION_DEFINITION = "^.*function\\s([\\w]*).*(\\(.*\\))";
    Pattern P_FUNCTION_DEFINITION = Pattern.compile(REGEX_FUNCTION_DEFINITION);

    String REGEX_CLASS_DEFINITION = "^.*class\\s([\\w]*).*";
    Pattern P_CLASS_DEFINITION = Pattern.compile(REGEX_CLASS_DEFINITION);

    String REGEX_EXPORT_INLINE = ".*(export)\\s";
    Pattern P_EXPORT_INLINE = Pattern.compile(REGEX_EXPORT_INLINE);

    String REGEX_CLASS_CONSTRUCTOR = "^.*(constructor)[\\s+]?(\\(.*\\))";
    Pattern P_CLASS_CONSTRUCTOR = Pattern.compile(REGEX_CLASS_CONSTRUCTOR);

    String REGEX_CLASS_FUNCTION = "^[\\s]*?([\\w]*)[\\s]?(\\(.*\\)).*";
    Pattern P_CLASS_FUNCTION = Pattern.compile(REGEX_CLASS_FUNCTION);

    /**
     * Expression to get name and module location.
     * <p>
     *  Example: import {doubleIt} from './js/tools.js'.
     *  Result: 'doubleIt', './js/tools.js'.
     * </p>
     *
     * <p>
     *  Example: import {addOne, minusOne} from './js/api/math.js'.
     *  Results:
     *      'addOne', './js/api/math.js'
     *      'minusOne', './js/api/math.js'
     * </p>
     */
    String REGEX_NAMED_IMPORT = "^import\\s\\{(.*)}\\sfrom\\s['\"](.*)['\"]";
    Pattern P_IMPORT_NAMED = Pattern.compile(REGEX_NAMED_IMPORT);

    /**
     * Expression to get wildcard name from an module.
     * <p>
     *  Example: import * as math from './js/api/math.js'
     *  Result: 'math', './js/api/math.js'
     * </p>
     * TODO add support to wild card import
     */
    String REGEX_WILDCARD_IMPORT = "^import\\s\\*\\sas\\s(.*)\\sfrom\\s['\"](.*)['\"]";
    Pattern P_IMPORT_WILDCARD = Pattern.compile(REGEX_WILDCARD_IMPORT);

}
