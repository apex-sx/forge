package potatoxf.helper.convert.impl;

import potatoxf.helper.HelpCollection;
import potatoxf.helper.Tools;
import potatoxf.helper.convert.Converter;
import potatoxf.helper.util.TypeUtil;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * 各种集合类转换器
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class CollectionConverter implements Converter<Collection<?>> {

  /** 集合类型 */
  private final Type collectionType;
  /** 集合元素类型 */
  private final Type elementType;

  /** 构造，默认集合类型使用{@link Collection} */
  public CollectionConverter() {
    this(Collection.class);
  }

  // ----------------------------------------------------------------------------------------------
  // Constractor start
  /**
   * 构造
   *
   * @param collectionType 集合类型
   */
  public CollectionConverter(Type collectionType) {
    this(collectionType, TypeUtil.getTypeArgument(collectionType));
  }

  /**
   * 构造
   *
   * @param collectionType 集合类型
   */
  public CollectionConverter(Class<?> collectionType) {
    this(collectionType, TypeUtil.getTypeArgument(collectionType));
  }

  /**
   * 构造
   *
   * @param collectionType 集合类型
   * @param elementType 集合元素类型
   */
  public CollectionConverter(Type collectionType, Type elementType) {
    this.collectionType = collectionType;
    this.elementType = elementType;
  }
  // ----------------------------------------------------------------------------------------------
  // Constractor end

  @Override
  public Collection<?> convert(Object value, Collection<?> defaultValue)
      throws IllegalArgumentException {
    final Collection<?> result = convertInternal(value);
    return Tools.safeValue(result, defaultValue);
  }

  /**
   * 内部转换
   *
   * @param value 值
   * @return 转换后的集合对象
   */
  protected Collection<?> convertInternal(Object value) {
    final Collection<Object> collection =
        HelpCollection.create(TypeUtil.getClass(this.collectionType));
    return HelpCollection.addAll(collection, value, this.elementType);
  }
}
