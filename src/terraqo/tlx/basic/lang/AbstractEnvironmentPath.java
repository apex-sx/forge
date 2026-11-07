package pxf.toolkit.basic.lang;

import java.nio.file.Path;
import java.util.function.Function;
import javax.annotation.Nullable;
import pxf.toolkit.basic.util.Is;

/**
 * 抽象环境路径
 *
 * @author potatoxf
 * @date 2021/4/18
 */
public abstract class AbstractEnvironmentPath {

  private final CategorySelector<PathDecorator> pathHandlerSelector =
      CategorySelector.of(true, true);
  private final String environmentKey;
  private final String propertyKey;

  protected AbstractEnvironmentPath() {
    this("APPLICATION_HOME", "user.dir");
  }

  protected AbstractEnvironmentPath(String environmentKey, String propertyKey) {
    this.environmentKey = environmentKey;
    this.propertyKey = propertyKey;
  }

  /**
   * 获取配置路径字符串
   *
   * @return {@code String}
   */
  @Nullable
  public final String getConfigPathString() {
    String result = System.getenv(environmentKey);
    if (Is.empty(result)) {
      result = System.getProperty(propertyKey);
    }
    if (Is.empty(result)) {
      return null;
    }
    return result;
  }

  /**
   * 获取配置路径
   *
   * @return {@code Path}
   */
  @Nullable
  public final Path getConfigPath() {
    String configPathString = getConfigPathString();
    if (configPathString != null) {
      return Path.of(configPathString);
    }
    return null;
  }

  /**
   * 获取处理后的配置路径字符串
   *
   * @param catalogKey 分类键
   * @return {@code String}
   */
  @Nullable
  public final String getDecoratedConfigPathString(String catalogKey) {
    Path configPath = getConfigPath();
    if (configPath != null) {
      PathDecorator pathDecorator = pathHandlerSelector.selectAction(catalogKey);
      if (pathDecorator == null) {
        return configPath.toString();
      }
      configPath = pathDecorator.apply(configPath);
      if (configPath != null) {
        return configPath.toString();
      }
    }
    return null;
  }

  /**
   * 获取处理后的配置路径
   *
   * @param catalogKey 分类键
   * @return {@code Path}
   */
  @Nullable
  public final Path getDecoratedConfigPath(String catalogKey) {
    Path configPath = getConfigPath();
    if (configPath != null) {
      PathDecorator pathDecorator = pathHandlerSelector.selectAction(catalogKey);
      if (pathDecorator == null) {
        return configPath;
      }
      return pathDecorator.apply(configPath);
    }
    return null;
  }

  /**
   * 配置路径处理
   *
   * @param pathDecorators 路径处理器
   */
  public final void configPathDecorator(PathDecorator... pathDecorators) {
    pathHandlerSelector.expandActionList(pathDecorators);
  }

  public interface PathDecorator extends Function<Path, Path>, CategorySelector.Category {}
}
