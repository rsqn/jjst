package tech.rsqn.utils.jjst.aggregater.es6;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import tech.rsqn.utils.jjst.aggregater.es6.module.Module;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

import static tech.rsqn.utils.jjst.util.ResourceUtil.getResource;

/**
 * @author Andy Chau on 14/9/20.
 */
public class WrapperModuleTemplate {

    private static final String TEMPLATE_NAME = "module.wrapper.function.vm";
    private static final String TEMPLATE_RESOURCES = Paths.get("/", "aggregate", "ES6", TEMPLATE_NAME).toString();

    private Properties engineProps;
    private VelocityEngine ve;
    private Template template;

    public WrapperModuleTemplate() throws IOException {
        final String resourcePath = Paths.get(getResource(TEMPLATE_RESOURCES)).getParent().toString();
        engineProps = new Properties();
        engineProps.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, resourcePath);

        ve = new VelocityEngine();
        ve.init(engineProps);
        template = ve.getTemplate(TEMPLATE_NAME);
    }

    public String generateFunction(final Module module) {
        Objects.requireNonNull(module, "Parameter module is required");

        final VelocityContext ctx = new VelocityContext();
        final StringWriter writer = new StringWriter();

        ctx.put("module", module);
        template.merge(ctx, writer);

        return writer.toString();
    }
}
