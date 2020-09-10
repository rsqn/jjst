package tech.rsqn.utils.jjst.aggregater.es6;

import tech.rsqn.utils.jjst.aggregater.es6.module.Module;

import java.util.Objects;

/**
 * @author Andy Chau on 8/9/20.
 */
public class ModuleConverter {

    private Module module;

    public ModuleConverter(final Module module) {
        Objects.requireNonNull(module, "Parameter module is required");
        this.module = module;
    }

    public String convert() {

        return null;

    }
}
