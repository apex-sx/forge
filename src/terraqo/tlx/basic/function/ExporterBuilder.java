package pxf.toolkit.basic.function;

/**
 * {@code Exporter}构建器
 *
 * @author potatoxf
 * @date 2021/4/4
 */
public interface ExporterBuilder<T extends Exporter> extends Builder<T> {

  /**
   * 构建导出器
   *
   * @return {@code Exporter}
   */
  @Override
  T build();
}
