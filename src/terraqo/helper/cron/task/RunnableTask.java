package potatoxf.helper.cron.task;

/**
 * {@link Runnable} 的 {@link Task}包装
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class RunnableTask implements Task {
  private final Runnable runnable;

  public RunnableTask(Runnable runnable) {
    this.runnable = runnable;
  }

  @Override
  public void execute() {
    runnable.run();
  }
}
