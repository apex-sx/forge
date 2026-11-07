package pxf.toolkit.basic.exception;

import org.apache.commons.lang3.StringUtils;
import pxf.toolkit.basic.ToolkitException;
import pxf.toolkit.basic.util.Empty;
import pxf.toolkit.basic.util.Is;

/**
 * 信息钩子异常，保存相关信息
 *
 * @author potatoxf
 * @date 2021/5/3
 */
public class MessageHookException extends ToolkitException {

  /**
   * 保存相关数据
   */
  private Object data;
  /**
   * 保存相关属性名
   */
  private String[] propertyNames;

  public MessageHookException() {
    super(null);
  }

  public MessageHookException(String... propertyNames) {
    super(null);
    this.propertyNames = propertyNames;
  }

  /**
   * 获取属性名的连接后的字符串
   *
   * @return 属性名的连接后的字符串
   */
  public String getPropertyNamesString() {
    if (Is.empty(propertyNames)) {
      return Empty.STRING;
    }
    return StringUtils.join(propertyNames, ',');
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public String[] getPropertyNames() {
    return propertyNames;
  }

  public void setPropertyNames(String... propertyNames) {
    this.propertyNames = propertyNames;
  }
}
