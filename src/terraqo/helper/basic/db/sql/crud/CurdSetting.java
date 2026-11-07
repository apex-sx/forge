package potatoxf.helper.basic.db.sql.crud;

import potatoxf.helper.api.Tools;
import potatoxf.helper.api.This;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author potatoxf
 * @date 2022/6/22
 */
public abstract class CurdSetting<T extends CurdSetting<T>> implements This<T> {
  private final String tableName;

  private String parameterPlaceholder = "?";

  private String keywordDelimiter = " ";

  private String fieldDelimiter = ",";

  protected CurdSetting(@Nonnull final String tableName) {
    this.tableName = Tools.requireMatchPattern(Tools.TOKEN_PATTERN, tableName, true);
  }

  @Nonnull
  public String getTableName() {
    return tableName;
  }

  @Nonnull
  public String getParameterPlaceholder() {
    return parameterPlaceholder;
  }

  @Nonnull
  public T setParameterPlaceholder(@Nonnull final String parameterPlaceholder) {
    this.parameterPlaceholder = Objects.requireNonNull(parameterPlaceholder);
    return this$();
  }

  @Nonnull
  public String getKeywordDelimiter() {
    return keywordDelimiter;
  }

  @Nonnull
  public T setKeywordDelimiter(@Nonnull final String keywordDelimiter) {
    this.keywordDelimiter = Objects.requireNonNull(keywordDelimiter);
    return this$();
  }

  @Nonnull
  public String getFieldDelimiter() {
    return fieldDelimiter;
  }

  @Nonnull
  public T setFieldDelimiter(@Nonnull final String fieldDelimiter) {
    this.fieldDelimiter = Objects.requireNonNull(fieldDelimiter);
    return this$();
  }
}
