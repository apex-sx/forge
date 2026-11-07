package potatoxf.helper.basic.general;

/**
 * API结果异常接口
 *
 * <p>用于处理带有{@code ApiResultException}的异常来返回前端相应的{@code ApiResult}
 *
 * @author potatoxf
 * @date 2022/7/13
 */
public interface ApiResultException<E extends Throwable & ApiResultException<E>> {

  /**
   * 获取异常
   *
   * @return
   */
  ApiResult<?> getApiResult();
}
