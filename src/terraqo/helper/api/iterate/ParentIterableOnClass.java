package potatoxf.helper.api.iterate;

/**
 * @author potatoxf
 * @date 2022/7/10
 */
public final class ParentIterableOnClass extends ParentIterable<Class<?>> {
  public ParentIterableOnClass(Class<?> start) {
    super(start, Object.class, Class::getSuperclass, (c, e) -> c != null && c != Object.class);
  }
}
