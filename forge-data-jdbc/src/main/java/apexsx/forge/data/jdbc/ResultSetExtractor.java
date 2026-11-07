package apexsx.forge.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集提取器接口，用于从JDBC查询结果中提取数据
 * <p>
 * 这是一个函数式接口，可以用作Lambda表达式或方法引用的目标类型
 * 主要用于将ResultSet中的数据转换为特定类型的对象
 *
 * @param <T> 提取结果的类型参数
 * @author zhitron
 */
@FunctionalInterface
public interface ResultSetExtractor<T> {

    /**
     * 从ResultSet中提取数据并转换为指定类型的结果
     *
     * @param resultSet JDBC查询结果集，调用者需要确保结果集处于可读取状态
     * @param key       与当前结果关联的键值，可用于标识特定记录或分组
     * @return 提取并转换后的结果对象
     * @throws SQLException 当访问结果集或数据转换过程中发生SQL异常时抛出
     */
    T extract(ResultSet resultSet, Object key) throws SQLException;
}
