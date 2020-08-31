package tech.rsqn.utils.jjst.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Andy Chau on 31/8/20.
 */
public class ContentCacheTest {

    @Test
    void shouldWriteAndRead() {
        final ContentCache<String, String> cache = new ContentCache<>();

        String key = "test1";
        String value = "test1_value";

        cache.put(key, value);
        assertThat(cache.containsKey(key), equalTo(true));
        assertThat(cache.get(key), equalTo(value));

        cache.clear();
        assertThat(cache.cachedKeys().size(), equalTo(0));

        cache.put(key, value);
        cache.remove(key);
        assertThat(cache.cachedKeys().size(), equalTo(0));
    }
}
