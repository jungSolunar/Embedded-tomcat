package com.solunar.init;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

/**
 * Hello world!
 *
 */
public class TomcatServer
{
    private static final String PATH_CONF_FILE = "config-server.properties";

    private int port;

    private Tomcat tomcat;

    public void init() throws Exception{
        Configuration config = new PropertiesConfiguration(PATH_CONF_FILE);
        this.port = config.getInt("server.port");

        tomcat = new Tomcat();
        tomcat.setPort(this.port);

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File("src/main/webapp").getAbsolutePath());
        System.out.println("configuring app with basedir: " + new File("./" + "src/main/webapp").getAbsolutePath());

        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);
    }

    public void run() throws Exception{
        this.tomcat.start();
        this.tomcat.getServer().await();
    }

    public static void main( String[] args ) throws Exception {
        try {
            System.out.println("Server starting..");
            TomcatServer tomcatServer = new TomcatServer();
            tomcatServer.init();
            tomcatServer.run();
            System.out.println("Server starting.. DONE");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

}
