package pxf.toolkit.basic.function;

/**
 * 可刷新标识
 *
 * @author potatoxf
 * @date 2021/5/28
 */
public interface Refreshable {

  /** 将缓存里的内容重刷新 */
  void refresh();
}
