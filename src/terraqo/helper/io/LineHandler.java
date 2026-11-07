package potatoxf.helper.io;

/**
 * 行处理器
 *
 * @author potatoxf
 * @date 2021/9/26
 */
@FunctionalInterface
public interface LineHandler {
  /**
   * 处理一行数据，可以编辑后存入指定地方
   *
   * @param line 行
   */
  void handle(String line);
}
