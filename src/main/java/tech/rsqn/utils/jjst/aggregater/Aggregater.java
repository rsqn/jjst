package tech.rsqn.utils.jjst.aggregater;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * @author Andy Chau on 28/8/20.
 */
public interface Aggregater {

    void aggregateFromFile(StringBuffer buffer, File cwd, String jsFilePath, Collection<String> profiles)
            throws IOException;


    String getSpec();

}
