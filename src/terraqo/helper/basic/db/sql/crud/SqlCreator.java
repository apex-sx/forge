package potatoxf.helper.basic.db.sql.crud;

import potatoxf.helper.api.CharSequenceAppendable;
import potatoxf.helper.basic.db.type.DatabaseType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Sql创建器
 *
 * @author potatoxf
 * @date 2022/6/22
 */
public interface SqlCreator {

  /**
   * 创建SQL
   *
   * @param databaseType 数据库类型
   * @param charSequenceAppendable 字符串构建器
   * @return 返回字符串参数列表
   */
  @Nonnull
  List<Object> createSql(
      @Nullable final DatabaseType databaseType, @Nonnull final CharSequenceAppendable charSequenceAppendable);

  /**
   * 创建SQL
   *
   * @param charSequenceAppendable 字符串构建器
   * @return 返回字符串参数列表
   */
  @Nonnull
  default List<Object> createSql(@Nonnull final CharSequenceAppendable charSequenceAppendable) {
    return createSql(null, charSequenceAppendable);
  }
}
