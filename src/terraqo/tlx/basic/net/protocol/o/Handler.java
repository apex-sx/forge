package pxf.toolkit.basic.net.protocol.o;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import pxf.toolkit.basic.net.protocol.WindowURLConnection;

/**
 * Window驱动盘URL
 *
 * @author potatoxf
 * @date 2021/1/10
 */
public class Handler extends URLStreamHandler {

  @Override
  protected URLConnection openConnection(URL u) throws IOException {
    return new WindowURLConnection(u);
  }
}
