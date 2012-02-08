package org.unidal.webres.tag.resource;

import org.unidal.webres.tag.ITagState;

public enum ResourceTagState implements ITagState<ResourceTagState> {
   CREATED(0, 1),

   STARTED(1, 2),

   BUILT(2, 3, 9),

   RENDERED(3, 9),

   ENDED(9, 1);

   private int m_id;

   private int[] m_nextStateIds;

   private ResourceTagState(int id, int... nextStates) {
      m_id = id;
      m_nextStateIds = nextStates;
   }

   public boolean canTransit(ResourceTagState nextState) {
      for (int id : m_nextStateIds) {
         if (id == nextState.getId()) {
            return true;
         }
      }

      return false;
   }

   public int getId() {
      return m_id;
   }

   public boolean isBuilt() {
      return this == BUILT;
   }

   public boolean isCreated() {
      return this == CREATED;
   }

   public boolean isEnded() {
      return this == ENDED;
   }

   public boolean isRendered() {
      return this == RENDERED;
   }

   public boolean isStarted() {
      return this == STARTED;
   }
}