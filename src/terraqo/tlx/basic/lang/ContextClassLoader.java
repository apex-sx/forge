package pxf.toolkit.basic.lang;

import java.io.InputStream;
import java.net.URL;

/**
 * A class to wrap access to multiple class loaders making them work as one
 *
 * @author potatoxf
 * @date 2021/4/12
 */
public final class ContextClassLoader {

  private final ClassLoader[] classLoaders;

  public ContextClassLoader() {
    this.classLoaders = defaultClassLoader();
  }

  public ContextClassLoader(ClassLoader... classLoaders) {
    this.classLoaders = classLoaders;
  }

  private static ClassLoader[] defaultClassLoader() {
    ClassLoader classLoader = null;
    try {
      classLoader = ClassLoader.getSystemClassLoader();
    } catch (SecurityException ignored) {
      // AccessControlException on Google App Engine
    }
    if (classLoader != null) {
      return new ClassLoader[] {classLoader, classLoader.getParent()};
    }
    return null;
  }

  /**
   * Get a resource as a URL using the current class path
   *
   * @param resource - the resource to locate
   * @return the resource or null
   */
  public URL getResourceAsUrl(String resource) {
    return getResourceAsUrl(resource, getClassLoaders(null));
  }

  /**
   * Get a resource from the classpath, starting with a specific class loader
   *
   * @param resource - the resource to find
   * @param classLoader - the first classloader to try
   * @return the stream or null
   */
  public URL getResourceAsUrl(String resource, ClassLoader classLoader) {
    return getResourceAsUrl(resource, getClassLoaders(classLoader));
  }

  /**
   * Get a resource from the classpath
   *
   * @param resource - the resource to find
   * @return the stream or null
   */
  public InputStream getResourceAsStream(String resource) {
    return getResourceAsStream(resource, getClassLoaders(null));
  }

  /**
   * Get a resource from the classpath, starting with a specific class loader
   *
   * @param resource - the resource to find
   * @param classLoader - the first class loader to try
   * @return the stream or null
   */
  public InputStream getResourceAsStream(String resource, ClassLoader classLoader) {
    return getResourceAsStream(resource, getClassLoaders(classLoader));
  }

  /**
   * Find a class on the classpath (or die trying)
   *
   * @param name - the class to look for
   * @return - the class
   * @throws ClassNotFoundException Duh.
   */
  public Class<?> classForName(String name) throws ClassNotFoundException {
    return classForName(name, true, getClassLoaders(null));
  }

  /**
   * Find a class on the classpath (or die trying)
   *
   * @param name - the class to look for
   * @param initialize - the class initialize
   * @return - the class
   * @throws ClassNotFoundException Duh.
   */
  public Class<?> classForName(String name, boolean initialize) throws ClassNotFoundException {
    return classForName(name, initialize, getClassLoaders(null));
  }

  /**
   * Find a class on the classpath, starting with a specific classloader (or die trying)
   *
   * @param name - the class to look for
   * @param classLoader - the first classloader to try
   * @return - the class
   * @throws ClassNotFoundException Duh.
   */
  public Class<?> classForName(String name, ClassLoader classLoader) throws ClassNotFoundException {
    return classForName(name, true, getClassLoaders(classLoader));
  }

  /**
   * Find a class on the classpath, starting with a specific classloader (or die trying)
   *
   * @param name - the class to look for
   * @param initialize - the class initialize
   * @param classLoader - the first classloader to try
   * @return - the class
   * @throws ClassNotFoundException Duh.
   */
  public Class<?> classForName(String name, boolean initialize, ClassLoader classLoader)
      throws ClassNotFoundException {
    return classForName(name, initialize, getClassLoaders(classLoader));
  }

  /**
   * Get a resource as a URL using the current class path
   *
   * @param resource - the resource to locate
   * @param classLoader - the class loaders to examine
   * @return the resource or null
   */
  private URL getResourceAsUrl(String resource, ClassLoader[] classLoader) {
    URL url;
    for (ClassLoader cl : classLoader) {
      if (null != cl) {
        // look for the resource as passed in...
        url = cl.getResource(resource);
        // ...but some class loaders want this leading "/", so we'll add it
        // and try aget if we didn't find the resource
        if (null == url) {
          url = cl.getResource("/" + resource);
        }
        // "It's always in the last place I look for it!"
        // ... because only an idiot would keep looking for it after finding it, so stop looking
        // already.
        if (null != url) {
          return url;
        }
      }
    }
    // didn't find it anywhere.
    return null;
  }

  /**
   * Try to get a resource from a group of classLoaders
   *
   * @param resource - the resource to get
   * @param classLoader - the classLoaders to examine
   * @return the resource or null
   */
  private InputStream getResourceAsStream(String resource, ClassLoader[] classLoader) {
    for (ClassLoader cl : classLoader) {
      if (null != cl) {
        // try to find the resource as passed
        InputStream returnValue = cl.getResourceAsStream(resource);
        // now, some class loaders want this leading "/", so we'll add it and try aget if we didn't
        // find the resource
        if (null == returnValue) {
          returnValue = cl.getResourceAsStream("/" + resource);
        }
        if (null != returnValue) {
          return returnValue;
        }
      }
    }
    return null;
  }

  /**
   * Attempt to load a class from a group of classLoaders
   *
   * @param name - the class to load
   * @param initialize - the class initialize
   * @param classLoader - the group of classLoaders to examine
   * @return the class
   * @throws ClassNotFoundException - Remember the wisdom of Judge Smails: Well, the world needs
   *     ditch diggers, too.
   */
  private Class<?> classForName(String name, boolean initialize, ClassLoader[] classLoader)
      throws ClassNotFoundException {
    for (ClassLoader cl : classLoader) {
      if (null != cl) {
        try {
          return Class.forName(name, true, cl);
        } catch (ClassNotFoundException e) {
          // we'll ignore this until all classLoaders fail to locate the class
        }
      }
    }
    throw new ClassNotFoundException("Cannot find class: " + name);
  }

  private ClassLoader[] getClassLoaders(ClassLoader classLoader) {
    if (classLoaders == null) {
      return new ClassLoader[] {
        classLoader, Thread.currentThread().getContextClassLoader(), getClass().getClassLoader()
      };
    }
    int len = classLoaders.length + 3;
    ClassLoader[] result = new ClassLoader[len];
    result[0] = classLoader;
    System.arraycopy(classLoaders, 0, result, 1, classLoaders.length);
    result[len - 2] = Thread.currentThread().getContextClassLoader();
    result[len - 1] = getClass().getClassLoader();
    return result;
  }
}
