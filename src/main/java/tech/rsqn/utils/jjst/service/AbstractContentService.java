package tech.rsqn.utils.jjst.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.rsqn.utils.jjst.aggregater.Aggregater;
import tech.rsqn.utils.jjst.servlets.AbstractAggregationServlet;
import tech.rsqn.utils.jjst.util.ContentCache;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import static tech.rsqn.utils.jjst.service.Profiles.*;

/**
 * @author Andy Chau on 28/8/20.
 */
public abstract class AbstractContentService {

    private static Logger log = LoggerFactory.getLogger(AbstractAggregationServlet.class);

    private Profiles baseProfile;
    private ContentCache<String, String> cache = new ContentCache<>();

    public AbstractContentService(final Profiles baseProfile) {
        Objects.requireNonNull(baseProfile, "Parameter baseProfile is required.");
        this.baseProfile = baseProfile;
    }

    /**
     *
     * @param cwd Required file instead to where the JS project root it is
     * @param path Required relative path of the JS file relation to the {@code cwd}.
     * @param customProfile Optional ad-hoc profiles.
     * @return The content.
     * @throws IOException Thrown when file can't be read.
     */
    public String getContent(final File cwd, final String path, final String customProfile) throws IOException {
        Objects.requireNonNull(cwd, "Parameter cwd (current working directory) is required");
        Objects.requireNonNull(path, "Parameter path is required");

        final Profiles profiles = new Profiles(this.baseProfile, customProfile);
        final String cacheKey = generateCacheKey(path, profiles.getProfiles());

        this.clearCacheIfRequired(profiles);

        // get and check if content exists in cache
        String contents = cache.get(cacheKey);

        if (contents == null || profiles.contains(NO_CACHE)) {
            log.debug("{} - Profiles = {}", jsSpec(), profiles);

            contents = buildContent(cwd, path, profiles, cacheKey);

            log.info("{} - Aggregation of {} complete", jsSpec(), path);
        } else {
            log.trace("{} - Returning {} from cache ", jsSpec(), path);
        }
        return contents;
    }


    /**
     * Method to perform a clear catch if provided profiles contains {@link Profiles#CLEAR_CACHE}.
     * @param profiles
     */
    protected void clearCacheIfRequired(final Profiles profiles) {
        if (Objects.isNull(profiles)) {
            return;
        }
        if (profiles.contains(CLEAR_CACHE)) {
            cache.clear();
        }
    }

    /**
     * Method will check if profiles contain {@link Profiles#NO_COMPILE} or not, if exists will return none minified
     * content, otherwise use will minifier to compact the content.
     *
     * @param profiles
     * @param content
     * @return
     */
    protected String minifyContent(final Profiles profiles, final String content) {
       if (!profiles.contains(NO_COMPILE)) {
           // TODO implement minifier logic when
//            JavaScriptMinifier minifier = new JavaScriptMinifier();
//            return minifier.minify(contents, false);
        }

       return content;
    }

    protected abstract String jsSpec();

    protected abstract String processFileContent(String content);

    /**
     * Any additional process can be done by providing specific implementations.
     * @param content The aggregated content.
     * @return Post processed content.
     */
    protected abstract String postProcess(String content);

    /**
     * Provide instance of aggregater.
     * @return
     */
    protected abstract Aggregater getAggregater();


    private String generateCacheKey(String path, Collection profiles) {
        // TODO generate cache key should consider minified and none-minified
        return path + profiles;
    }

    /**
     * Method to build javascript content using provided aggregater.
     * @param cwd
     * @param path
     * @param profiles
     * @param cacheKey
     * @return
     * @throws IOException
     */
    private String buildContent(File cwd, String path, Profiles profiles, String cacheKey) throws IOException {
        final StringBuffer buffer = new StringBuffer();

        String contents;

        getAggregater().aggregateFromFile(buffer, cwd, path, profiles.getProfiles());

        contents = buffer.toString();
        contents = postProcess(contents);

        cache.put(cacheKey, contents);

        return contents;
    }

}
