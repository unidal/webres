package org.unidal.webres.tag.core;

public interface ITagAdvice {
   public <T, M extends ITagModel> void onBegin(ITag<T, M> tag, TagAdviceTarget target);

   public <T, M extends ITagModel> void onEnd(ITag<T, M> tag, TagAdviceTarget target);

   public <T, M extends ITagModel> boolean onError(ITag<T, M> tag, TagAdviceTarget target, Throwable cause);
}
