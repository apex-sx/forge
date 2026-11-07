package pxf.toolkit.basic.net.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.lang.ContextResource;

/**
 * 类路径URL连接
 *
 * @author potatoxf
 * @date 2021/1/10
 */
public class ClasspathURLConnection extends URLConnection {

  private static final Logger LOG = LoggerFactory.getLogger(ClasspathURLConnection.class);
  private final ContextResource contextResource = new ContextResource();

  public ClasspathURLConnection(URL url) {
    super(url);
  }

  @Override
  public void connect() throws IOException {}

  @Override
  public InputStream getInputStream() throws IOException {
    String classpath = url.toString().substring(10);
    if (classpath.startsWith("/")) {
      classpath = classpath.substring(1);
    }
    if (LOG.isTraceEnabled()) {
      LOG.trace(String.format("Parse URL [%s] as classpath for [%s]", url, classpath));
    }
    return contextResource.getResourceAsStream(classpath);
  }
}
