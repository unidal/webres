package org.unidal.webres.resource.runtime;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.unidal.helper.Files;
import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.ResourceContext;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.WarConstant;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.injection.ResourceContainer;
import org.unidal.webres.resource.loader.IClassLoader;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.profile.Profiles;
import org.unidal.webres.resource.profile.entity.Profile;
import org.unidal.webres.resource.spi.IResourceContainer;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegistry;
import org.unidal.webres.resource.variation.ResourceVariations;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.transform.ElToUrnTransformer;

public class ResourceRuntimeConfig {
   public static final String RESOURCE_PROFILE = "/WEB-INF/esf/resource_profile.xml";

   public static final String RESOURCE_PROPERTIES = "/WEB-INF/esf/resource.properties";

   public static final String RESOURCE_VARIATIONS = "/WEB-INF/esf/resource_variations.xml";

   public static final String RUNTIME_EXT_PROPERTIES = "/META-INF/esf/runtime_extension.properties";

   public static final String PROPERTY_RESOURCE_CONFIGURATORS = "resource.configurators";

   private IResourceRegistry m_registry;

   private IResourceContainer m_container;

   private IClassLoader m_appClassLoader;

   private File m_warRoot;

   private String m_contextPath;

   private String m_warName;

   private List<ResourcePermutation> m_permutations;

   private List<ResourceWarReference> m_referenceWars;

   private Map<IResourceType, Root> m_profiles = new HashMap<IResourceType, Root>();

   private IResourceContext m_resourceContext;

   private ResourceVariation m_resourceVariation;

   ResourceRuntimeConfig(String contextPath, File warRoot) {
      if (warRoot == null) {
      	System.out.println("No local resources will be supported due to no warRoot specified!");
      } else if (!warRoot.exists() || !warRoot.isDirectory()) {
         throw new RuntimeException(String.format("Invalid warRoot(%s), it must be an existing folder.", warRoot));
      }

      m_container = new ResourceContainer();
      m_registry = new ResourceRegistry(m_container);
      m_warRoot = warRoot;
      m_contextPath = contextPath;
      m_resourceContext = new ResourceContext(m_registry);

      new ResourceConfigurator().configure(m_registry);

      m_container.setAttribute(String.class, WarConstant.ContextPath, contextPath);
      m_container.setAttribute(File.class, WarConstant.WarRoot, warRoot);
   }

   public IClassLoader getAppClassLoader() {
      return m_appClassLoader;
   }

   public IResourceContainer getContainer() {
      return m_container;
   }

   public String getContextPath() {
      return m_contextPath;
   }

   public List<ResourcePermutation> getPermutations() {
      return m_permutations;
   }

   public Root getProfile(IResourceType type) {
      return m_profiles.get(type);
   }

   public List<ResourceWarReference> getReferenceWars() {
      return m_referenceWars;
   }

   public IResourceRegistry getRegistry() {
      return m_registry;
   }

   public IResourceContext getResourceContext() {
      return m_resourceContext;
   }

   public ResourceVariation getResourceVariation() {
      return m_resourceVariation;
   }

   public String getWarName() {
      return m_warName;
   }

   public File getWarRoot() {
      return m_warRoot;
   }

   void loadResourceProfile(String resourceProfileXml) {
      if (m_warRoot != null) {
         File file = new File(m_warRoot, resourceProfileXml);

         if (file.isFile()) {
            try {
               String content = Files.forIO().readFrom(file, "utf-8");
               Profile profile = Profiles.forXml().parse(content);
               SystemResourceType[] types = { SystemResourceType.Js, SystemResourceType.Css, SystemResourceType.Image };

               Profiles.forObject().validate(profile);

               for (SystemResourceType type : types) {
                  Root model = Profiles.forObject().buildModel(profile, type);

                  m_profiles.put(type, model);
               }
            } catch (Exception e) {
               throw new RuntimeException(String.format("Unable to load resource profile(%s)!", file), e);
            }
         }
      }
   }

   void loadResourceProperties(String resourceProperties) {
      ResourcePropertyLoader loader = new ResourcePropertyLoader();

      // set default value
      m_container.setAttribute(ResourceConstant.Image.Base, loader.getImageBase());
      m_container.setAttribute(ResourceConstant.Css.Base, loader.getCssBase());
      m_container.setAttribute(ResourceConstant.Js.Base, loader.getJsBase());

      if (m_warRoot == null) {
         // no war root, it means there is no local resources
         return;
      }

      File file = new File(m_warRoot, resourceProperties);

      if (file.isFile()) {
         try {

            loader.load(file);

            m_container.setAttribute(String.class, ResourceConstant.Image.Base, loader.getImageBase());
            m_container.setAttribute(String.class, ResourceConstant.Css.Base, loader.getCssBase());
            m_container.setAttribute(String.class, ResourceConstant.Js.Base, loader.getJsBase());
            m_container.setAttribute(String.class, ResourceConstant.AppId, loader.getAppId());
            m_container.setAttribute(String.class, ResourceConstant.AppVersion, loader.getAppVersion());

            m_warName = loader.getWarId();
            m_permutations = loader.getPermutations();
            m_referenceWars = loader.getReferences();
         } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to load resource properties(%s)!", file), e);
         }
      }
   }

   void loadResourceVariations(String resourceVariationsXml) {
      if (m_warRoot != null) {
         File file = new File(m_warRoot, resourceVariationsXml);

         if (file.isFile()) {
            try {
               String content = Files.forIO().readFrom(file, "utf-8");
               ResourceVariation resourceVariation = ResourceVariations.forXml().parse(content);

               ResourceVariations.forObject().validate(resourceVariation);
               resourceVariation.accept(new ElToUrnTransformer(m_resourceContext));

               m_resourceVariation = resourceVariation;
            } catch (Exception e) {
               throw new RuntimeException(String.format("Unable to load resource profile(%s)!", file), e);
            }
         }
      }
   }

   public void setAppClassLoader(IClassLoader appClassLoader) {
      m_appClassLoader = appClassLoader;
   }

   public void setProfile(IResourceType type, Root profile) {
      m_profiles.put(type, profile);
   }

   @Override
   public String toString() {
      return String.format("%s[contextPath=%s, warRoot=%s]", m_contextPath, m_warRoot);
   }
}