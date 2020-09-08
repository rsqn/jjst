package tech.rsqn.utils.jjst.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.stream.Collectors;

public class ResourceUtil {
    private static Logger log = LoggerFactory.getLogger(ResourceUtil.class);

    public static final String getResourceRoot() throws IOException {
        return getResource("/");
    }

    public static final String getResource(final String resourceFile) throws IOException {
        final URL url = ResourceUtil.class.getResource(resourceFile);

        if (url == null) {
            throw new IOException(String.format("Resource: %s not found!", resourceFile));
        }
        return url.getPath();
    }

    /**
     * Loading content from class path resources the path provided should be from root of class resources.
     * @param path Class resource path, not absolute path from file system.
     * @return
     * @throws IOException
     * @deprecated Use {@link #loadContentFromFileSystem(String)} instead.
     */
    @Deprecated
    public static final String loadContentFromResource(String path) throws IOException {

        log.info("Loading {} from classpath", path);

        final InputStream inputStream = ResourceUtil.class.getResourceAsStream(path);
        if (inputStream == null) {
            throw new IOException(String.format("Path provided does not exist: %s", path));
        }

        return readInputStream(inputStream);
    }

    public static final String loadContentFromFileSystem(String absolutePath) throws IOException {
        log.info("Loading {} from file system", absolutePath);

        final InputStream inputStream = FileUtils.openInputStream(new File(absolutePath));

        return readInputStream(inputStream);
    }

    public static final String loadContentFromFileSystem(File absolutePath) throws IOException {
        log.info("Loading {} from file system", absolutePath);

        final InputStream inputStream = FileUtils.openInputStream(absolutePath);

        return readInputStream(inputStream);
    }


    private static String readInputStream(final InputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream, "Parameter inputStream is required");
        try {
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
                return buffer.lines().collect(Collectors.joining("\n"));
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
