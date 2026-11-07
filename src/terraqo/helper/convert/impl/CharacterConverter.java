package potatoxf.helper.convert.impl;

import potatoxf.helper.api.Whether;
import potatoxf.helper.convert.AbstractConverter;
import potatoxf.helper.util.BooleanUtil;

/**
 * 字符转换器
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class CharacterConverter extends AbstractConverter<Character> {
  private static final long serialVersionUID = 1L;

  @Override
  protected Character convertInternal(Object value) {
    if (value instanceof Boolean) {
      return BooleanUtil.toCharacter((Boolean) value);
    } else {
      final String valueStr = convertToStr(value);
      if (Whether.noBlank(valueStr)) {
        return valueStr.charAt(0);
      }
    }
    return null;
  }
}
