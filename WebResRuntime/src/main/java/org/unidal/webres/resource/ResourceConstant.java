package org.unidal.webres.resource;

public interface ResourceConstant {
   public String AppId = "appId";

   public String AppVersion = "appVersion";

   public interface Css {
      public String Base = "css.base";

      public String Local = "css.local";

      public String Shared = "css.shared";

      public String War = "css.war";

      public String Inline = "css.inline";

      public String Aggregated = "css.aggregated";

      public String AggregatedValidation = "css.aggregated.validation";

      public String AggregatedVerbose = "css.aggregated.verbose";

      public String SharedUrlPrefix = "css.shared.urlPrefix";

      public String SharedSecureUrlPrefix = "css.shared.secureUrlPrefix";
   }

   public interface Image {
      public String Base = "img.base";

      public String DataUriBuilder = "img.dataUriBuilder";

      public String Validation = "img.validation";

      public String Local = "img.local";

      public String Pics = "img.pics";

      public String PicsUrlPrefix = "img.pics.urlPrefix";

      public String PicsSecureUrlPrefix = "img.pics.secureUrlPrefix";

      public String Shared = "img.shared";

      public String SharedUrlPrefix = "img.shared.urlPrefix";

      public String SharedSecureUrlPrefix = "img.shared.secureUrlPrefix";

      public String War = "img.war";
   }

   public interface Js {
      public String Base = "js.base";

      public String Local = "js.local";

      public String Shared = "js.shared";

      public String War = "js.war";

      public String Inline = "js.inline";

      public String Aggregated = "js.aggregated";

      public String AggregatedValidation = "js.aggregated.validation";

      public String AggregatedVerbose = "js.aggregated.verbose";

      public String SharedUrlPrefix = "js.shared.urlPrefix";

      public String SharedSecureUrlPrefix = "js.shared.secureUrlPrefix";
   }

   public interface Link {
      public String Pages = "link.pages";

      public String PagesUrlPrefix = "link.pages.urlPrefix";

      public String PagesSecureUrlPrefix = "link.pages.secureUrlPrefix";

      public String Cmd = "link.cmd";
   }

   public interface Template {
      public String Local = "template.local";

      public String Inline = "template.inline";

      public String Shared = "template.shared";

      public String War = "template.war";

      public String Java = "template.java";
   }
}
