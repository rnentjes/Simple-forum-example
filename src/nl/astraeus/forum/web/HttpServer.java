package nl.astraeus.forum.web;

import nl.astraeus.http.SimpleWebServer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * User: rnentjes
 * Date: 3/25/12
 * Time: 1:09 PM
 */
public class HttpServer {
    
    public static void main(String args []) throws Exception {
        int port = 8080;

        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        startSimpleServer(port);
        startJetty(port+1);
    }

    public static void startSimpleServer(int port) {
        SimpleWebServer server = new SimpleWebServer(port);

        server.addServlet(new ResourceServlet(), "/resources/*");
        server.addServlet(new ForumServlet(), "/*");

        server.start();
    }

    public static void startJetty(int port) throws Exception {
        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.setContextPath("/");

        server.setHandler(context);

        ServletHolder resourceServlet = new ServletHolder(new ResourceServlet());
        ServletHolder forumServlet = new ServletHolder(new ForumServlet());

        context.addServlet(resourceServlet, "/resources/*");
        context.addServlet(forumServlet, "/*");

        server.start();

        for (Connector con : server.getConnectors()) {
            System.out.println("Found connector: "+con);
            if (con instanceof SelectChannelConnector) {
                SelectChannelConnector scc = (SelectChannelConnector)con;

                System.out.println("\tAcceptors: "+scc.getAcceptors());
                System.out.println("\tQueue size: "+scc.getAcceptQueueSize());
                System.out.println("\tThreads: "+scc.getThreadPool().getThreads());
            }
        }
    }

}
