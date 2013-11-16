package org.unidal.webres.server.css;

import org.unidal.helper.Joiners;
import org.unidal.helper.Joiners.IBuilder;
import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.annotation.ContextPath;
import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.css.AggregatedCss;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class SimpleAggregatedCssUrlBuilder implements IResourceUrlBuilder<AggregatedCss>,
      IResourceRegisterable<SimpleAggregatedCssUrlBuilder> {
   private String m_contextPath;

   private String m_servletPath;

   public SimpleAggregatedCssUrlBuilder(String servletPath) {
      m_servletPath = servletPath;

      if (servletPath != null) {
         if (!servletPath.startsWith("/") && servletPath.endsWith("/")) {
            throw new RuntimeException("servletPath should be null or starting with '/' but not ending with '/'.");
         }
      }
   }

   @Override
   public String build(IResourceContext ctx, AggregatedCss css) {
      StringBuilder sb = new StringBuilder(128);

      if (m_contextPath != null) {
         sb.append(m_contextPath);
      }

      if (m_servletPath != null) {
         sb.append(m_servletPath);
      }

      IResourceUrn urn = css.getMeta().getUrn();

      sb.append('/').append(urn.getResourceTypeName());
      sb.append('/').append(urn.getNamespace());
      sb.append(urn.getPathInfo());

      sb.append("?urns=");
      sb.append(Joiners.by('|').join(css.getRef().getRefs(), new IBuilder<ICssRef>() {
         @Override
         public String asString(ICssRef ref) {
            return ref.getUrn().toString();
         }
      }));

      return sb.toString();
   }

   @Override
   public SimpleAggregatedCssUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Css.Aggregated;
   }

   @Override
   public Class<? super SimpleAggregatedCssUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }

   @ContextPath
   public void setContextPath(String contextPath) {
      m_contextPath = contextPath;
   }
}
