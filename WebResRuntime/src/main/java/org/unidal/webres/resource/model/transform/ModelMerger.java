package org.unidal.webres.resource.model.transform;

import java.util.Stack;

import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;

public class ModelMerger extends DefaultMerger {
   public ModelMerger(Root root) {
      super(root);
   }

   @Override
   protected void visitResourceChildren(Resource old, Resource resource) {
      Stack<Object> stack = getStack();
      Slot slot = (Slot) stack.peek();

      // only for new one or override one
      if (old == null || slot.isOverride()) {
         // slot re-assignment requirement
         // remove resource with given urn in other slots except current slot
         Page page = (Page) stack.get(stack.size() - 2);

         for (Slot s : page.getSlots()) {
            if (s != slot) { // KEEPME
               if (s.removeResource(resource.getUrn())) {
                  break;
               }
            }
         }
      }
   }

   @Override
   protected void visitSlotChildren(Slot old, Slot slot) {
      if (slot.isOverride()) {
         old.getResources().clear();
         getStack().push(old);
         
         for (Resource resource : slot.getResources()) {
            old.addResource(resource);
            visitResource(resource);
         }
         
         getStack().pop();
      } else {
         super.visitSlotChildren(old, slot);
      }
   }
}
