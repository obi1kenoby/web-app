package project.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;

/**
 * Registers web application context.
 *
 * @author Alexander Naumov.
 */
public class AppWebInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {
        var ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(ApplicationConfig.class);
        ctx.setServletContext(servletContext);

        var servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
    }
}
