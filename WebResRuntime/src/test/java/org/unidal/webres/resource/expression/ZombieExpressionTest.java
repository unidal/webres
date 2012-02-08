package org.unidal.webres.resource.expression;

import junit.framework.Assert;

import org.junit.Test;

public class ZombieExpressionTest {
   @Test
   public void test() {
      ZombieExpression root = new ZombieExpression("this");

      Assert.assertEquals("${this}", root.toString());
      Assert.assertEquals("${this.content}", root.get("content").toString());
      Assert.assertEquals("${this.meta.length}", root.get("meta").get("length").toString());
   }
}
