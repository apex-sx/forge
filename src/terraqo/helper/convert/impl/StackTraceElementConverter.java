package potatoxf.helper.convert.impl;

import potatoxf.helper.Tools;
import potatoxf.helper.api.map.MapUtil;
import potatoxf.helper.convert.AbstractConverter;

import java.util.Map;

/**
 * {@link StackTraceElement} 转换器<br>
 * 只支持Map方式转换
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class StackTraceElementConverter extends AbstractConverter<StackTraceElement> {
  private static final long serialVersionUID = 1L;

  @Override
  protected StackTraceElement convertInternal(Object value) {
    if (value instanceof Map) {
      final Map<?, ?> map = (Map<?, ?>) value;

      final String declaringClass = MapUtil.getStr(map, "className");
      final String methodName = MapUtil.getStr(map, "methodName");
      final String fileName = MapUtil.getStr(map, "fileName");
      final Integer lineNumber = MapUtil.getInt(map, "lineNumber");

      return new StackTraceElement(
          declaringClass, methodName, fileName, Tools.safeValue(lineNumber, 0));
    }
    return null;
  }
}
