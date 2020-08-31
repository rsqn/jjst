package tech.rsqn.utils.jjst.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Profiles which jjst supported.
 *
 * <ul>
 *     <li>clearcache</li>
 *     <li>nocache</li>
 *     <li>nocompile</li>
 * </ul>
 *
 */
public class Profiles {

    public final static String DELIMITER = ",";

    private static Logger log = LoggerFactory.getLogger(Profiles.class);

    public static final String CLEAR_CACHE = "clearcache";
    public static final String NO_CACHE = "nocache";
    public static final String NO_COMPILE = "nocompile";

    private static final Set<String> SUPPORTED_PROFILES =new HashSet<>(Arrays.asList(CLEAR_CACHE, NO_CACHE, NO_COMPILE));

    private Set<String> profileSet = new HashSet<>();

    /**
     * Empty profile, which mean not profile has been provided.
     */
    public Profiles() {
    }

    /**
     * Constructor will convert comma separated string into a list.
     * @param profiles Accept null and will not throw exception, if not null will split string into internal set.
     */
    public Profiles(final String profiles) {
        Objects.requireNonNull(profiles, "Parameter profiles is required");

        if (profiles.isEmpty()) {
            return;
        }

        this.add(profiles.trim().split(DELIMITER));

        log.info("Profiles has been registered {}", profileSet);
    }

    /**
     * Constructor with array of profiles.
     * @param profiles
     */
    public Profiles(String... profiles) {
        Objects.requireNonNull(profiles, "Parameter profiles is required");

        this.add(profiles);
    }

    /**
     * Create an instance base on a base profile, with some additional.
     * @param base Required base profile
     * @param additions Optional profile can be null.
     */
    public Profiles(final Profiles base, final String additions) {
        Objects.requireNonNull(base, "Parameter base is required");

        this.profileSet = new HashSet<>(base.getProfiles());

        if (Objects.isNull(additions)) {
            return;
        }

        this.add(additions.trim().split(DELIMITER));
    }

    public boolean contains(final String profile) {
        if (Objects.isNull(profile)) {
            return false;
        }

        return profileSet.contains(profile);
    }

    /**
     * Return a unmodifiable set.
     * @return
     */
    public Set<String> getProfiles() {
        return Collections.unmodifiableSet(profileSet);
    }

    @Override
    public String toString() {
        return profileSet.toString();
    }

    private void add(final String[] profiles) {

        Arrays.asList(profiles).forEach(s -> {
            if (!profileSet.contains(s)) {
                profileSet.add(s);
            }
        });
    }

}
