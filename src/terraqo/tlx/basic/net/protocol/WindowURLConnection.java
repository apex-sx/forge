package pxf.toolkit.basic.net.protocol;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Window驱动盘URL
 *
 * @author potatoxf
 * @date 2021/1/10
 */
public class WindowURLConnection extends URLConnection {

  public WindowURLConnection(URL url) {
    super(url);
  }

  @Override
  public void connect() throws IOException {}

  @Override
  public InputStream getInputStream() throws IOException {
    return new FileInputStream(url.toString());
  }

  @Override
  public OutputStream getOutputStream() throws IOException {
    return new FileOutputStream(url.toString());
  }
}
