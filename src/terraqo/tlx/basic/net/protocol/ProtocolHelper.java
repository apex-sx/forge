package pxf.toolkit.basic.net.protocol;

import pxf.toolkit.basic.lang.ExpandStringBuilder;
import pxf.toolkit.basic.util.Empty;

/**
 * 协议助手类
 *
 * @author potatoxf
 * @date 2021/1/30
 */
public final class ProtocolHelper {

  public static final String URL_PROTOCOL_KEY = "java.protocol.handler.pkgs";
  public static final String EXTEND_URL_PROTOCOL = "pxf.toolkit.basic.net.protocol";

  private ProtocolHelper() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 加载协议
   *
   * @see #load(String)
   */
  public static void load() {
    load(null);
  }

  /**
   * 加载协议
   *
   * @param urlProtocolPackage url协议包名
   */
  public static void load(String urlProtocolPackage) {
    String[] urlProtocolPackages;
    if (urlProtocolPackage == null) {
      urlProtocolPackages = Empty.STRING_ARRAY;
    } else {
      urlProtocolPackages = urlProtocolPackage.split("\\|");
    }
    String property = System.getProperty(URL_PROTOCOL_KEY);
    if (property == null) {
      ExpandStringBuilder sb = new ExpandStringBuilder(50);
      for (String protocolPackage : urlProtocolPackages) {
        sb.appendWhenNoEmpty('|').append(protocolPackage.trim());
      }
      sb.append('|').append(EXTEND_URL_PROTOCOL);
      property = sb.toString();
    } else {
      if (property.contains(EXTEND_URL_PROTOCOL) && urlProtocolPackage == null) {
        // 不需要重复加载
        return;
      }
      ExpandStringBuilder sb = new ExpandStringBuilder(50);
      sb.append(property);
      sb.append('|').append(EXTEND_URL_PROTOCOL);
      for (String protocolPackage : urlProtocolPackages) {
        protocolPackage = protocolPackage.trim();
        if (sb.indexOf(protocolPackage) <= -1) {
          sb.appendWhenNoEmpty('|').append(protocolPackage);
        }
      }
      property = sb.toString();
    }
    System.setProperty(URL_PROTOCOL_KEY, property);
  }
}
