package org.unidal.webres.server.support;

import java.io.File;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.servlet.Context;
import org.unidal.webres.server.SimpleResourceConfigurator;
import org.unidal.webres.server.SimpleResourceFilter;
import org.unidal.webres.server.SimpleResourceServlet;
import org.unidal.webres.taglib.support.ResourceTemplateTestSupport;

public abstract class SimpleServerSupport extends ResourceTemplateTestSupport {
   @Override
   protected void configure() throws Exception {
      super.configure();

      getContext().addServlet(SimpleResourceServlet.class, "/js/*");
      getContext().addServlet(SimpleResourceServlet.class, "/css/*");
      getContext().addServlet(SimpleResourceServlet.class, "/img/*");
      getContext().addServlet(SimpleResourceServlet.class, "/images/*");
      getContext().addServlet(SimpleResourceServlet.class, "/f/*");
      getContext().addServlet(SimpleResourceServlet.class, "/z/*");
      getContext().addFilter(SimpleResourceFilter.class, "*.jsp", Handler.ALL);
   }

   @Override
   protected int getServerPort() {
      return 80;
   }

   @Override
   protected File getWarRoot() {
      return new File("src/main/webapp");
   }

   @Override
   protected void postConfigure(Context ctx) {
      new SimpleResourceConfigurator().configure(getRegistry());

      super.postConfigure(ctx);
   }
}
