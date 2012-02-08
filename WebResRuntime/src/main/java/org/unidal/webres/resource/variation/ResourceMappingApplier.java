package org.unidal.webres.resource.variation;

import java.util.HashMap;
import java.util.Map;

import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceMappingApplier;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.variation.entity.ResourceMapping;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.entity.Rule;

public class ResourceMappingApplier implements IResourceMappingApplier, IResourceRegisterable<ResourceMappingApplier> {
   @Override
   public IResourceUrn apply(IResourceContext ctx, IResourceUrn urn) {
      ResourceVariation variation = ResourceRuntimeContext.ctx().getResourceVariation();

      if (variation != null) {
         String key = urn.toString();
         ResourceMapping mapping = variation == null ? null : variation.findResourceMapping(key);

         // fall-back
         if (mapping == null && variation != null) {
            Map<String, ResourceMapping> mappings = variation.getResourceMappings();

            for (ResourceMapping e : mappings.values()) {
               if (isCompatible(e, key)) {
                  mapping = e;
                  break;
               }
            }
         }

         if (mapping != null) {
            VariationValueProvider provider = new VariationValueProvider(ctx);

            for (Rule rule : mapping.getRules()) {
               if (applyRule(urn, rule, provider)) {
                  return rule.getUrn();
               }
            }
         }
      }

      return urn;
   }

   protected boolean applyRule(IResourceUrn urn, Rule rule, VariationValueProvider provider) {
      Map<String, String> attributes = rule.getDynamicAttributes();

      if (attributes.size() > 0) {
         for (Map.Entry<String, String> e : attributes.entrySet()) {
            String name = e.getKey();
            String actualValue = provider.getVariationValue(name);

            if (actualValue == null || !actualValue.equals(e.getValue())) {
               return false;
            }
         }
      }

      return true;
   }

   protected String getFallbackPath(String path) {
      int pos1 = path.lastIndexOf('/');
      int pos2 = path.lastIndexOf('_');

      if (pos2 > pos1) {
         return path.substring(0, pos2) + '.' + path.substring(pos2 + 1);
      } else {
         return null;
      }
   }

   @Override
   public ResourceMappingApplier getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return null;
   }

   @Override
   public Class<? super ResourceMappingApplier> getRegisterType() {
      return IResourceMappingApplier.class;
   }

   protected boolean isCompatible(ResourceMapping mapping, String actual) {
      String fallback = mapping.getFallbackUrn();

      if (fallback == null) {
         fallback = getFallbackPath(mapping.getUrn());

         mapping.setFallbackUrn(fallback);
      }

      return fallback != null && fallback.equals(actual);
   }

   /**
    * To avoid accessing IResourceContext multiple times for same keys
    */
   static class VariationValueProvider {
      Map<String, String> m_cache = new HashMap<String, String>();

      private IResourceContext m_ctx;

      public VariationValueProvider(IResourceContext ctx) {
         m_ctx = ctx;
      }

      public String getVariationValue(String name) {
         String value = null;

         if (!m_cache.containsKey(name)) {
            Object val = m_ctx.getVariation(name, null);

            value = (val == null ? null : val.toString());
         } else {
            value = m_cache.get(name);
         }

         return value;
      }
   }
}
