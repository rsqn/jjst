package tech.rsqn.utils.jjst.service;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static tech.rsqn.utils.jjst.service.Profiles.*;

/**
 * @author Andy Chau on 28/8/20.
 */
public class ProfilesTest {

    @Test
    void shouldPassConstructorString() {

        final String str = String.join(Profiles.DELIMITER, Arrays.asList(CLEAR_CACHE, CLEAR_CACHE, NO_CACHE));
        final Profiles p = new Profiles(str);

        assertThat(p.contains(CLEAR_CACHE), equalTo(true));
        assertThat(p.contains(NO_CACHE), equalTo(true));
        assertThat(p.contains(NO_COMPILE), equalTo(false));

        assertThat(p.getProfiles().size(), equalTo(2));

        // it should not allow to remove from readonly set.
        assertThrows(UnsupportedOperationException.class, () -> p.getProfiles().remove(CLEAR_CACHE));

        final Profiles emptyStr = new Profiles("");
        assertThat(emptyStr.getProfiles().size(), equalTo(0));

        final Profiles def = new Profiles();
        assertThat(def.getProfiles().size(), equalTo(0));
    }

    @Test
    void shouldPassConstructorProfilesString() {

        final String str = String.join(Profiles.DELIMITER, Arrays.asList(CLEAR_CACHE));
        final Profiles base = new Profiles(str);

        final Profiles addition = new Profiles(base, NO_CACHE);

        assertThat(addition.contains(CLEAR_CACHE), equalTo(true));
        assertThat(addition.contains(NO_CACHE), equalTo(true));

        final Profiles noaddition = new Profiles(base, null);
        assertThat(noaddition.contains(CLEAR_CACHE), equalTo(true));
    }

    @Test
    void shouldAllowCustomProfiles() {
        final String debugProfile = "debug";

        final String str = String.join(Profiles.DELIMITER, Arrays.asList(CLEAR_CACHE));
        final Profiles base = new Profiles(str);
        final Profiles addition = new Profiles(base, "debug");

        assertThat(addition.contains(CLEAR_CACHE), equalTo(true));
        assertThat(addition.contains(debugProfile), equalTo(true));
    }

    @Test
    void shouldFailToConstruct() {
        assertThrows(NullPointerException.class, () -> new Profiles((String) null));
        assertThrows(IllegalArgumentException.class, () -> new Profiles("abc"));

        assertThrows(NullPointerException.class, () -> new Profiles((String[]) null));
        assertThrows(IllegalArgumentException.class, () -> new Profiles("abc"));
    }
}
