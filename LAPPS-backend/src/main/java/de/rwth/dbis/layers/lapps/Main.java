package de.rwth.dbis.layers.lapps;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.wordnik.swagger.jaxrs.config.BeanConfig;

/**
 * Creates and starts a new instance of {@link HttpServer} waiting for an user input.
 * 
 */
public class Main {
  // Base URI the Grizzly HTTP server will listen on
  public static final String BASE_URI = "http://localhost:8080/lapps/";

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   * 
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() {
    // create a resource config that scans for JAX-RS resources and providers
    // in de.rwth.dbis.layers.lapps package
    // add com.wordnik.swagger.jersey.listing for swagger support
    String[] packages = {"de.rwth.dbis.layers.lapps", "com.wordnik.swagger.jersey.listing"};
    final ResourceConfig rc = new ResourceConfig().packages(packages);

    // Configure swagger
    BeanConfig config = new BeanConfig();
    config.setResourcePackage("de.rwth.dbis.layers.lapps");
    config.setVersion("1.0.0");
    config.setScan(true);
    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    // Host static web page for swagger ui
    server.getServerConfiguration().addHttpHandler(new StaticHttpHandler("src/main/webapp/"), "/");
    return server;
  }

  /**
   * Starts the server.
   * 
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    final HttpServer server = startServer();
    System.out.println(String.format("Jersey app started with WADL available at "
        + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
    System.in.read();
    server.shutdownNow();
  }
}
