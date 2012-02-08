package org.unidal.webres.resource.css;

import java.util.List;

import org.unidal.webres.resource.ResourceUrn;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.ICssRef;

public class AggregatedCssRef extends CssRef {
   private List<ICssRef> m_refs;

   public AggregatedCssRef(List<ICssRef> refs) {
      this(buildMockResourceId(refs), refs);
   }

   public AggregatedCssRef(String resourceId, List<ICssRef> refs) {
      super(new ResourceUrn(SystemResourceType.Css.getName(), CssNamespace.AGGREGATED.getName(), resourceId));

      m_refs = refs;
   }

   private static String buildMockResourceId(List<ICssRef> refs) {
      return String.valueOf(refs.hashCode());
   }

   public List<ICssRef> getRefs() {
      return m_refs;
   }
}
