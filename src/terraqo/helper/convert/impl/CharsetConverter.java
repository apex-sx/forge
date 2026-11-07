package potatoxf.helper.convert.impl;

import potatoxf.helper.convert.AbstractConverter;
import potatoxf.helper.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * 编码对象转换器
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class CharsetConverter extends AbstractConverter<Charset> {
  private static final long serialVersionUID = 1L;

  @Override
  protected Charset convertInternal(Object value) {
    return CharsetUtil.charset(convertToStr(value));
  }
}
