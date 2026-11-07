package potatoxf.helper.text.csv;

/**
 * CSV的行处理器，实现此接口用于按照行处理数据
 *
 * @author potatoxf
 * @date 2021/9/26
 */
@FunctionalInterface
public interface CsvRowHandler {

  /**
   * 处理行数据
   *
   * @param row 行数据
   */
  void handle(CsvRow row);
}
