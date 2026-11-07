package potatoxf.helper.bean.copier;

import potatoxf.helper.Tools;
import potatoxf.helper.lang.copier.Copier;

/**
 * 抽象的对象拷贝封装，提供来源对象、目标对象持有
 *
 * @param <S> 来源对象类型
 * @param <T> 目标对象类型
 * @author potatoxf
 * @date 2021/9/26
 */
public abstract class AbsCopier<S, T> implements Copier<T> {

  protected final S source;
  protected final T target;
  /** 拷贝选项 */
  protected final CopyOptions copyOptions;

  public AbsCopier(S source, T target, CopyOptions copyOptions) {
    this.source = source;
    this.target = target;
    this.copyOptions = Tools.safeValue(copyOptions, CopyOptions::create);
  }
}
