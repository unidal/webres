package org.unidal.webres.tag.resource;

import org.unidal.webres.resource.api.IResource;
import org.unidal.webres.resource.api.IResourceRef;
import org.unidal.webres.resource.expression.IResourceExpression;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.tag.ITagLookupManager;
import org.unidal.webres.tag.TagSupport;

public abstract class ResourceTagSupport<M extends ResourceTagModelSupport<? extends IResourceTagRenderType>, Ref extends IResourceRef<R>, R extends IResource<?, ?>>
      extends TagSupport<Ref, M, ResourceTagState> {
   public ResourceTagSupport(M model) {
      super(model, ResourceTagState.CREATED);
   }

   protected String getRenderType(R resource) {
      ResourceTagModelSupport<?> model = (ResourceTagModelSupport<?>) getModel();
      String renderType = model.getRenderType();

      if (renderType == null) {
         renderType = model.getDefaultRenderType().getName();
      }

      return renderType;
   }

   // TODO revise the error message later
   @SuppressWarnings("unchecked")
   protected <S> S getResourceRef(Class<S> expectedClass, Object value) {
      S ref = null;

      if (value == null) {
         String message = String.format("<!-- The attribute(value) of %s is null. -->", getClass().getSimpleName());

         getEnv().err(message);
      } else if (value instanceof IResourceExpression) {
         IResourceExpression<?, Object> expr = (IResourceExpression<?, Object>) value;
         Object exprValue = expr.evaluate();

         if (exprValue == null) {
            String message = String.format("<!-- No resource found for EL(%s) in %s. -->", expr.toExternalForm(),
                  getClass().getSimpleName());

            getEnv().err(message);
         } else if (expectedClass.isAssignableFrom(exprValue.getClass())) {
            ref = (S) exprValue;
         } else {
            String message = String.format("<!-- Invalid value of EL(%s) within %s, Expected %s, but was %s. -->", expr
                  .toExternalForm(), getClass().getSimpleName(), expectedClass.getName(), exprValue.getClass()
                  .getName());

            getEnv().err(message);
         }
      } else if (expectedClass.isAssignableFrom(value.getClass())) {
         ref = (S) value;
      } else {
         String message = String.format("<!-- Unsupported value(%s) of %s. -->", value, getClass().getSimpleName());

         getEnv().err(message);
      }

      return ref;
   }

   @Override
   @SuppressWarnings("unchecked")
   public String render(Ref ref) {
      if (ref != null) {
         ITagLookupManager manager = getEnv().getLookupManager();
         IResourceContext ctx = manager.lookupComponent(IResourceContext.class);

         if (ctx == null) {
            throw new RuntimeException("No IResourceContext was registered!");
         }

         try {
            R resource = ref.resolve(ctx);
            String renderType = getRenderType(resource);
            String key = getClass().getName() + ":" + renderType;
            String content = manager.lookupComponent(IResourceTagRenderer.class, key).render(this, resource);

            return content;
         } catch (RuntimeException e) {
            getEnv().onError(this, getState(), e);
         }
      }

      return null;
   }
}
