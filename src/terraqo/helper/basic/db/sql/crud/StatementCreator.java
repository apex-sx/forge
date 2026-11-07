package potatoxf.helper.basic.db.sql.crud;

import potatoxf.helper.api.CharSequenceAppendable;
import potatoxf.helper.api.map.Parametric;
import potatoxf.helper.basic.db.JdbcHelper;
import potatoxf.helper.basic.db.type.DatabaseType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * {@code PreparedStatement}创建器接口
 *
 * @author potatoxf
 * @date 2022/6/22
 */
public interface StatementCreator extends SqlCreator {

  /**
   * 创建{@code PreparedStatement}
   *
   * @param connection jdbc连接
   * @return {@code PreparedStatement}
   * @throws SQLException 如果创建发生异常
   */
  default PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
    CharSequenceAppendable stringBuilder = CharSequenceAppendable.ofStringBuilder();
    List<Object> parameters = createSql(DatabaseType.parse(connection), stringBuilder);
    String sql = stringBuilder.toString();
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    for (int i = 1; i < parameters.size(); i++) {
      JdbcHelper.setStatementValue(preparedStatement, i, parameters.get(0), (Parametric) null);
    }
    return preparedStatement;
  }
}
