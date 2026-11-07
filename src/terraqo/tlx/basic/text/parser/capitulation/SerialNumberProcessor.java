package pxf.toolkit.basic.text.parser.capitulation;

import javax.annotation.Nonnull;

/**
 * 序号处理器
 *
 * @author potatoxf
 * @date 2021/5/22
 */
public interface SerialNumberProcessor {

  /**
   * 处理序号
   *
   * @param serialNumber 序号
   * @return 返回不同样式的序号
   */
  @Nonnull
  String handle(int serialNumber);
}
