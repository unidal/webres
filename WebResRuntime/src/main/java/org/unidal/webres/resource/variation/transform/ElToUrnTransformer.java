package org.unidal.webres.resource.variation.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.unidal.webres.resource.ResourceExpressionEnv;
import org.unidal.webres.resource.api.IResourceRef;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.expression.IResourceExpression;
import org.unidal.webres.resource.expression.IResourceExpressionEnv;
import org.unidal.webres.resource.expression.ResourceExpressionParser;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.variation.entity.ResourceMapping;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.entity.Rule;

public class ElToUrnTransformer extends BaseVisitor {
   private ResourceExpressionParser m_parser;

   public ElToUrnTransformer(IResourceContext ctx) {
      IResourceExpressionEnv env = new ResourceExpressionEnv(ctx);

      m_parser = new ResourceExpressionParser(env);
   }

   protected IResourceUrn toUrn(String el) {
      try {
         IResourceExpression<?, ?> expr = m_parser.parse(el);
         IResourceRef<?> ref = (IResourceRef<?>) expr.evaluate();

         return ref.getUrn();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return null;
   }

   @Override
   public void visitResourceVariation(ResourceVariation resourceVariation) {
      super.visitResourceVariation(resourceVariation);

      // replace key ${res.img.local.jquery_js} with 'img.local:/jquery_js'
      Map<String, ResourceMapping> resourceMappings = resourceVariation.getResourceMappings();
      List<String> keys = new ArrayList<String>(resourceMappings.keySet());

      for (String key : keys) {
         ResourceMapping resourceMapping = resourceMappings.get(key);

         resourceMappings.remove(key);
         resourceMappings.put(resourceMapping.getUrn(), resourceMapping);
      }
   }

   @Override
   public void visitResourceMapping(ResourceMapping resourceMapping) {
      String el = resourceMapping.getUrn();

      if (el != null && el.startsWith("${") && el.endsWith("}")) {
         IResourceUrn urn = toUrn(el);

         if (urn != null) {
            resourceMapping.setUrn(urn.toString());
         }
      }

      super.visitResourceMapping(resourceMapping);
   }

   @Override
   public void visitRule(Rule rule) {
      String text = rule.getText();

      if (text != null && text.startsWith("${") && text.endsWith("}")) {
         IResourceUrn urn = toUrn(text);

         if (urn != null) {
            rule.setUrn(urn);
            rule.setText(urn.toString());
         }
      }

      super.visitRule(rule);
   }
}
