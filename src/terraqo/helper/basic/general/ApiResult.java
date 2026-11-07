package potatoxf.helper.basic.general;

import org.springframework.http.HttpStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Api调用返回结果
 *
 * @author potatoxf
 * @date 2022/7/13
 */
public class ApiResult<T> {

  /** 成功 */
  public static final String MESSAGE_OK = "OK";
  /** 失败 */
  public static final String MESSAGE_FAIL = "FAIL";
  /** 成功码 */
  public static final int CODE_OK = 0;
  /** 失败码 */
  public static final int CODE_FAIL = 1;
  /** 是否成功 */
  private final boolean success;
  /** api 编码 */
  private final int code;
  /** 返回处理信息 */
  @Nonnull private final String message;
  /** 数据 */
  @Nullable private final T data;
  /**
   * 构造失败API结果
   *
   * @param message 消息信息
   * @return {@code ApiResult<?>}
   */
  @Nonnull
  public static ApiResult<?> ofFail(@Nullable String message) {
    return ofFail(CODE_FAIL, message, null);
  }
  /**
   * 构造失败API结果
   *
   * @param code API结果码
   * @param message 消息信息
   * @return {@code ApiResult<?>}
   */
  @Nonnull
  public static ApiResult<?> ofFail(int code, @Nullable String message) {
    return ofFail(code, message, null);
  }

  /**
   * 构造失败API结果
   *
   * @param code API结果码
   * @param message 消息信息
   * @param data 数据
   * @param <T> 数据类型
   * @return {@code ApiResult<T>}
   */
  @Nonnull
  public static <T> ApiResult<T> ofFail(int code, @Nullable String message, @Nullable T data) {
    return of(false, code, message, data);
  }
  /**
   * 构造成功API结果
   *
   * @return {@code ApiResult<?>}
   */
  @Nonnull
  public static ApiResult<?> ofSuccess() {
    return ofSuccess(CODE_OK, null, null);
  }
  /**
   * 构造成功API结果
   *
   * @param data 数据
   * @param <T> 数据类型
   * @return {@code ApiResult<T>}
   */
  @Nonnull
  public static <T> ApiResult<T> ofSuccess(@Nullable T data) {
    return ofSuccess(CODE_OK, null, data);
  }

  /**
   * 构造成功API结果
   *
   * @param code API结果码
   * @param data 数据
   * @param <T> 数据类型
   * @return {@code ApiResult<T>}
   */
  @Nonnull
  public static <T> ApiResult<T> ofSuccess(int code, @Nullable T data) {
    return ofSuccess(code, null, data);
  }

  /**
   * 构造成功API结果
   *
   * @param code API结果码
   * @param message 消息信息
   * @param data 数据
   * @param <T> 数据类型
   * @return {@code ApiResult<T>}
   */
  @Nonnull
  public static <T> ApiResult<T> ofSuccess(int code, @Nullable String message, @Nullable T data) {
    return of(true, code, message, data);
  }

  /**
   * 构造API结果
   *
   * @param httpStatus HTTP状态码
   * @return {@code ApiResult<T>}
   */
  @Nonnull
  public static ApiResult<?> of(@Nonnull HttpStatus httpStatus) {
    return of(httpStatus.series() == HttpStatus.Series.SUCCESSFUL, httpStatus.value(), null, null);
  }
  /**
   * 构造API结果
   *
   * @param success 是否成功
   * @return {@code ApiResult<T>}
   */
  @Nonnull
  public static ApiResult<?> of(boolean success) {
    return of(success, success ? CODE_OK : CODE_FAIL, null, null);
  }

  /**
   * 构造API结果
   *
   * @param apiResult 是否成功
   * @param message 消息信息
   * @param <T> 数据类型
   * @return {@code ApiResult<T>}
   */
  @Nonnull
  public static <T> ApiResult<T> copyExceptMessage(
      @Nonnull ApiResult<T> apiResult, @Nullable String message) {
    return of(apiResult.success, apiResult.code, message, apiResult.data);
  }

  /**
   * 构造API结果
   *
   * @param apiResult 是否成功
   * @param data 数据
   * @param <T> 数据类型
   * @return {@code ApiResult<T>}
   */
  @Nonnull
  public static <T> ApiResult<T> copyExceptData(@Nonnull ApiResult<?> apiResult, @Nullable T data) {
    return of(apiResult.success, apiResult.code, apiResult.message, data);
  }
  /**
   * 构造API结果
   *
   * @param success 是否成功
   * @param code API结果码
   * @param message 消息信息
   * @param data 数据
   * @param <T> 数据类型
   * @return {@code ApiResult<T>}
   */
  @Nonnull
  public static <T> ApiResult<T> of(
      boolean success, int code, @Nullable String message, @Nullable T data) {
    if (success) {
      if (message == null) {
        return new ApiResult<>(true, code, MESSAGE_OK, data);
      } else {
        return new ApiResult<>(true, code, message, data);
      }
    } else {
      if (message == null) {
        return new ApiResult<>(false, code, MESSAGE_FAIL, data);
      } else {
        return new ApiResult<>(false, code, message, data);
      }
    }
  }

  private ApiResult(boolean success, int code, @Nonnull String message, @Nullable T data) {
    this.success = success;
    this.code = code;
    this.message = message;
    this.data = data;
  }

  /**
   * 判断返回结果是否成功
   *
   * @return {@code boolean}
   */
  public boolean isSuccess() {
    return success;
  }

  /**
   * 获取API结果码
   *
   * @return {@code int}
   */
  public int getCode() {
    return code;
  }

  /**
   * 获取API结果信息
   *
   * @return {@code int}
   */
  @Nonnull
  public String getMessage() {
    return message;
  }

  /**
   * 获取API结果数据
   *
   * @return {@code T}
   */
  @Nullable
  public T getData() {
    return data;
  }
}
