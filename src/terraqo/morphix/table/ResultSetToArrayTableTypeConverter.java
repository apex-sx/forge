/*
 * Copyright (c) 2026.
 */

package terraqo.morphix.table;

import terraqo.morphix.TypeConverter;
import terraqo.morphix.TypeConverterException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ResultSet到二维数组的类型转换器
 * <p>
 * 该转换器用于将JDBC的ResultSet对象转换为Object[][]类型的二维数组，
 * 其中第一维表示行，第二维表示列。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author Terraqo
 */
public class ResultSetToArrayTableTypeConverter extends TypeConverter<ResultSet, Object[][]> {
    /**
     * 转换器的单例实例
     */
    public static final ResultSetToArrayTableTypeConverter INSTANCE = new ResultSetToArrayTableTypeConverter();

    /**
     * 构造函数，初始化目标类型为Object[][]
     * 使用protected修饰符防止外部直接实例化
     */
    protected ResultSetToArrayTableTypeConverter() {
        super(Object[][].class);
    }

    /**
     * 判断指定的源对象是否支持转换
     * 仅当源对象是ResultSet类型时才支持转换
     *
     * @param source 源对象
     * @return 如果源对象是ResultSet类型返回true，否则返回false
     */
    @Override
    public boolean isSupportsSource(Object source) {
        return source instanceof ResultSet;
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 遍历ResultSet中的每一行数据，将每行的列值存储到Object数组中，
     * 最终返回包含所有行数据的二维数组
     *
     * @param source 源对象，必须是ResultSet类型
     * @return 转换后的Object[][]对象，包含ResultSet中的所有数据
     * @throws TypeConverterException 当ResultSet元数据为空或遍历过程中发生SQL异常时抛出
     * @throws SQLException           当访问ResultSet过程中发生SQL错误时抛出
     * @throws NullPointerException   当source为null时抛出
     */
    @Override
    public Object[][] convertsUnchecked(ResultSet source) throws Throwable {
        ResultSetMetaData metadata = source.getMetaData();
        if (metadata == null) {
            throw new TypeConverterException("ResultSet Metadata is unexpectedly null");
        }
        int columnCount = metadata.getColumnCount();
        // 使用List动态收集行数据以避免预分配固定大小数组
        List<Object[]> rows = new ArrayList<>();
        try {
            while (source.next()) {
                Object[] row = new Object[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    // 列索引从1开始（JDBC规范）
                    row[j] = source.getObject(j + 1);
                }
                rows.add(row);
            }
        } catch (SQLException e) {
            throw new TypeConverterException("Error occurred during ResultSet iteration", e);
        }
        return rows.toArray(new Object[0][0]);
    }
}
