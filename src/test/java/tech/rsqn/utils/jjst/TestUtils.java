package tech.rsqn.utils.jjst;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Andy Chau on 14/9/20.
 */
public class TestUtils {

    public static String concatList(List<String> list) {
        return list.stream().collect(Collectors.joining(System.lineSeparator()));
    }
}
