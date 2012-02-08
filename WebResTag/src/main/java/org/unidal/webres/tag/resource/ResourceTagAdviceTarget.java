package org.unidal.webres.tag.resource;

import org.unidal.webres.tag.ITagState;

public enum ResourceTagAdviceTarget implements ITagState<ResourceTagAdviceTarget> {
   START(0, 1, 3),

   BUILD(1, 2),

   RENDER(2, 3, 9),

   END(3, 9),

   DEFER(9, 0);

   private int m_id;

   private int[] m_nextStateIds;

   private ResourceTagAdviceTarget(int id, int... nextStates) {
      m_id = id;
      m_nextStateIds = nextStates;
   }

   public boolean canTransit(ResourceTagAdviceTarget nextState) {
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

}
