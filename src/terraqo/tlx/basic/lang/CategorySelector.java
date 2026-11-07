package pxf.toolkit.basic.lang;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 类别选择器，该类的目的是简化 {@code if elseif}
 *
 * <p>一般会出现这种情况：
 *
 * <pre>
 *     if(val==xxx){
 *         ...
 *     }else if(val==xxx1){
 *         ...
 *     }else if(val==xxx2){
 *         ...
 *     }
 * </pre>
 *
 * @author potatoxf
 * @date 2021/3/25
 */
public final class CategorySelector<T extends CategorySelector.Category> {

  /** 是否允许运行的时候添加 */
  private final boolean isAllowAddWhenRunning;
  /** 是否找不到的时候返回 {@code null} */
  private final boolean isReturnNullWhenNotFound;
  /** 选择列表 */
  private volatile Map<Object, T> selectMap;

  private CategorySelector(
      boolean isAllowAddWhenRunning, boolean isReturnNullWhenNotFound, Map<Object, T> selectMap) {
    this.isAllowAddWhenRunning = isAllowAddWhenRunning;
    this.isReturnNullWhenNotFound = isReturnNullWhenNotFound;
    this.selectMap = selectMap;
  }

  /**
   * 构建选择列表
   *
   * @param actionList 执行对象列表
   * @param <T>对象类型
   * @return {@code SequenceSelect<T>}
   * @throws IllegalArgumentException 当{@code actionList}为空
   */
  @SafeVarargs
  public static <T extends CategorySelector.Category> CategorySelector<T> of(T... actionList) {
    return of(false, false, actionList);
  }

  /**
   * 构建选择列表
   *
   * @param isAllowAddWhenRunning 是否允许运行的时候添加
   * @param isReturnNullWhenNotFound 是否找不到的时候返回 {@code null}
   * @param actionList 执行对象列表
   * @param <T>对象类型
   * @return {@code SequenceSelect<T>}
   * @throws IllegalArgumentException 当{@code actionList}为空
   */
  @SafeVarargs
  public static <T extends CategorySelector.Category> CategorySelector<T> of(
      boolean isAllowAddWhenRunning, boolean isReturnNullWhenNotFound, T... actionList) {
    return new CategorySelector<>(
        isAllowAddWhenRunning, isReturnNullWhenNotFound, buildActionMap(null, actionList));
  }

  /**
   * 构建选择列表
   *
   * @param actionList 执行对象列表
   * @param <T>对象类型
   * @return {@code SequenceSelect<T>}
   * @throws IllegalArgumentException 当{@code actionList}为空
   */
  @SafeVarargs
  private static <T extends CategorySelector.Category> Map<Object, T> buildActionMap(
      Map<Object, T> oldSelectMap, T... actionList) {
    if (oldSelectMap == null) {
      oldSelectMap = Collections.emptyMap();
      if (actionList == null || actionList.length == 0) {
        return oldSelectMap;
      }
    }
    Map<Object, T> selectMap = new HashMap<>(oldSelectMap.size() + actionList.length, 1);
    selectMap.putAll(oldSelectMap);
    for (T action : actionList) {
      if (action == null) {
        continue;
      }
      Object category = action.category();
      if (selectMap.containsKey(category)) {
        throw new IllegalArgumentException(
            "The same execution target already exists for" + category);
      }
      selectMap.put(category, action);
    }
    return selectMap;
  }

  /**
   * 扩充选择列表
   *
   * @param actionList 执行对象列表
   */
  @SafeVarargs
  public final void expandActionList(T... actionList) {
    if (!isAllowAddWhenRunning) {
      throw new RuntimeException("The selection list is not allowed to expand at runtime");
    }
    synchronized (this) {
      this.selectMap = buildActionMap(selectMap, actionList);
    }
  }

  /**
   * 选择出目标对象
   *
   * @param selectKey 选择键
   * @return 目标对象
   * @throws IllegalArgumentException 当{@code selectKey}小于0或者大于目标长度
   */
  public final synchronized T selectAction(Object selectKey) {
    if (selectKey == null) {
      throw new IllegalArgumentException("The selectKey must be no null");
    }
    T result = selectMap.get(selectKey);
    if (result == null && !isReturnNullWhenNotFound) {
      throw new RuntimeException("The action object was not found");
    }
    return result;
  }

  /**
   * 是否允许运行的时候添加
   *
   * @return 如果 {@code true}则允许添加，否则 {@code false}
   */
  public final boolean isAllowAddWhenRunning() {
    return isAllowAddWhenRunning;
  }

  /**
   * 是否找不到的时候返回 {@code null}
   *
   * @return 如果 {@code true}则返回 {@code null}，否则 {@code false}
   */
  public final boolean isReturnNullWhenNotFound() {
    return isReturnNullWhenNotFound;
  }

  /** 类别 */
  public interface Category {

    /**
     * 在同一 {@code CategorySelector}中 ，类别需要唯一
     *
     * @return {@code Object}返回该类别
     */
    Object category();
  }
}
