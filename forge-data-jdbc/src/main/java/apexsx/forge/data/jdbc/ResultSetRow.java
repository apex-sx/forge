package apexsx.forge.data.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 抽象类，用于从ResultSet中提取特定类型的值
 * 该类提供了对JDBC ResultSet的基本封装和访问方法
 *
 * @author zhitron
 */
public abstract class ResultSetRow {
    // 封装的ResultSet对象，不能为空
    private final ResultSet resultSet;
    private ResultSetMetaData resultSetMetaData;
    private Integer columnCount;
    private String[] columnLabels;
    private String[] columnNames;

    private int currentRow = 0;

    public int getCurrentRow() {
        return currentRow;
    }

    /**
     * 构造函数，初始化ResultSetTypedExtractor实例
     *
     * @param resultSet JDBC ResultSet对象，用于数据提取
     * @throws NullPointerException 如果resultSet为null则抛出异常
     */
    public ResultSetRow(ResultSet resultSet) {
        this.resultSet = Objects.requireNonNull(resultSet);
    }

    /**
     * 移动到结果集的下一行记录
     *
     * @return 如果存在下一行记录返回true，否则返回false
     * @throws SQLException 如果在移动游标时发生数据库访问错误
     */
    boolean next() throws SQLException {
        boolean next = resultSet.next();
        currentRow++;
        return next;
    }

    /**
     * 获取内部封装的ResultSet对象
     *
     * @return 当前封装的ResultSet对象
     */
    ResultSet resultSet() {
        return resultSet;
    }

    public int getColumnCount() throws SQLException {
        if (resultSetMetaData == null) {
            resultSetMetaData = resultSet.getMetaData();
        }
        if (columnCount == null) {
            columnCount = resultSetMetaData.getColumnCount();
        }
        return columnCount;
    }

    public String getColumnLabel(int index) throws SQLException {
        if (resultSetMetaData == null) {
            resultSetMetaData = resultSet.getMetaData();
        }
        if (columnLabels == null) {
            columnLabels = new String[getColumnCount()];
            for (int i = 1; i <= getColumnCount(); i++) {
                columnLabels[i - 1] = resultSetMetaData.getColumnLabel(i);
            }
        }
        return columnLabels[index - 1];
    }

    public String getColumnName(int index) throws SQLException {
        if (resultSetMetaData == null) {
            resultSetMetaData = resultSet.getMetaData();
        }
        if (columnNames == null) {
            columnNames = new String[getColumnCount()];
            for (int i = 1; i <= getColumnCount(); i++) {
                columnNames[i - 1] = resultSetMetaData.getColumnName(i);
            }
        }
        return columnNames[index - 1];
    }

    /**
     * 根据指定的键和类型从结果集中获取值
     *
     * @param key  用于定位数据的键（可能是列名或列索引）
     * @param type 期望返回的数据类型
     * @param <T>  泛型参数，表示期望返回的具体类型
     * @return 指定键对应的指定类型的值
     * @throws SQLException 如果在获取数据时发生数据库访问错误
     */
    public abstract <T> T get(Object key, Class<T> type) throws SQLException;
}
