package tech.rsqn.utils.jjst.util;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andy Chau on 7/9/20.
 */
public class RegexHelper {

    public static List<String> match(final Pattern pattern, final String string) {
        Objects.requireNonNull(pattern, "Parameter pattern is required");

        final List<String> groups = new ArrayList<>();

        if (StringUtils.isEmpty(string)) {
            return groups;
        }

        final Matcher m = pattern.matcher(string.trim());
        final int gCnt = m.groupCount();

        if (m.find()) {
            // if group count is bigger than 1, there are subgroups will return the subgroups.
            for (int i = 1; i <= gCnt; i++) {
                final String g = m.group(i);
                groups.add(g == null ? null : g.trim());
            }
        }
        return groups;
    }
}
