package pxf.toolkit.basic.function;

/**
 * {@code Importer}构建器
 *
 * @author potatoxf
 * @date 2021/4/4
 */
public interface ImporterBuilder<T, I extends Importer<T>> extends Builder<I> {

  /**
   * 构建导出器
   *
   * @return {@code Importer}
   */
  @Override
  I build();
}
