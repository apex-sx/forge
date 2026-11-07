package apexsx.forge.util.converter.table;

import com.github.zhitron.type_converter.TypeConverter;
import com.github.zhitron.type_converter.TypeConverterException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ResultSet到Map数组的类型转换器
 * <p>
 * 该转换器将JDBC ResultSet转换为Map数组，每个Map代表一行数据。
 * 对于每一行数据，使用列索引、列名和列标签作为键存储值，
 * 其中列名和列标签会转换为小写形式。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ResultSetToMapTableTypeConverter extends TypeConverter<ResultSet, Map[]> {
    /**
     * 转换器的单例实例
     */
    public static final ResultSetToMapTableTypeConverter INSTANCE = new ResultSetToMapTableTypeConverter();

    /**
     * 构造函数，初始化目标类型为Map数组
     * 使用protected修饰符防止外部直接实例化
     */
    protected ResultSetToMapTableTypeConverter() {
        super(Map[].class);
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
     * 遍历ResultSet中的每一行数据，将每行的列值以多种键形式存储到Map中：
     * 1. 列索引（从1开始）
     * 2. 列名（转换为小写）
     * 3. 列标签（转换为小写，当列名和列标签不同时）
     *
     * @param source 源对象，必须是ResultSet类型
     * @return 转换后的Map[]对象，包含ResultSet中的所有数据
     * @throws TypeConverterException 当ResultSet元数据为空或遍历过程中发生SQL异常时抛出
     * @throws SQLException           当访问ResultSet过程中发生SQL错误时抛出
     * @throws NullPointerException   当source为null时抛出
     */
    @Override
    public Map[] convertsUnchecked(ResultSet source) throws Throwable {
        ResultSetMetaData metaData = source.getMetaData();
        if (metaData == null) {
            throw new TypeConverterException("ResultSet metadata is unexpectedly null");
        }
        int columnCount = metaData.getColumnCount();
        // 使用List动态收集行数据以避免预分配固定大小数组
        List<Map> rows = new ArrayList<>();
        try {
            while (source.next()) {
                Map row = new HashMap((int) (columnCount / 0.75f + 1));
                for (int j = 0; j < columnCount; j++) {
                    // 列索引从1开始（JDBC规范）
                    int columnIndex = j + 1;
                    String columnName = metaData.getColumnName(columnIndex);
                    String columnLabel = metaData.getColumnLabel(columnIndex);
                    Object value = source.getObject(columnIndex);
                    row.put(columnIndex, value);
                    if (columnName != null) {
                        columnName = columnName.toLowerCase();
                        row.put(columnName, value);
                        if (columnLabel != null && !columnName.equalsIgnoreCase(columnLabel)) {
                            columnLabel = columnLabel.toLowerCase();
                            row.put(columnLabel, value);
                        }
                    } else if (columnLabel != null) {
                        columnLabel = columnLabel.toLowerCase();
                        row.put(columnLabel, value);
                    }
                }
                rows.add(row);
            }
        } catch (SQLException e) {
            throw new TypeConverterException("Error occurred during ResultSet iteration", e);
        }
        return rows.toArray(new Map[0]);
    }
}
