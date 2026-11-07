package potatoxf.helper.basic.db.sql.crud;

import potatoxf.helper.api.CharSequenceAppendable;
import potatoxf.helper.api.base.ImmutablePair;
import potatoxf.helper.basic.db.sql.CommonKeyWord;
import potatoxf.helper.basic.db.type.DatabaseType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author potatoxf
 * @date 2022/6/22
 */
public final class Update extends Where<Update> implements StatementCreator {
  private final List<ImmutablePair<String, Object>> values = new LinkedList<>();

  private Update(String tableName) {
    super(tableName);
  }

  public static Update from(String tableName) {
    return new Update(tableName);
  }

  /**
   * 设置值
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public Update set(@Nonnull final String columnName, @Nullable final Object value) {
    values.add(new ImmutablePair<>(columnName, value));
    return this;
  }

  /**
   * 如果不为null则设置值
   *
   * @param columnName 列名
   * @param value 值
   * @return {@code this}
   */
  @Nonnull
  public Update setIfNoNull(@Nonnull final String columnName, @Nullable final Object value) {
    if (value != null) {
      values.add(new ImmutablePair<>(columnName, value));
    }
    return this;
  }

  /**
   * 创建SQL
   *
   * @param databaseType 数据库类型
   * @param charSequenceAppendable 字符串构建器
   * @return 返回字符串参数列表
   */
  @Nonnull
  @Override
  public List<Object> createSql(
      @Nullable final DatabaseType databaseType, @Nonnull final CharSequenceAppendable charSequenceAppendable) {
    List<Object> parameters = new ArrayList<>();
    charSequenceAppendable
        .append(CommonKeyWord.UPDATE_PREFIX.getName(databaseType))
        .append(getKeywordDelimiter())
        .append(getTableName())
        .append(getKeywordDelimiter())
        .append(CommonKeyWord.UPDATE_SET.getName(databaseType))
        .append(getKeywordDelimiter());
    Iterator<ImmutablePair<String, Object>> iterator = values.iterator();
    ImmutablePair<String, Object> next = iterator.next();
    Object value = next.getValue();
    if (value == null) {
      charSequenceAppendable
          .append(next.getKey())
          .append(CommonKeyWord.UPDATE_ASSIGN.getName(databaseType))
          .append(CommonKeyWord.NULL.getName(databaseType));
    } else {
      charSequenceAppendable
          .append(next.getKey())
          .append(CommonKeyWord.UPDATE_ASSIGN.getName(databaseType))
          .append(getParameterPlaceholder());
      parameters.add(value);
    }
    while (iterator.hasNext()) {
      next = iterator.next();
      value = next.getValue();
      charSequenceAppendable.append(getFieldDelimiter()).append(" ");
      if (value == null) {
        charSequenceAppendable
            .append(next.getKey())
            .append(CommonKeyWord.UPDATE_ASSIGN.getName(databaseType))
            .append(CommonKeyWord.NULL.getName(databaseType));
      } else {
        charSequenceAppendable
            .append(next.getKey())
            .append(CommonKeyWord.UPDATE_ASSIGN.getName(databaseType))
            .append(getParameterPlaceholder());
        parameters.add(value);
      }
    }
    parameters.addAll(super.createSql(databaseType, charSequenceAppendable));
    return parameters;
  }
}
