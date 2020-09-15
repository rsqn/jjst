package tech.rsqn.utils.jjst.aggregater;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * @author Andy Chau on 15/9/20.
 */
public class CssAggregater implements Aggregater {

    public CssAggregater() {
    }

    @Override
    public String getType() {
        return "css";
    }

    @Override
    public void aggregateFromFile(StringBuffer buffer, File cwd, String jsFilePath, Collection<String> profiles) throws IOException {
        // TODO
    }

}
