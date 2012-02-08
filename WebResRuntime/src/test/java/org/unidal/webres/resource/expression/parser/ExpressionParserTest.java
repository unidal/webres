package org.unidal.webres.resource.expression.parser;

import junit.framework.Assert;

import org.junit.Test;

import org.unidal.webres.resource.expression.parser.IExpressionParser.IExpressionNode;

public class ExpressionParserTest {
   protected void check(String expression) {
      check(expression, expression);
   }

   protected void check(String expression, String expected) {
      IExpressionNode expr = new ExpressionParser().parse(expression);

      Assert.assertEquals(expected, "${" + expr.toString() + "}");
   }

   @Test
   public void testAll() {
      check("${x}");
      check("${x.$y}");
      check("${x.y}");
      check("${x['y']}");
      check("${x[true]}");
      check("${x[false]}");
      check("${x[null]}");
      check("${x[123]}");
      check("${x[123e2]}");
      check("${x[y]}");
      check("${x[y.z]}");
      check("${x.y()}");
      check("${x.y(a)}");
      check("${x.y(a,'b')}");
      check("${x.y(a,'b',c.d)}");
      check("${x.y(a,'b',c.d,e.f())}");
      check("${x.y(a,'b',c.d,e.f(g(h())))}");
      check("${x.y().z}");
      check("${x.y()[z]}");
      check("${x:y()}");
      check("${x:y(a)}");
      check("${x:y(a,b:c())}");
      check("${x:y(a,b:c().d())}");
      check("${x:y(a,b:c()[e()])}");
      check("${x:y(a,b:c().d()[e()][f:g()])}");
      check("${a.b[c:d(e,'f',g.h)].i(j,k.l())['m'].n.o()}");
      check("${f:a.b[c:d(e,'f',g.h)].i(j,k.l())['m'].n.o()}");
   }

   @Test
   public void testArray() {
      check("${profile.users[1].name}");
   }

   @Test
   public void testFunction() {
      check("${x:y()}");
      check("${x:y(a)}");
      check("${x:y(a,b:c())}");
      check("${x:y(a,b:c().d())}");
      check("${x:y(a,b:c()[e()])}");
      check("${x:y(a,b:c().d()[e()][f:g()])}");
      check("${a.b[c:d(e,'f',g.h)].i(j,k.l())['m'].n.o()}");
      check("${f:a.b[c:d(e,'f',g.h)].i(j,k.l())['m'].n.o()}");
   }

   @Test
   public void testMethod() {
      check("${x.y()}");
      check("${x.y(a)}");
      check("${x.y(a,'b')}");
      check("${x.y(a,'b',c.d)}");
      check("${x.y(a,'b',c.d,e.f())}");
      check("${x.y(a,'b',c.d,e.f(g(h())))}");
      check("${x.y().z}");
      check("${x.y()[z]}");
      check("${x:y()}");
      check("${x:y(a)}");
      check("${x:y(a,b:c())}");
      check("${x:y(a,b:c().d())}");
      check("${x:y(a,b:c()[e()])}");
      check("${x:y(a,b:c().d()[e()][f:g()])}");
      check("${a.b[c:d(e,'f',g.h)].i(j,k.l())['m'].n.o()}");
      check("${f:a.b[c:d(e,'f',g.h)].i(j,k.l())['m'].n.o()}");
   }

   @Test
   public void testResource() {
      check("${res.img.local.a_gif}");
      check("${res['img']['local'].a_gif}");
      check("${res.css.shared.css.breadcrumb.breadcrumb_css}");
      check("${res.link.cmd.signin['&user'][user]['&pwd'][pwd]['%rtm']}");
      check("${res.css.shared.css[page.breadcrumb].breadcrumb_css}");
      check("${res.css.shared.css[breadcrumb].breadcrumb_css}");
   }

   @Test
   public void testSimple() {
      check("${x}");
      check("${x.$y}");
      check("${x.y}");
      check("${x['y']}");
      check("${x[true]}");
      check("${x[false]}");
      check("${x[null]}");
      check("${x[123]}");
      check("${x[123e2]}");
      check("${x[y]}");
      check("${x[y.z]}");
   }

   @Test
   public void testTrim() {
      check("${res.css.shared.css.breadcrumb.breadcrumb_css  }", "${res.css.shared.css.breadcrumb.breadcrumb_css}");
      check("${res.css.shared.css.breadcrumb.breadcrumb_css\r\n}", "${res.css.shared.css.breadcrumb.breadcrumb_css}");
      check("${  res.css.shared.css.breadcrumb.breadcrumb_css}", "${res.css.shared.css.breadcrumb.breadcrumb_css}");
      check("${\r\nres.css.shared.css.breadcrumb.breadcrumb_css}", "${res.css.shared.css.breadcrumb.breadcrumb_css}");
      check("${  res.css.shared.css.breadcrumb.breadcrumb_css  }", "${res.css.shared.css.breadcrumb.breadcrumb_css}");
   }
}
