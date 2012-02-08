package org.unidal.webres.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ProxiesTest {

   @Test
   public void testPrimitiveModel() {
      IModel model = Proxies.forObject().newInstance("Duke", new Proxies.PrimitiveAccessor(), IModel.class);
      Assert.assertEquals("Duke", model.getName());
   }

   @Test
   public void testIModelWithArg() {
      IModelWithArg modelWithArg = Proxies.forObject().newInstance(new ModelWithArg(), IModelWithArg.class);
      Assert.assertEquals("int", modelWithArg.getName(0));
      Assert.assertEquals("String", modelWithArg.getName("Duke"));
   }

   @Test
   public void testIModelWithInheritArg() {
      IModelWithInheritArg modelWithInheritArg = Proxies.forObject().newInstance(new ModelWithInheritArg(),
            IModelWithInheritArg.class);
      Assert.assertEquals("Duke", modelWithInheritArg.getName(new Model()));
   }

   @Test
   public void testIModel() {
      // test with Model implements IModel
      IModel modelFromIModel = Proxies.forObject().newInstance(new Model(), IModel.class);
      Assert.assertEquals("Duke", modelFromIModel.getName());

      IModel2 modelFromIModel2 = Proxies.forObject().newInstance(new Model2(), IModel2.class);
      Assert.assertEquals("Duke", modelFromIModel2.getName());
      Assert.assertEquals(20, modelFromIModel2.getAge());

      IModel3 modelFromIModel3 = Proxies.forObject().newInstance(new Model3(), IModel3.class);
      Assert.assertEquals("Duke", modelFromIModel3.getName());
      Assert.assertEquals(20, modelFromIModel3.getAge());
      Assert.assertEquals(false, modelFromIModel3.getMarried());
   }

   @Test
   public void testPojoModel() {
      // test with Pojo Model
      IModel modelFromPojoModel = Proxies.forObject().newInstance(new PojoModel(), IModel.class);
      Assert.assertEquals("Pojo Duke", modelFromPojoModel.getName());

      IModel2 modelFromPojoModel2 = Proxies.forObject().newInstance(new PojoModel2(), IModel2.class);
      Assert.assertEquals("Pojo Duke", modelFromPojoModel2.getName());
      Assert.assertEquals(30, modelFromPojoModel2.getAge());

      IModel3 modelFromIPojoModel3 = Proxies.forObject().newInstance(new PojoModel3(), IModel3.class);
      Assert.assertEquals("Pojo Duke", modelFromIPojoModel3.getName());
      Assert.assertEquals(30, modelFromIPojoModel3.getAge());
      Assert.assertEquals(true, modelFromIPojoModel3.getMarried());
   }

   @Test
   public void testPojoModelWithConverted() {
      IModel2 modelFromPojoModel2Converted = Proxies.forObject().newInstance(new PojoModel2Converted(), IModel2.class);
      Assert.assertEquals("Pojo Duke", modelFromPojoModel2Converted.getName());
      Assert.assertEquals(30, modelFromPojoModel2Converted.getAge());
   }

   @Test
   public void testPojoModelWithMapping() {
      // test with User Pojo Model
      Map<String, String> userMap = new HashMap<String, String>();
      userMap.put("userName", "name");
      userMap.put("userAge", "age");
      userMap.put("userMarried", "married");

      UserPojoModel userPojoModel = new UserPojoModel();
      IModel modelFromUserPojoModel = Proxies.forObject().newInstance(userPojoModel,
            new Proxies.RouterAccessor(userPojoModel, userMap), IModel.class);
      Assert.assertEquals("User Pojo Duke", modelFromUserPojoModel.getName());

      UserPojoModel2 userPojoModel2 = new UserPojoModel2();
      IModel2 modelFromUserPojoModel2 = Proxies.forObject().newInstance(userPojoModel2,
            new Proxies.RouterAccessor(userPojoModel2, userMap), IModel2.class);
      Assert.assertEquals("User Pojo Duke", modelFromUserPojoModel2.getName());
      Assert.assertEquals(40, modelFromUserPojoModel2.getAge());

      UserPojoModel3 userPojoModel3 = new UserPojoModel3();
      IModel3 modelFromUserPojoModel3 = Proxies.forObject().newInstance(userPojoModel3,
            new Proxies.RouterAccessor(userPojoModel3, userMap), IModel3.class);
      Assert.assertEquals("User Pojo Duke", modelFromUserPojoModel3.getName());
      Assert.assertEquals(40, modelFromUserPojoModel3.getAge());
      Assert.assertEquals(true, modelFromUserPojoModel3.getMarried());
   }

   @Test
   public void testListModelWithMapping() {
      List<Object> oneParam = new ArrayList<Object>();
      List<Object> twoParams = new ArrayList<Object>();
      List<Object> threeParams = new ArrayList<Object>();
      oneParam.add("Duke");

      twoParams.add("Duke2");
      twoParams.add(50);

      threeParams.add("Duke2");
      threeParams.add(50);
      threeParams.add(true);

      Map<String, String> userMap = new HashMap<String, String>();
      userMap.put("1", "name");
      userMap.put("2", "age");
      userMap.put("3", "married");

      IModel listFromMapAndMapping = Proxies.forObject().newInstance(oneParam,
            new Proxies.RouterAccessor(oneParam, userMap), IModel.class);
      Assert.assertEquals("Duke", listFromMapAndMapping.getName());

      IModel2 listFromMapAndMapping2 = Proxies.forObject().newInstance(twoParams,
            new Proxies.RouterAccessor(twoParams, userMap), IModel2.class);
      Assert.assertEquals("Duke2", listFromMapAndMapping2.getName());
      Assert.assertEquals(50, listFromMapAndMapping2.getAge());

      IModel3 listFromMapAndMapping3 = Proxies.forObject().newInstance(threeParams,
            new Proxies.RouterAccessor(threeParams, userMap), IModel3.class);
      Assert.assertEquals("Duke2", listFromMapAndMapping3.getName());
      Assert.assertEquals(50, listFromMapAndMapping3.getAge());
      Assert.assertEquals(true, listFromMapAndMapping3.getMarried());
   }

   @Test
   public void testMapModel() {
      // test with Map Model
      Map<String, Object> map1 = createMap("name", "Duke");
      IModel modelFromMap = Proxies.forObject().newInstance(map1,
            new Proxies.RouterAccessor(map1, Collections.<String, String> emptyMap()), IModel.class);
      Assert.assertEquals("Duke", modelFromMap.getName());

      Map<String, Object> map2 = createMap("name", "Duke2", "age", 50);
      IModel2 modelFromMap2 = Proxies.forObject().newInstance(map2,
            new Proxies.RouterAccessor(map2, Collections.<String, String> emptyMap()), IModel2.class);
      Assert.assertEquals("Duke2", modelFromMap2.getName());
      Assert.assertEquals(50, modelFromMap2.getAge());

      Map<String, Object> map3 = createMap("name", "Duke2", "age", 50, "married", true);
      IModel3 modelFromMap3 = Proxies.forObject().newInstance(map3,
            new Proxies.RouterAccessor(map3, Collections.<String, String> emptyMap()), IModel3.class);
      Assert.assertEquals("Duke2", modelFromMap3.getName());
      Assert.assertEquals(50, modelFromMap3.getAge());
      Assert.assertEquals(true, modelFromMap3.getMarried());
   }

   @Test
   public void testMapModelWithMapping() {
      // test with Map Model with Mapping
      Map<String, String> userMap = new HashMap<String, String>();
      userMap.put("userName", "name");
      userMap.put("userAge", "age");
      userMap.put("userMarried", "married");

      Map<String, Object> map1 = createMap("userName", "Duke");
      IModel modelFromMapWithMapping = Proxies.forObject().newInstance(map1, new Proxies.RouterAccessor(map1, userMap),
            IModel.class);
      Assert.assertEquals("Duke", modelFromMapWithMapping.getName());

      Map<String, Object> map2 = createMap("userName", "Duke2", "userAge", 50);
      IModel2 modelFromMapWithMapping2 = Proxies.forObject().newInstance(map2,
            new Proxies.RouterAccessor(map2, userMap), IModel2.class);
      Assert.assertEquals("Duke2", modelFromMapWithMapping2.getName());
      Assert.assertEquals(50, modelFromMapWithMapping2.getAge());

      Map<String, Object> map3 = createMap("userName", "Duke3", "userAge", 50, "userMarried", true);
      IModel3 modelFromMapWithMapping3 = Proxies.forObject().newInstance(map3,
            new Proxies.RouterAccessor(map3, userMap), IModel3.class);
      Assert.assertEquals("Duke3", modelFromMapWithMapping3.getName());
      Assert.assertEquals(50, modelFromMapWithMapping3.getAge());
      Assert.assertEquals(true, modelFromMapWithMapping3.getMarried());
   }

   private Map<String, Object> createMap(Object... items) {
      Map<String, Object> map = new HashMap<String, Object>();
      Object[] itemArray = items.clone();
      int length = itemArray.length;
      int i = 0;
      while (i < length) {
         map.put((String) itemArray[i], itemArray[i + 1]);
         i = i + 2;
      }
      return map;
   }

   static class UserPojoModel3 {
      public String getUserName() {
         return "User Pojo Duke";
      }

      public int getUserAge() {
         return 40;
      }

      public boolean getUserMarried() {
         return true;
      }
   }

   static class UserPojoModel2 {
      public String getUserName() {
         return "User Pojo Duke";
      }

      public int getUserAge() {
         return 40;
      }
   }

   static class UserPojoModel {
      public String getUserName() {
         return "User Pojo Duke";
      }
   }

   static class PojoModel3 {
      public String getName() {
         return "Pojo Duke";
      }

      public int getAge() {
         return 30;
      }

      public boolean getMarried() {
         return true;
      }
   }

   static class PojoModel2Converted {
      public String getName() {
         return "Pojo Duke";
      }

      public Long getAge() {
         return 30L;
      }
   }

   static class PojoModel2 {
      public String getName() {
         return "Pojo Duke";
      }

      public int getAge() {
         return 30;
      }
   }

   static class PojoModel {
      public String getName() {
         return "Pojo Duke";
      }
   }

   static class Model3 implements IModel3 {
      @Override
      public int getAge() {
         return 20;
      }

      @Override
      public String getName() {
         return "Duke";
      }

      @Override
      public boolean getMarried() {
         return false;
      }
   }

   static class Model2 implements IModel2 {
      @Override
      public int getAge() {
         return 20;
      }

      @Override
      public String getName() {
         return "Duke";
      }
   }

   static class Model implements IModel {
      public String getName() {
         return "Duke";
      }
   }

   public static interface IModel {
      public String getName();
   }

   public static class ModelWithInheritArg {
      public String getName(Model arg) {
         return arg.getName();
      }
   }

   public static interface IModelWithInheritArg {
      public String getName(IModel arg);
   }

   public static interface IModelWithArg {

      public String getName(int arg);

      public String getName(String arg);
   }

   public class ModelWithArg {

      public String getName(int arg) {
         return "int";
      }

      public String getName(String arg) {
         return "String";
      }
   }

   public static interface IModel2 {
      public String getName();

      public int getAge();
   }

   public static interface IModel3 {
      public String getName();

      public int getAge();

      public boolean getMarried();
   }
}
