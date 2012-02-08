package org.unidal.webres.resource.js;

import java.util.List;

import org.unidal.webres.resource.ResourceUrn;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IJsRef;

public class AggregatedJsRef extends JsRef {
   private List<IJsRef> m_refs;

   public AggregatedJsRef(List<IJsRef> refs) {
      this(buildMockResourceId(refs), refs);
   }

   public AggregatedJsRef(String resourceId, List<IJsRef> refs) {
      super(new ResourceUrn(SystemResourceType.Js.getName(), JsNamespace.AGGREGATED.getName(), resourceId));

      m_refs = refs;
   }

   private static String buildMockResourceId(List<IJsRef> refs) {
      return String.valueOf(refs.hashCode());
   }

   public List<IJsRef> getRefs() {
      return m_refs;
   }
}
