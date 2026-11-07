package potatoxf.helper.clone;

/**
 * 克隆支持类，提供默认的克隆方法
 *
 * @author potatoxf
 * @date 2021/9/26
 * @param <T> 继承类的类型
 */
public class CloneSupport<T> implements Cloneable<T> {

  @SuppressWarnings("unchecked")
  @Override
  public T clone() {
    try {
      return (T) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new CloneRuntimeException(e);
    }
  }
}
