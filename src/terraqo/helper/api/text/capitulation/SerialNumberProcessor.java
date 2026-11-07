package potatoxf.helper.api.text.capitulation;

/**
 * 序号处理器
 *
 * @author potatoxf
 * @date 2021/06/19
 */
public interface SerialNumberProcessor {

  /**
   * 处理序号
   *
   * @param serialNumber 序号
   * @return 返回不同样式的序号
   */
  String handle(int serialNumber);
}
