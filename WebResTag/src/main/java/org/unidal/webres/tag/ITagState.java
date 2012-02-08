package org.unidal.webres.tag;

public interface ITagState<S extends ITagState<S>> {
   public int getId();

   public boolean canTransit(S nextState);
}
