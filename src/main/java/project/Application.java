package project;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * The main class. Launch the application.
 *
 * @author Alexander Naumov.
 */
public class Application {

    private static final String PORT = "8080";

    private static final String WEB_DIR = "src/web";

    public static void main(String[] args) throws Exception {

        String port = System.getenv("PORT") != null ? System.getenv("PORT") : PORT;

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.valueOf(port));

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(WEB_DIR).getAbsolutePath());

        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }
}