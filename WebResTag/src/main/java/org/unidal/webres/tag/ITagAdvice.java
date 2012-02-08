package org.unidal.webres.tag;

public interface ITagAdvice<S extends ITagState<S>, T extends ITag<?, ?, S>> {
   public void onBegin(T tag, S state);

   public void onEnd(T tag, S state);

   public boolean onError(T tag, S state, Throwable cause);
}
