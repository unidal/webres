package org.unidal.webres.resource.expression;

public class ZombieExpression extends BaseResourceExpression<ZombieExpression, Object> {
   public ZombieExpression(String key) {
      super(null, null, key);
   }

   public ZombieExpression(ZombieExpression parent, String key) {
      super(null, parent, key);
   }

   @Override
   protected ZombieExpression createChild(String key) {
      return new ZombieExpression(this, key);
   }

   @Override
   public Object evaluate() {
      return toString();
   }

   @Override
   protected String getDefaultProperty() {
      // no default property
      return null;
   }

   @Override
   protected void prepareUrn(Urn urn) {
      urn.addSection(getKey());
   }

   @Override
   public String toString() {
      Urn urn = (Urn) buildUrn();
      StringBuilder sb = new StringBuilder();

      sb.append("${");

      for (String section : urn.getSections()) {
         sb.append(section).append('.');
      }

      sb.setCharAt(sb.length() - 1, '}');

      return sb.toString();
   }
}