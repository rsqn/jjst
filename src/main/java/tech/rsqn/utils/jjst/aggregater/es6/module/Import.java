package tech.rsqn.utils.jjst.aggregater.es6.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import tech.rsqn.utils.jjst.util.RegexHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class to convert module imports with it assigned name and module file name.
 *
 * @author Andy Chau on 4/9/20.
 */
public class Import {

    private static Logger log = LoggerFactory.getLogger(Import.class);

    enum Type {
        NAMED,  // For import {named} from './path/file.js'
        WILDCARD // For import * as wildcard from './path/file.js'
    }

    private Type type;

    private String path;
    private Set<String> names = new HashSet<>();

    public Import(final Type type, final String path) {
        this.type = type;
        this.path = path;
    }

    public void addName(final String name) {
        names.add(name);
    }

    public Type getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public Set<String> getNames() {
        return names;
    }

    /**
     * Static function to parse a line if the line is import line an instance will be return.
     */
    public static Import parseLine(final String l) {
        if (StringUtils.isEmpty(l)) {
            return null;
        }


        // try named
        Import im;

        im = parseNamed(l);
        if (im != null) {
            return im;
        }

        // try wildcard
        im = parseWildCard(l);

        return im;
    }

    private static Import parseNamed(final String l) {
        final List<String> named = RegexHelper.match(ES6Regexs.P_IMPORT_NAMED, l);
        if (named.size() == 2) {
            // it is named
            log.debug("Detected named import: {}", l);

            final Import im = new Import(Type.NAMED, named.get(1));
            // as named import can be multiple names, therefore each name is require to register
            Arrays.asList(named.get(0).split(",")).forEach(n -> {
                im.addName(n.trim());
            });

            return im;
        }
        return null;
    }

    private static Import parseWildCard(final String l) {
        final List<String> wildcard = RegexHelper.match(ES6Regexs.P_IMPORT_WILDCARD, l);
        if (wildcard.size() == 2) {
            // it is wildcard
            log.debug("Detected wildcard import: {}", l);

            final Import im = new Import(Type.WILDCARD, wildcard.get(1));
            im.addName(wildcard.get(0));

            return im;
        }
        return null;
    }
}
