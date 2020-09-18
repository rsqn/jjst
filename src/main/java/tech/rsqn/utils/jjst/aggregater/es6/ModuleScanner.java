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
     * The system resource path.
     */
    private Path resourcePath;

    /**
     * The index file name
     */
    private String indexFile;

    /**
     * The path to the index file.
     */
    private Path indexPath;


    /**
     * Constructor to create module register, this class will refer
     * the path to the index.js as working root directory.
     *
     * @param resourcePath The system path to the resources
     * @param rootIndex The relative path to the root index js file, e.g. /js/index.js
     * @throws NullPointerException     when parameter rootIndex is null
     * @throws IllegalArgumentException when parameter rootIndex does not exist in file system.
     */
    public ModuleScanner(final Path resourcePath, final Path rootIndex) {
        Objects.requireNonNull(resourcePath, "Parameter resourcePath is required");
        Objects.requireNonNull(rootIndex, "Parameter rootIndex is required");

        // checking the file exist in the system or not.

        final File f = new File(resourcePath.toString() + rootIndex.toString());
        if (!f.exists()) {
            // file not exist in the path
            throw new IllegalArgumentException(
                    String.format("rootIndex: %s does not exist in file system!", rootIndex));
        }

        this.resourcePath = resourcePath;
        this.indexPath = Paths.get(f.getParentFile().toURI());
        this.indexFile = rootIndex.toString();
        this.map = new HashMap<>();

        log.info("Loading modules begin from location: {}, indexFile: {}", indexPath, indexFile);
        // load modules
    }

    public Path getIndexPath() {
        return indexPath;
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
        FileUtil.scanDirectory(indexPath, "js", "jsx")
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
        final String name = "/" + resourcePath.relativize(p).toString();

        map.put(name, new Module(name, content));
    }

}
