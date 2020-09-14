package tech.rsqn.utils.jjst.aggregater.es6.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.rsqn.utils.jjst.util.RegexHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static tech.rsqn.utils.jjst.aggregater.es6.module.ES6Regexs.*;

/**
 * @author Andy Chau on 10/9/20.
 */
public class Module {
    private static Logger log = LoggerFactory.getLogger(Module.class);

    public static final String BLOCK_OPEN = "{";
    public static final String BLOCK_CLOSE = "}";
    public static final String SEMICOLON = ";";

    private String path;
    private String fullContent;
    private List<String> lines = new ArrayList<>();
    private List<Import> imports = new ArrayList<>();
    private List<JsFunction> jsFuncs = new ArrayList<>();
    private JsClass jsClass;

    public Module(final String path, final String fullContent) {
        this.path = path;
        this.fullContent = fullContent;
        this.parseModule();
    }

    public String getPath() {
        return path;
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
            // trim the line first remove spaces in beginning or end of line
            final String l = lines.get(lineIdx.getAndAdd(1)).trim();

            if (isEmptyOrComment(l)) {
                // empty line or comment line will be skipped.
                continue;
            }

            if (!captureBody) {

                // assuming when not capturing body and block is not opened it will be import statement
                final Import im = Import.parseLine(l);
                if (im != null) {
                    imports.add(im);
                    continue;
                }

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

                // handle close block
                if (endWithCloseBlock(l)) {
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

                    // pass current line idx and lines to the function it will handle the line process
                    final int endLine = this.parseClassInnerLines(
                            lines, lineIdx.get() - 1, blockOpened, ((JsClass) curJsObj));
                    lineIdx.set(endLine);

                    blockDeep--;
                    if (blockDeep == 0) {
                        // body closed
                        captureBody = false;
                        blockOpened = false;
                        curJsObj = null;
                    }
                    continue;

                } else if (curJsObj.getType().equals(BaseJsObject.Type.FUNCTION)) {
                    // need to capture function body
                    ((JsFunction) curJsObj).addBodyLine(l);
                }
            }
        }
    }

    private BaseJsObject parseLine(final String l, final int n) {

        // try function first
        final JsFunction jsFunc = convertToFunction(l, P_FUNCTION_DEFINITION);
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

    /**
     * When parsing the class, it consume
     *
     * @param lines
     * @param lineIdx
     * @param blockOpened
     * @param jsClass
     */
    private int parseClassInnerLines(final List<String> lines,
                                     final int lineIdx,
                                     final boolean blockOpened,
                                     final JsClass jsClass) {

        boolean captureBody = false;
        boolean classBlockOpen = blockOpened;
        boolean innerBlockOpen = false;
        JsFunction workingFunc = null;
        int blockDeep = blockOpened ? 1 : 0;
        final AtomicInteger innerLineIdx = new AtomicInteger(lineIdx);

        while (innerLineIdx.get() < lines.size()) {
            // due to in main loop already incremented 1, we need to get the previous line to process.
            final String l = lines.get(innerLineIdx.getAndAdd(1)).trim();

            if (isEmptyOrComment(l)) {
                // empty line or comment line will be skipped.
                continue;
            }

            if (!captureBody && !classBlockOpen && l.startsWith(BLOCK_OPEN)) {
                // { is in a new line for class
                classBlockOpen = true;
                blockDeep++;
                continue;
            }

            if (!captureBody && endWithCloseBlock(l)) {
                blockDeep--;
                if (blockDeep == 0) {
                    classBlockOpen = false;
                }
                continue;
            }

            if (captureBody) {
                // go through the body of current working function (constructor also is a function)
                // this line is part of function or class function body
                if (!innerBlockOpen && l.startsWith(BLOCK_OPEN)) {
                    // { is in a new line
                    innerBlockOpen = true;
                    blockDeep++;
                    continue;
                }

                // handle close block
                if (endWithCloseBlock(l)) {
                    blockDeep--;
                    if (blockDeep == 1) {
                        // body closed
                        captureBody = false;
                        innerBlockOpen = false;
                        workingFunc = null;
                    }
                    continue;
                }

                // need to capture function body
                workingFunc.addBodyLine(l);

                continue;
            }

            // Class constructor
            workingFunc = this.convertToFunction(l, P_CLASS_CONSTRUCTOR);
            if (workingFunc != null) {
                // constructor detected need to capture body
                jsClass.setConstructor(workingFunc);
                captureBody = true;

                if (l.endsWith(BLOCK_OPEN)) {
                    innerBlockOpen = true;
                    blockDeep++;
                }
                continue;
            }

            // Class function
            workingFunc = this.convertToFunction(l, P_CLASS_FUNCTION);
            if (workingFunc != null) {
                // class function detected need to capture its body
                jsClass.addClassFunction(workingFunc);
                captureBody = true;

                if (l.endsWith(BLOCK_OPEN)) {
                    innerBlockOpen = true;
                    blockDeep++;
                }
                continue;
            }

            // Normal function outside of class
            workingFunc = this.convertToFunction(l, P_FUNCTION_DEFINITION);
            if (workingFunc != null) {
                jsClass.addNoneClassFunction(workingFunc);
                captureBody = true;

                if (l.endsWith(BLOCK_OPEN)) {
                    innerBlockOpen = true;
                    blockDeep++;
                }
                continue;
            }

            // if none of above assuming the line is a member variable
            jsClass.addClassMember(l);
        }

        // when inner block not open means already closed.
        return innerLineIdx.get();
    }

    private JsFunction convertToFunction(final String l, final Pattern pattern) {
        final List<String> grps = RegexHelper.match(pattern, l);
        if (grps.size() == 2) {
            final String name = grps.get(0);
            final String params = grps.get(1);

            log.debug("Detected function - name: {}, params: {}", name, params);
            return new JsFunction(name, params);
        }
        return null;
    }

    private JsClass convertToJsClass(final String l) {
        final List<String> classDefGrp = RegexHelper.match(P_CLASS_DEFINITION, l);

        if (classDefGrp.size() == 1) {
            final String name = classDefGrp.get(0);

            log.debug("Detected class - name: {}", name);
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

    private boolean endWithCloseBlock(final String l) {
        final String noSpace = l.replaceAll("\\s+", "");
        return (noSpace.endsWith(BLOCK_CLOSE) || noSpace.endsWith(BLOCK_CLOSE + SEMICOLON));
    }

    private boolean isEmptyOrComment(final String l) {
        return (l.length() == 0 || (l.startsWith("//") || l.startsWith("*")));
    }
}
