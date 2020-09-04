package tech.rsqn.utils.jjst.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * Scan and return all file paths from starting path with provided file extensions.
     * @param path
     * @param extensions
     * @return
     */
    public static List<Path> scanDirectory(final Path path, final String... extensions) throws IOException {
        final Set<String> extensionSet = new HashSet<>(Arrays.asList(extensions));

        final List<Path> fileWithName = Files.walk(path)
                .filter(s ->
                        extensionSet.contains(FilenameUtils.getExtension(s.getFileName().toString())))
                .sorted().collect(Collectors.toList());

        return fileWithName;
    }

    public static String readFileContent(final Path path) throws IOException {
        return FileUtils.readFileToString(path.toFile(), StandardCharsets.UTF_8);
    }
}
