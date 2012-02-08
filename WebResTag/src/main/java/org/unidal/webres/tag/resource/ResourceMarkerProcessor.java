package org.unidal.webres.tag.resource;

import org.unidal.webres.helper.Markers;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceContainer;
import org.unidal.webres.resource.spi.IResourceDeferRenderer;

public class ResourceMarkerProcessor implements IResourceMarkerProcessor {
   private MarkerParser m_parser = new MarkerParser();

   private MarkerTranslator m_translator = new MarkerTranslator();

   @Override
   public void process(StringBuilder content) {
      ResourceRuntimeContext.ctx().applyProfile();

      m_parser.parse(content, m_translator);
   }

   protected static class MarkerParser {
      public void parse(StringBuilder content, MarkerTranslator translator) {
         final StringBuilder marker = new StringBuilder(64);
         final int len = content.length();
         boolean dollar = false;
         boolean bracket = false;
         int count = 0;
         int pos = 0;

         while (count < len) {
            final char ch = content.charAt(pos);

            switch (ch) {
            case '$':
               if (bracket) {
                  marker.setLength(0);
                  bracket = false;
               }

               dollar = true;
               break;
            case '{':
               if (bracket) {
                  marker.append(ch);
               } else {
                  if (dollar) {
                     bracket = true;
                     dollar = false;
                  }
               }
               break;
            case '}':
               if (bracket) {
                  int length = marker.length();
                  String translated = translator.translate(marker.toString());

                  if (translated != null) {
                     content.replace(pos - length - 2, pos + 1, translated);
                     pos += translated.length() - length - 3;
                  }

                  marker.setLength(0);
                  bracket = false;
               } else {
                  dollar = false;
               }
               break;
            default:
               if (bracket) {
                  marker.append(ch);
               } else {
                  dollar = false;
               }

               break;
            }

            pos++;
            count++;
         }
      }
   }

   protected static class MarkerTranslator {
      protected String translate(String marker) {
         String[] sections = Markers.forDefer().parse(marker);

         if (sections.length > 0) {
            IResourceContainer container = ResourceRuntimeContext.ctx().getContainer();
            IResourceDeferRenderer renderer = container.getAttribute(IResourceDeferRenderer.class, sections[0]);

            if (renderer != null) {
               return renderer.deferRender();
            }
         } else {
            System.out.println("invalid marker: " + marker);
         }

         return null;
      }
   }
}