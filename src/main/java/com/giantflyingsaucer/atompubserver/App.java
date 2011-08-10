/*
 * Original code created by the Apache Abdera team
 * http://abdera.apache.org/
 */
package com.giantflyingsaucer.atompubserver;

import org.apache.abdera.protocol.server.Provider;
import org.apache.abdera.protocol.server.impl.DefaultProvider;
import org.apache.abdera.protocol.server.impl.SimpleWorkspaceInfo;
import org.apache.abdera.protocol.server.servlet.AbderaServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class App {

    public static void main(String... args) throws Exception {
        int port = 9002;
        try {
            port = args.length > 0 ? Integer.parseInt(args[0]) : 9002;
        } catch (Exception e) {
        }
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        ServletHolder servletHolder = new ServletHolder(new EmployeeProviderServlet());
        context.addServlet(servletHolder, "/*");
        server.start();
        server.join();
    }

    public static final class EmployeeProviderServlet extends AbderaServlet {
        @Override
        protected Provider createProvider() {
            EmployeeCollectionAdapter ca = new EmployeeCollectionAdapter();
            ca.setHref("employee");

            SimpleWorkspaceInfo wi = new SimpleWorkspaceInfo();
            wi.setTitle("Employee Directory Workspace");
            wi.addCollection(ca);

            DefaultProvider provider = new DefaultProvider("/");
            provider.addWorkspace(wi);

            provider.init(getAbdera(), null);
            return provider;
        }
        
        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            
            super.service(request, response);
        }        
        
    }
}
