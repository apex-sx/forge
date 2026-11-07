package pxf.toolkit.basic.lang;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;
import pxf.toolkit.basic.util.Close;

/**
 * A class to simplify access to resources through the classLoader.
 *
 * @author potatoxf
 * @date 2021/4/12
 */
public final class ContextResource {

  private final ContextClassLoader contextClassLoader;
  private final Charset charset;

  public ContextResource() {
    this(new ContextClassLoader(), StandardCharsets.UTF_8);
  }

  public ContextResource(ContextClassLoader contextClassLoader, Charset charset) {
    this.contextClassLoader =
        Objects.requireNonNullElseGet(contextClassLoader, ContextClassLoader::new);
    this.charset = charset;
  }

  public ContextResource(Charset charset) {
    this(new ContextClassLoader(), charset);
  }

  public ContextResource(ContextClassLoader contextClassLoader) {
    this(contextClassLoader, StandardCharsets.UTF_8);
  }

  /**
   * Returns a resource on the classpath as a Properties object
   *
   * @param resource The resource to find
   * @return The resource
   * @throws IOException If the resource cannot be found or read
   */
  public Properties getResourceAsProperties(String resource) throws IOException {
    return getResourceAsProperties(resource, null);
  }

  /**
   * Returns a resource on the classpath as a Properties object
   *
   * @param loader The classLoader used to fetch the resource
   * @param resource The resource to find
   * @return The resource
   * @throws IOException If the resource cannot be found or read
   */
  public Properties getResourceAsProperties(String resource, ClassLoader loader)
      throws IOException {
    Properties props = new Properties();
    InputStream in = getResourceAsStream(resource, loader);
    try {
      props.load(in);
      return props;
    } finally {
      Close.closeableSilently(in);
    }
  }

  /**
   * Returns a resource on the classpath as a Reader object
   *
   * @param resource The resource to find
   * @return The resource
   * @throws IOException If the resource cannot be found or read
   */
  public Reader getResourceAsReader(String resource) throws IOException {
    Reader reader;
    if (charset == null) {
      reader = new InputStreamReader(getResourceAsStream(resource));
    } else {
      reader = new InputStreamReader(getResourceAsStream(resource), charset);
    }
    return reader;
  }

  /**
   * Returns a resource on the classpath as a Reader object
   *
   * @param resource The resource to find
   * @param loader The classLoader used to fetch the resource
   * @return The resource
   * @throws IOException If the resource cannot be found or read
   */
  public Reader getResourceAsReader(String resource, ClassLoader loader) throws IOException {
    Reader reader;
    if (charset == null) {
      reader = new InputStreamReader(getResourceAsStream(resource, loader));
    } else {
      reader = new InputStreamReader(getResourceAsStream(resource, loader), charset);
    }
    return reader;
  }

  /**
   * Returns a resource on the classpath as a Stream object
   *
   * @param resource The resource to find
   * @return The resource
   * @throws IOException If the resource cannot be found or read
   */
  public InputStream getResourceAsStream(String resource) throws IOException {
    return getResourceAsStream(resource, null);
  }

  /**
   * Returns a resource on the classpath as a Stream object
   *
   * @param resource The resource to find
   * @param loader The classLoader used to fetch the resource
   * @return The resource
   * @throws IOException If the resource cannot be found or read
   */
  public InputStream getResourceAsStream(String resource, ClassLoader loader) throws IOException {
    InputStream in = contextClassLoader.getResourceAsStream(resource, loader);
    if (in == null) {
      throw new IOException("Could not find resource " + resource);
    }
    return in;
  }

  /**
   * Returns a resource on the classpath as a File object
   *
   * @param resource The resource to find
   * @return The resource
   * @throws IOException If the resource cannot be found or read
   */
  public File getResourceAsFile(String resource) throws IOException {
    return new File(getResourceUrl(resource).getFile());
  }

  /**
   * Returns the URL of the resource on the classpath
   *
   * @param resource The resource to find
   * @return The resource
   * @throws IOException If the resource cannot be found or read
   */
  public URL getResourceUrl(String resource) throws IOException {
    // issue #625
    return getResourceUrl(resource, null);
  }

  /**
   * Returns the URL of the resource on the classpath
   *
   * @param resource The resource to find
   * @param loader The classLoader used to fetch the resource
   * @return The resource
   * @throws IOException If the resource cannot be found or read
   */
  public URL getResourceUrl(String resource, ClassLoader loader) throws IOException {
    URL url = contextClassLoader.getResourceAsUrl(resource, loader);
    if (url == null) {
      throw new IOException("Could not find resource " + resource);
    }
    return url;
  }

  /**
   * Returns a resource on the classpath as a File object
   *
   * @param resource - the resource to find
   * @param loader - the classLoader used to fetch the resource
   * @return The resource
   * @throws IOException If the resource cannot be found or read
   */
  public File getResourceAsFile(String resource, ClassLoader loader) throws IOException {
    return new File(getResourceUrl(resource, loader).getFile());
  }

  /**
   * Gets a URL as a Reader
   *
   * @param urlString - the URL to get
   * @return A Reader with the data from the URL
   * @throws IOException If the resource cannot be found or read
   */
  public Reader getUrlAsReader(String urlString) throws IOException {
    Reader reader;
    if (charset == null) {
      reader = new InputStreamReader(getUrlAsStream(urlString));
    } else {
      reader = new InputStreamReader(getUrlAsStream(urlString), charset);
    }
    return reader;
  }

  /**
   * Gets a URL as an input stream
   *
   * @param urlString - the URL to get
   * @return An input stream with the data from the URL
   * @throws IOException If the resource cannot be found or read
   */
  public InputStream getUrlAsStream(String urlString) throws IOException {
    URL url = new URL(urlString);
    URLConnection conn = url.openConnection();
    return conn.getInputStream();
  }

  /**
   * Gets a URL as a Properties object
   *
   * @param urlString - the URL to get
   * @return A Properties object with the data from the URL
   * @throws IOException If the resource cannot be found or read
   */
  public Properties getUrlAsProperties(String urlString) throws IOException {
    Properties props = new Properties();
    InputStream in = getUrlAsStream(urlString);
    props.load(in);
    return props;
  }

  /**
   * Loads a class
   *
   * @param className - the class to fetch
   * @return The loaded class
   * @throws ClassNotFoundException If the class cannot be found (duh!)
   */
  public Class<?> classForName(String className) throws ClassNotFoundException {
    return contextClassLoader.classForName(className);
  }
}
