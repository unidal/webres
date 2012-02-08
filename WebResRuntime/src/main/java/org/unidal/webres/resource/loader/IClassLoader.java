package org.unidal.webres.resource.loader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public interface IClassLoader {
   /**
    * Invoked by the Virtual Machine when resolving class references.
    * Equivalent to loadClass(className, false);
    *
    * @return     java.lang.Class
    *             the Class object.
    * @param      className String
    *             the name of the class to search for.
    * @exception  ClassNotFoundException
    *             If the class could not be found.
    */
   public Class<?> loadClass (String className) throws ClassNotFoundException;
   
   
   /**
    * Answers an URL which can be used to access the resource
    * described by resName, using the class loader's resource lookup
    * algorithm. The default behavior is just to return null.
    *
    * @return     URL
    *             the location of the resource.
    * @param      resName String
    *             the name of the resource to find.
    *
    * @see        Class#getResource
    */
   public URL getResource (String resName);
   
   
   /**
    * Answers an Enumeration of URL which can be used to access the resources
    * described by resName, using the class loader's resource lookup
    * algorithm.
    *
    * @param      resName String
    *             the name of the resource to find.

    * @return     Enumeration
    *             the locations of the resources.
    *
    * @throws IOException when an error occurs
    */
   public Enumeration<URL> getResources (String resName) throws IOException;
}
