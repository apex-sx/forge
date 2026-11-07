package potatoxf.helper.lang.ansi;

/**
 * ANSI可转义节点接口，实现为ANSI颜色等
 *
 * <p>来自Spring Boot
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public interface AnsiElement {

  /**
   * @return ANSI转义编码
   */
  @Override
  String toString();
}
