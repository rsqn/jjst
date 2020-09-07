package tech.rsqn.utils.jjst.aggregater.es6;

import tech.rsqn.utils.jjst.util.RegexHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public static final String REGEX_DEFINITION = "^.*(class|function)\\s([\\w]*).*";
    public static final String REGEX_EXPORT_INLINE = "^.*(export)\\s";

    public final static Pattern P_DEFINITION = Pattern.compile(REGEX_DEFINITION);
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
    }

    /**
     * Constructor require the full content of the module
     * @param lines
     */
    public ExportDefinition(final List<String> lines) {
        Objects.requireNonNull(lines, "lines is null");

        lines.forEach(l -> this.parseLine(l));
    }

    public Map<String, Definition> getExports() {
        return exports;
    }

    private void parseLine(final String l) {

        final Definition def = getDefinition(l);
        if (def != null) {
            definitions.put(def.name, def);
        }

        // now see if there is inline export
        final List<String> expGrp = RegexHelper.match(P_EXPORT_INLINE, l);
        if (expGrp.size() == 1) {
            // export detected
            exports.put(def.name, def);
        }
    }

    private Definition getDefinition(final String l) {

        final List<String> defGrp = RegexHelper.match(P_DEFINITION, l);

        if (defGrp.size() == 2) {
            Definition def = new Definition();
            def.name = defGrp.get(1);
            if (Definition.Type.FUNCTION.toString().equals(defGrp.get(0).toUpperCase())) {
                def.type = Definition.Type.FUNCTION;
            } else if (Definition.Type.CLASS.toString().equals(defGrp.get(0).toUpperCase())) {
                def.type = Definition.Type.CLASS;
            } else {
                final String err = String.format("Variable export is not supported - %s", def.name);
                throw new UnsupportedOperationException(err);
            }
            return def;
        } else {
            return null;
        }
    }
}
