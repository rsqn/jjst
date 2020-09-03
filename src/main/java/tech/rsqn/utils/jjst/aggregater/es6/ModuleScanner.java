package tech.rsqn.utils.jjst.aggregater.es6;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Scanner scans from root js and store all the module JS file into memory.
 * @author Andy Chau on 3/9/20.
 */
public class ModuleScanner {


    /**
     * Map holds module file as key, file content stored as value.
     */
    private Map<String, String> map;

    public ModuleScanner() {
        this.map = new HashMap<>();
    }

    public void scanModule(Path workingDirection, final String filename) {

        // TODO
        // Considerations:
        // 1. modules from root path never changes
        // 2. contain of a module doesn't change
        // 3. only import from different model changes


        // Therefore:
        // When process each modules' import either:
        // - consider the path relative to the module (faster)
        // - just go read the file content and lookup and compare the value (slower)

    }

}
