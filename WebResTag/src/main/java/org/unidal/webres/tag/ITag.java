package org.unidal.webres.tag;


public interface ITag<T, M extends ITagModel, S extends ITagState<S>> {
   /**
    * Build a component instance for this tag. The component could be an instance of resource, 
    * for example, ICssResource, IJsResource etc. It could be a DSF component, for example, 
    * DContent or DDiv etc.
    * 
    * Return null if there is no component built, that leads to render() method will be skipped.
    * 
    * @return an instance of component. Return null means no component is built.
    */
   public T build();

   /**
    * Be called when a tag ends.
    */
   public void end();

   public ITagEnv getEnv();

   /**
    * Return a data model of the tag instance.
    * 
    * @return data model of the tag instance.
    */
   public M getModel();

   public S getState();

   public String render(T component);

   /**
    * Inject an environment instance for this tag to live on.
    * 
    * @param env
    */
   public void setEnv(ITagEnv env);

   public void setState(S newState);

   /**
    * Be called when a tag starts.
    */
   public void start();
}
