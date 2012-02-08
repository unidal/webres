package org.unidal.webres.server.js;

import org.unidal.webres.helper.Joiners;
import org.unidal.webres.helper.Joiners.IBuilder;
import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.annotation.ContextPath;
import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.js.AggregatedJs;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class SimpleAggregatedJsUrlBuilder implements IResourceUrlBuilder<AggregatedJs>,
      IResourceRegisterable<SimpleAggregatedJsUrlBuilder> {
   private String m_contextPath;

   private String m_servletPath;

   public SimpleAggregatedJsUrlBuilder(String servletPath) {
      m_servletPath = servletPath;

      if (servletPath != null) {
         if (!servletPath.startsWith("/") && servletPath.endsWith("/")) {
            throw new RuntimeException("servletPath should be null or starting with '/' but not ending with '/'.");
         }
      }
   }

   @Override
   public String build(IResourceContext ctx, AggregatedJs js) {
      StringBuilder sb = new StringBuilder(128);

      if (m_contextPath != null) {
         sb.append(m_contextPath);
      }

      if (m_servletPath != null) {
         sb.append(m_servletPath);
      }

      IResourceUrn urn = js.getMeta().getUrn();

      sb.append('/').append(urn.getResourceTypeName());
      sb.append('/').append(urn.getNamespace());
      sb.append(urn.getPathInfo());

      sb.append("?urns=");
      sb.append(Joiners.by('|').join(js.getRef().getRefs(), new IBuilder<IJsRef>() {
         @Override
         public String asString(IJsRef ref) {
            return ref.getUrn().toString();
         }
      }));

      return sb.toString();
   }

   @Override
   public SimpleAggregatedJsUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Js.Aggregated;
   }

   @Override
   public Class<? super SimpleAggregatedJsUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }

   @ContextPath
   public void setContextPath(String contextPath) {
      m_contextPath = contextPath;
   }
}
