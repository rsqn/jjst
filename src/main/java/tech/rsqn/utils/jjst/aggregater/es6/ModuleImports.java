package tech.rsqn.utils.jjst.aggregater.es6;

import java.util.Map;

/**
 * Class to convert module imports with it assigned name and module file name.
 * @author Andy Chau on 4/9/20.
 */
public class ModuleImports {
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
    public static final String REGEX_NAMED_IMPORT = "^import\\s(\\{.*})\\sfrom\\s'(.*)'";

    /**
     * Expression to get wildcard name from an module.
     * <p>
     *  Example: import * as math from './js/api/math.js'
     *  Result: 'math', './js/api/math.js'
     * </p>
     */
    public static final String REGEX_WILDCARD_IMPORT = "^import\\s\\*\\sas\\s(.*)\\sfrom\\s'(.*)'";

    private Map<String, String> importMap;

    public ModuleImports(final String modulePath) {
        // considerations

    }



}
