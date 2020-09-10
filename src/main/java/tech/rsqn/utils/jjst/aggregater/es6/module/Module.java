package tech.rsqn.utils.jjst.aggregater.es6.module;

import tech.rsqn.utils.jjst.util.RegexHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * @author Andy Chau on 10/9/20.
 */
public class Module {

    public static final String BLOCK_OPEN = "{";
    public static final String BLOCK_CLOSE = "}";
    public static final String SEMICOLON = ";";

    // Inline export support class and function, group 1 as type, group 2 as the name of class or function
    public static final String REGEX_FUNCTION_DEFINITION = "^.*(function)\\s([\\w]*).*(\\(.*\\))";
    public static final String REGEX_CLASS_DEFINITION = "^.*(class)\\s([\\w]*).*";
    public static final String REGEX_EXPORT_INLINE = ".*(export)\\s";

    public final static Pattern P_FUNCTION_DEFINITION = Pattern.compile(REGEX_FUNCTION_DEFINITION);
    public final static Pattern P_CLASS_DEFINITION = Pattern.compile(REGEX_CLASS_DEFINITION);
    public final static Pattern P_EXPORT_INLINE = Pattern.compile(REGEX_EXPORT_INLINE);

    private String name;
    private String fullContent;
    private List<String> lines = new ArrayList<>();
    private List<Import> imports = new ArrayList<>();
    private List<JsFunction> jsFuncs = new ArrayList<>();
    private JsClass jsClass;

    public Module(final String name, final String fullContent) {
        this.name = name;
        this.fullContent = fullContent;
        this.parseModule();
    }

    public String getName() {
        return name;
    }

    public String getFullContent() {
        return fullContent;
    }

    public List<String> getLines() {
        return lines;
    }

    public List<Import> getImports() {
        return imports;
    }

    public List<JsFunction> getJsFuncs() {
        return jsFuncs;
    }

    public JsClass getJsClass() {
        return jsClass;
    }

    private void parseModule() {
        final AtomicInteger lineIdx = new AtomicInteger(0);
        lines = Arrays.asList(fullContent.split(System.lineSeparator()));

        boolean captureBody = false;
        boolean blockOpened = false;
        BaseJsObject curJsObj = null;
        int blockDeep = 0;

        while (lineIdx.get() < lines.size()) {
            final String l = lines.get(lineIdx.getAndAdd(1)).trim();

            if (l.length() == 0 || (l.startsWith("//") || l.startsWith("*"))) {
                // empty line or comment line will be skipped.
                continue;
            }

            if (!captureBody) {
                curJsObj = this.parseLine(l, lineIdx.get());

                if (curJsObj != null) {
                    // either function or class detected require to capture the body
                    curJsObj.setExported(this.hasInlineExport(l));
                    captureBody = true;
                    if (l.endsWith(BLOCK_OPEN)) {
                        blockOpened = true;
                        blockDeep++;
                    }
                }
            } else {
                // this line is part of function or class function body
                if (!blockOpened && l.startsWith(BLOCK_OPEN)) {
                    // { is in a new line
                    blockOpened = true;
                    blockDeep++;
                    continue;
                }

                final String noSpace = l.replaceAll("\\s+", "");
                if (endWithCloseBlock(noSpace)) {
                    blockDeep--;
                    if (blockDeep == 0) {
                        // body closed
                        captureBody = false;
                        blockOpened = false;
                        curJsObj = null;
                    }
                    continue;
                }

                if (curJsObj.getType().equals(BaseJsObject.Type.CLASS)) {
                    // TODO handle class body which has nested function

                } else if (curJsObj.getType().equals(BaseJsObject.Type.FUNCTION)) {
                    // need to capture function body
                    ((JsFunction) curJsObj).addBodyLine(l);
                }
            }
        }
    }

    private BaseJsObject parseLine(final String l, final int n) {

        // try function first
        final JsFunction jsFunc = convertToJsFunction(l);
        if (jsFunc != null) {
            jsFuncs.add(jsFunc);
            return jsFunc;
        }

        // try class
        final JsClass jsClass = this.convertToJsClass(l);
        if (jsClass != null) {
            this.jsClass = jsClass;
            return jsClass;
        }

        return null;
    }

    private boolean endWithCloseBlock(final String noSpace) {
        return (noSpace.endsWith(BLOCK_CLOSE) || noSpace.endsWith(BLOCK_CLOSE + SEMICOLON));
    }

    private void parseClassInnerLine(final List<String> lines, final AtomicInteger lineIdx, final JsClass jsClass) {

    }

    private JsFunction convertToJsFunction(final String l) {

        final List<String> funDefGrp = RegexHelper.match(P_FUNCTION_DEFINITION, l);
        if (funDefGrp.size() == 3) {
            final String name = funDefGrp.get(1);
            final String params = funDefGrp.get(2);
            return new JsFunction(name, params);
        }

        return null;
    }

    private JsClass convertToJsClass(final String l) {
        final List<String> classDefGrp = RegexHelper.match(P_CLASS_DEFINITION, l);

        if (classDefGrp.size() == 2) {
            final String name = classDefGrp.get(1);
            return new JsClass(name);
        }
        return null;
    }

    private boolean hasInlineExport(final String l) {
        // now see if there is inline export
        final List<String> expGrp = RegexHelper.match(P_EXPORT_INLINE, l);
        if (expGrp.size() == 1) {
            // export detected
            return true;
        }
        return false;
    }
}
