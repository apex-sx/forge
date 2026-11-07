package potatoxf.helper.thread;

import java.util.concurrent.ExecutorService;

/**
 * 保证ExecutorService在对象回收时正常结束
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class FinalizableDelegatedExecutorService extends DelegatedExecutorService {

  /**
   * 构造
   *
   * @param executor {@link ExecutorService}
   */
  FinalizableDelegatedExecutorService(ExecutorService executor) {
    super(executor);
  }

  @Override
  protected void finalize() {
    super.shutdown();
  }
}
