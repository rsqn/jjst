package tech.rsqn.utils.jjst.aggregater.es6;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.rsqn.utils.jjst.aggregater.es6.module.Module;
import tech.rsqn.utils.jjst.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Scanner scans from root js and store all the module JS file into memory.
 *
 * @author Andy Chau on 3/9/20.
 */
public class ModuleScanner {

    private static Logger log = LoggerFactory.getLogger(ModuleScanner.class);

    /**
     * Map holds module file as key, file content stored as value.
     */
    private Map<String, Module> map;

    /**
     * The path to the index file.
     */
    private Path rootPath;
    /**
     * The index file name
     */
    private String indexFile;

    /**
     * Constructor to create module register, this class will refer
     * the path to the index.js as working root directory.
     *
     * @param rootIndex The system path to the root index js file.
     * @throws NullPointerException     when parameter rootIndex is null
     * @throws IllegalArgumentException when parameter rootIndex does not exist in file system.
     */
    public ModuleScanner(final String rootIndex) {
        Objects.requireNonNull(rootIndex, "Parameter rootIndex is required");

        // checking the file exist in the system or not.

        final File f = new File(rootIndex);
        if (!f.exists()) {
            // file not exist in the path
            throw new IllegalArgumentException(
                    String.format("rootIndex: %s does not exist in file system!", rootIndex));
        }

        this.rootPath = Paths.get(f.getParentFile().toURI());
        this.indexFile = f.getName();
        this.map = new HashMap<>();

        log.info("Loading modules begin from location: {}, indexFile: {}", rootPath, indexFile);
        // load modules
    }

    public Path getRootPath() {
        return rootPath;
    }

    public String getIndexFile() {
        return indexFile;
    }

    public Map<String, Module> getMap() {
        return map;
    }

    /**
     * To command a scan, if file content changed or new file added will update the map, removed file will
     * still remain in the map.
     *
     * @return
     * @throws IOException
     */
    public Map<String, Module> scan() throws IOException {

        // walk through from the root
        FileUtil.scanDirectory(rootPath, "js", "jsx")
                .forEach((p -> {
                    try {
                        this.readFromPath(p);
                    } catch (IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
                }));
        return map;
    }

    private void readFromPath(final Path p) throws IOException {
        final String content = FileUtil.readFileContent(p);
        final String name = rootPath.relativize(p).toString();

        map.put(name, new Module(name, content));
    }

    /**
     * @param cwd      Current working directory
     * @param filename The module file name
     */
    private void readModules(final Path cwd, final String filename) {

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
