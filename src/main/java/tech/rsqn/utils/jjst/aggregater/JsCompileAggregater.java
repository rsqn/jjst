package tech.rsqn.utils.jjst.aggregater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.rsqn.utils.jjst.aggregater.es6.FunctionTemplate;
import tech.rsqn.utils.jjst.aggregater.es6.ModuleScanner;
import tech.rsqn.utils.jjst.aggregater.es6.module.Module;
import tech.rsqn.utils.jjst.util.FileUtil;
import tech.rsqn.utils.jjst.util.ResourceUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author Andy Chau on 7/9/20.
 */
public class JsCompileAggregater implements Aggregater {

    private static Logger log = LoggerFactory.getLogger(JsCompileAggregater.class);

    static final String TYPE = "javascript";

    /**
     * With ES6 aggregater will scan all files in the given directory and preprocess all content.
     *
     * @param buffer
     * @param cwd
     * @param jsFilePath
     * @param profiles
     * @throws IOException
     */
    @Override
    public void aggregateFromFile(final StringBuffer buffer,
                                  final File cwd,
                                  final String jsFilePath,
                                  final Collection<String> profiles) throws IOException {
        Objects.requireNonNull(buffer, "Parameter buffer is required");
        Objects.requireNonNull(cwd, "Parameter cwd is required");
        Objects.requireNonNull(jsFilePath, "Parameter jsFilePath is required");

        final ModuleScanner scanner = new ModuleScanner(cwd.toPath(), Paths.get(jsFilePath));

        this.buildOutput(buffer, scanner);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    private StringBuffer buildOutput(final StringBuffer buffer, final ModuleScanner scanner) throws IOException {
        final Map<String, Module> moduleMap = scanner.scan();

        this.addModuleRegistry(buffer);

        // this is the root index file need to process last
        final Module rootIdx = moduleMap.get(scanner.getIndexFile());

        // register modules other than the root file
        moduleMap.entrySet().forEach(e -> {
            if (!e.getKey().equals(scanner.getIndexFile())) {
                try {
                    String func = new FunctionTemplate().generateFunction(e.getValue());
                    buffer.append(func);
                } catch (IOException ex) {
                    final String err = String.format("Unable to convert module to function template: %s", e.getKey());
                    log.error(err, ex);
                    throw new RuntimeException(err, ex);
                }
            }
        });

        return buffer;
    }

    /**
     * Method to add the {@code module.registry.js} at the beginning of buffer.
     * @param buffer
     * @return
     */
    private StringBuffer addModuleRegistry(final StringBuffer buffer) throws IOException {
        final String resourcePath = ResourceUtil.getResource(Paths.get("/",
                "aggregate", "ES6", "module.registry.js").toString());

        buffer.append(FileUtil.getFileContents(new File(resourcePath)));
        return buffer;
    }
}
