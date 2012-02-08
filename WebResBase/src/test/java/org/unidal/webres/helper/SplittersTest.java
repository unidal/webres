package org.unidal.webres.helper;

import org.junit.Assert;
import org.junit.Test;

public class SplittersTest {
   @Test
   public void test() {
      Assert.assertEquals("[, a,  b , c, d, ]", Splitters.by(',').split(",a, b ,c,d,").toString());
      Assert.assertEquals("[a, b , c, d]", Splitters.by(',').noEmptyItem().split(",a,b ,c,d,").toString());
      Assert.assertEquals("[, a, b, c, d, ]", Splitters.by(',').trim().split(",a,b ,c,d,").toString());
      Assert.assertEquals("[a, b, c, d]", Splitters.by(',').noEmptyItem().trim().split(",a,b ,c,d,").toString());
   }
}
