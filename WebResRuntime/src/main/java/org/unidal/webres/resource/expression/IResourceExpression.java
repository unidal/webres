package org.unidal.webres.resource.expression;

import java.util.Map;
import java.util.Set;

/**
 * A resource expression is a basic unit of EL evaluation for V4 resource. 
 * 
 * <p>
 * <ul>For example, ${spec.Images.half.buttons.btn_Buy_37x19_gif} will be represented 
 * by a chain of <code>IResourceExpression</code>:
 * <li>SpecExpression for 'spec'</li>
 * <li>ImagesExpression for 'Images'</li>
 * <li>ImageExpression for 'half'</li>
 * <li>ImageExpression for 'button'</li>
 * <li>ImageExpression for 'btn_Buy_37x19_gif'</li>
 *  </ul>
 *  </p>
 *
 * <p>By this way, resource expression will enable partial of expressions to 
 * be shortcut and store to a variable, and then reference the actual resource
 * starting from the variable.
 * 
 * <p>i.e. ${buttons} := ${spec.Images.half.buttons} and use ${buttons.btn_Buy_37x19_gif} 
 * to reach the actual image.
 * 
 * <p>Also, it enables underlying infrastructure to do delegation and provide a cache 
 * mechanism. In production, there could be a cache manager for all resource under spec
 * since they are static and determinable at build time.
 * 
 */
public interface IResourceExpression<T, S> extends Map<String, T> {
   public char PROPERTY_PREFIX = '$';

   /**
    * Support EL method invocation when in UEL (only for JSP 2.2 and above).<p>
    * 
    * For example, <br>
    *   ${res.content.global.srpProject.srp.pageTitle('mybundle')}<br>
    */
   public T call(Object key, Object[] arguments);

   /**
    * used by &lt;c:forEach ... /&gt; tag.<p>
    */
   @Override
   public Set<Map.Entry<String, T>> entrySet();

   /**
    * Normal EL dot(.) operation.<p>
    * 
    * For example,<br>
    *   ${res.content.global.srpProject.srp.page.title}<br>
    *   or ${res.content.global.srpProject.srp.page['title']}<br>
    */
   @Override
   public T get(Object key);

   public S evaluate();

   /**
    * @return external form of the expression
    */
   public String toExternalForm();
}
