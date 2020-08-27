package tech.rsqn.utils.jjst.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * File utility class.
 */
public class FileUtil {

    /**
     * To resolve a file by give parent.
     * @param cwd
     * @param relativePath
     * @return
     * @throws IOException
     * @see java.io.File(File, String)
     */
    public static File resolveFile(final File cwd, final String relativePath) throws IOException {
        final File f = new File(cwd, relativePath);
        return f;
    }

    /**
     * Method to read file contents and return as a string.
     * @param f File to read.
     * @return Return the content of provided file.
     * @throws IOException
     */
    public static String getFileContents(final File f) throws IOException {
        Objects.requireNonNull(f, "Parameter f is required");

        final String content = new String(Files.readAllBytes(f.toPath()));
        return content;
    }
}
