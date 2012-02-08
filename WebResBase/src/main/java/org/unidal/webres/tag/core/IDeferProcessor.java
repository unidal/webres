package org.unidal.webres.tag.core;

import java.io.IOException;
import java.io.Writer;

public interface IDeferProcessor {
   public void process(StringBuilder sb) throws IOException;

   public IMarkerHandler getMarkerHandker();

   public static interface IMarkerHandler {
      public boolean handle(Writer writer, String marker) throws IOException;
   }
}