package potatoxf.helper.basic.db.sql.crud;

import potatoxf.helper.api.CharSequenceAppendable;
import potatoxf.helper.basic.db.sql.CommonKeyWord;
import potatoxf.helper.basic.db.type.DatabaseType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Delete 语句
 *
 * @author potatoxf
 * @date 2022/6/24
 */
public final class Delete extends Where<Delete> implements StatementCreator {

  private Delete(String tableName) {
    super(tableName);
  }

  public static Delete from(String tableName) {
    return new Delete(tableName);
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
    charSequenceAppendable
        .append(CommonKeyWord.DELETE_PREFIX.getName(databaseType))
        .append(getKeywordDelimiter())
        .append(CommonKeyWord.FROM.getName(databaseType))
        .append(getKeywordDelimiter())
        .append(getTableName());
    return super.createSql(databaseType, charSequenceAppendable);
  }
}
