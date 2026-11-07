package apexsx.forge.data.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 函数式接口，用于设置PreparedStatement参数
 * <p>
 * 该接口定义了如何为PreparedStatement设置特定类型的参数值。
 * 作为一个泛型函数式接口，它可以处理各种数据类型的参数设置。
 *
 * @param <T> 参数值的类型
 * @author zhitron
 */
@FunctionalInterface
public interface PreparedStatementSetter<T> {
    /**
     * 为PreparedStatement设置参数值
     *
     * @param preparedStatement 预编译SQL语句对象
     * @param index             参数在SQL语句中的位置索引（从1开始）
     * @param value             要设置的参数值
     * @throws SQLException 当设置参数过程中发生SQL错误时抛出
     */
    void set(PreparedStatement preparedStatement, int index, T value) throws SQLException;
}
