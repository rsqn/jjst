package tech.rsqn.utils.jjst.aggregater.es6;

import tech.rsqn.utils.jjst.util.RegexHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Class the provide export definition of a module, provide export type and name of the export.
 *
 * @author Andy Chau on 7/9/20.
 */
public class ExportDefinition {


    //
    // TODO: Add support to export {} with name assignment.
    // TODO: Add support export variable names
    //

    // Inline export support class and function, group 1 as type, group 2 as the name of class or function
    public static final String REGEX_FUNCTION_DEFINITION = "^.*(function)\\s([\\w]*).*(\\(.*\\))";
    public static final String REGEX_CLASS_DEFINITION = "^.*(class)\\s([\\w]*).*";
    public static final String REGEX_EXPORT_INLINE = ".*(export)\\s";

    public final static Pattern P_FUNCTION_DEFINITION = Pattern.compile(REGEX_FUNCTION_DEFINITION);
    public final static Pattern P_CLASS_DEFINITION = Pattern.compile(REGEX_CLASS_DEFINITION);
    public final static Pattern P_EXPORT_INLINE = Pattern.compile(REGEX_EXPORT_INLINE);

    /** Any function, class which exported*/
    private Map<String, Definition> exports = new HashMap<>();

    /** All the definitions of the module. */
    private Map<String, Definition> definitions = new HashMap<>();

    static class Definition {
        // Type of exports
        enum Type {
            VALUE,  // not supported yet!
            FUNCTION,
            CLASS;
        }

        Type type;
        String name;
        String params;
        int line;
    }

    /**
     * Constructor require the full content of the module
     * @param lines
     */
    public ExportDefinition(final List<String> lines) {
        Objects.requireNonNull(lines, "lines is null");
        final AtomicInteger line = new AtomicInteger(1);

        lines.forEach(l -> this.parseLine(l, line.getAndAdd(1)));
    }

    public Map<String, Definition> getExports() {
        return exports;
    }

    private void parseLine(final String l, final int n) {

        final Definition def = getDefinition(l.trim(), n);
        if (def != null) {
            definitions.put(def.name, def);
        }

        // now see if there is inline export
        final List<String> expGrp = RegexHelper.match(P_EXPORT_INLINE, l);
        if (expGrp.size() == 1) {
            // export detected
            if (def != null) {
                exports.put(def.name, def);
            } else {
                // TODO support for export {}
            }
        }
    }

    private Definition getDefinition(final String l, final int n) {

        if (l.startsWith("//") || l.startsWith("*")) {
            // comment line will skip it.
            return null;
        }

        // try function first
        final List<String> funDefGrp = RegexHelper.match(P_FUNCTION_DEFINITION, l);

        if (funDefGrp.size() == 3) {
            Definition def = new Definition();
            def.name = funDefGrp.get(1);
            def.params = funDefGrp.get(2);
            def.line = n;

            if (Definition.Type.FUNCTION.toString().equals(funDefGrp.get(0).toUpperCase())) {
                def.type = Definition.Type.FUNCTION;
            } else {
                final String err = String.format("Variable export is not supported - %s", def.name);
                throw new UnsupportedOperationException(err);
            }
            return def;
        }

        // try class

        final List<String> classDefGrp = RegexHelper.match(P_CLASS_DEFINITION, l);

        if (classDefGrp.size() == 2) {
            Definition def = new Definition();
            def.name = classDefGrp.get(1);
            def.line = n;
            if (Definition.Type.CLASS.toString().equals(classDefGrp.get(0).toUpperCase())) {
                def.type = Definition.Type.CLASS;
            } else {
                final String err = String.format("Variable export is not supported - %s", def.name);
                throw new UnsupportedOperationException(err);
            }
            return def;
        }

        return null;
    }
}
