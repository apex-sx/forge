package apexsx.forge.data.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.sql.JDBCType.*;

/**
 * ResultSetManager 负责管理不同数据类型的 ResultSet 提取器。
 * 它提供了根据 Java 类型和 JDBC 类型自动选择合适提取器的功能，
 * 并支持构建自定义的提取器映射。
 *
 * @author zhitron
 */
public class ResultSetManager {
    // 基础提取器，直接从 ResultSet 获取指定类型的数据，不考虑列类型
    public static final ResultSetExtractor<Boolean> BASE_GET_BOOLEAN = buildBaseResultSetValueExtractor(ResultSet::getBoolean, ResultSet::getBoolean);
    public static final ResultSetExtractor<Byte> BASE_GET_BYTE = buildBaseResultSetValueExtractor(ResultSet::getByte, ResultSet::getByte);
    public static final ResultSetExtractor<Short> BASE_GET_SHORT = buildBaseResultSetValueExtractor(ResultSet::getShort, ResultSet::getShort);
    public static final ResultSetExtractor<Integer> BASE_GET_INT = buildBaseResultSetValueExtractor(ResultSet::getInt, ResultSet::getInt);
    public static final ResultSetExtractor<Long> BASE_GET_LONG = buildBaseResultSetValueExtractor(ResultSet::getLong, ResultSet::getLong);
    public static final ResultSetExtractor<Float> BASE_GET_FLOAT = buildBaseResultSetValueExtractor(ResultSet::getFloat, ResultSet::getFloat);
    public static final ResultSetExtractor<Double> BASE_GET_DOUBLE = buildBaseResultSetValueExtractor(ResultSet::getDouble, ResultSet::getDouble);
    public static final ResultSetExtractor<BigDecimal> BASE_GET_BIG_DECIMAL = buildBaseResultSetValueExtractor(ResultSet::getBigDecimal, ResultSet::getBigDecimal);
    public static final ResultSetExtractor<String> BASE_GET_STRING = buildBaseResultSetValueExtractor(ResultSet::getString, ResultSet::getString);
    public static final ResultSetExtractor<String> BASE_GET_NSTRING = buildBaseResultSetValueExtractor(ResultSet::getNString, ResultSet::getNString);
    public static final ResultSetExtractor<Date> BASE_GET_DATE = buildBaseResultSetValueExtractor(ResultSet::getDate, ResultSet::getDate);
    public static final ResultSetExtractor<Time> BASE_GET_TIME = buildBaseResultSetValueExtractor(ResultSet::getTime, ResultSet::getTime);
    public static final ResultSetExtractor<Timestamp> BASE_GET_TIMESTAMP = buildBaseResultSetValueExtractor(ResultSet::getTimestamp, ResultSet::getTimestamp);
    public static final ResultSetExtractor<Array> BASE_GET_ARRAY = buildBaseResultSetValueExtractor(ResultSet::getArray, ResultSet::getArray);
    public static final ResultSetExtractor<Clob> BASE_GET_CLOB = buildBaseResultSetValueExtractor(ResultSet::getClob, ResultSet::getClob);
    public static final ResultSetExtractor<NClob> BASE_GET_NCLOB = buildBaseResultSetValueExtractor(ResultSet::getNClob, ResultSet::getNClob);
    public static final ResultSetExtractor<Reader> BASE_GET_CHARACTER_STREAM = buildBaseResultSetValueExtractor(ResultSet::getCharacterStream, ResultSet::getCharacterStream);
    public static final ResultSetExtractor<Reader> BASE_GET_NCHARACTER_STREAM = buildBaseResultSetValueExtractor(ResultSet::getNCharacterStream, ResultSet::getNCharacterStream);
    public static final ResultSetExtractor<InputStream> BASE_GET_ASCII_STREAM = buildBaseResultSetValueExtractor(ResultSet::getAsciiStream, ResultSet::getAsciiStream);
    public static final ResultSetExtractor<byte[]> BASE_GET_BYTES = buildBaseResultSetValueExtractor(ResultSet::getBytes, ResultSet::getBytes);
    public static final ResultSetExtractor<Blob> BASE_GET_BLOB = buildBaseResultSetValueExtractor(ResultSet::getBlob, ResultSet::getBlob);
    public static final ResultSetExtractor<InputStream> BASE_GET_BINARY_STREAM = buildBaseResultSetValueExtractor(ResultSet::getBinaryStream, ResultSet::getBinaryStream);
    public static final ResultSetExtractor<URL> BASE_GET_URL = buildBaseResultSetValueExtractor(ResultSet::getURL, ResultSet::getURL);
    public static final ResultSetExtractor<RowId> BASE_GET_ROW_ID = buildBaseResultSetValueExtractor(ResultSet::getRowId, ResultSet::getRowId);
    public static final ResultSetExtractor<Ref> BASE_GET_REF = buildBaseResultSetValueExtractor(ResultSet::getRef, ResultSet::getRef);
    public static final ResultSetExtractor<SQLXML> BASE_GET_SQLXML = buildBaseResultSetValueExtractor(ResultSet::getSQLXML, ResultSet::getSQLXML);
    public static final ResultSetExtractor<Object> BASE_GET_OBJECT = buildBaseResultSetValueExtractor(ResultSet::getObject, ResultSet::getObject);

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // 特定类型提取器，根据 JDBC 列类型选择合适的提取方法
    public static final ResultSetExtractor<Boolean> GET_BOOLEAN = buildSpecifyResultSetValueExtractor(BOOLEAN, BASE_GET_BOOLEAN);
    public static final ResultSetExtractor<Byte> GET_BYTE = buildSpecifyResultSetValueExtractor(TINYINT, SMALLINT, BASE_GET_BYTE);
    public static final ResultSetExtractor<Short> GET_SHORT = buildSpecifyResultSetValueExtractor(TINYINT, SMALLINT, INTEGER, BASE_GET_SHORT);
    public static final ResultSetExtractor<Integer> GET_INT = buildSpecifyResultSetValueExtractor(TINYINT, SMALLINT, INTEGER, BIGINT, BASE_GET_INT);
    public static final ResultSetExtractor<Long> GET_LONG = buildSpecifyResultSetValueExtractor(TINYINT, SMALLINT, INTEGER, BIGINT, REAL, BASE_GET_LONG);
    public static final ResultSetExtractor<Float> GET_FLOAT = buildSpecifyResultSetValueExtractor(TINYINT, SMALLINT, INTEGER, BIGINT, REAL, DOUBLE, BASE_GET_FLOAT);
    public static final ResultSetExtractor<Double> GET_DOUBLE = buildSpecifyResultSetValueExtractor(TINYINT, SMALLINT, INTEGER, BIGINT, REAL, DOUBLE, BASE_GET_DOUBLE);
    public static final ResultSetExtractor<BigDecimal> GET_BIG_DECIMAL = buildSpecifyResultSetValueExtractor(TINYINT, SMALLINT, INTEGER, BIGINT, REAL, DOUBLE, NUMERIC, DECIMAL, BASE_GET_BIG_DECIMAL);
    public static final ResultSetExtractor<String> GET_STRING = buildSpecifyResultSetValueExtractor(CHAR, VARCHAR, LONGVARCHAR, CLOB, SQLXML, BASE_GET_STRING, NCHAR, NVARCHAR, LONGNVARCHAR, NCLOB, BASE_GET_NSTRING, BASE_GET_STRING);
    public static final ResultSetExtractor<Date> GET_DATE = buildSpecifyResultSetValueExtractor(DATE, TIMESTAMP, TIME_WITH_TIMEZONE, BASE_GET_DATE);
    public static final ResultSetExtractor<Time> GET_TIME = buildSpecifyResultSetValueExtractor(TIME, TIME_WITH_TIMEZONE, TIMESTAMP, TIMESTAMP_WITH_TIMEZONE, BASE_GET_TIME);
    public static final ResultSetExtractor<Timestamp> GET_TIMESTAMP = buildSpecifyResultSetValueExtractor(TIMESTAMP, TIMESTAMP_WITH_TIMEZONE, BASE_GET_TIMESTAMP);
    public static final ResultSetExtractor<Clob> GET_CLOB = buildSpecifyResultSetValueExtractor(CHAR, VARCHAR, LONGVARCHAR, CLOB, BASE_GET_CLOB, NCHAR, NVARCHAR, LONGNVARCHAR, NCLOB, BASE_GET_NCLOB);
    public static final ResultSetExtractor<NClob> GET_NCLOB = buildSpecifyResultSetValueExtractor(NCHAR, NVARCHAR, LONGNVARCHAR, NCLOB, BASE_GET_NCLOB);
    public static final ResultSetExtractor<Reader> GET_READER = buildSpecifyResultSetValueExtractor(CHAR, VARCHAR, LONGVARCHAR, CLOB, BASE_GET_CHARACTER_STREAM, NCLOB, NCHAR, NVARCHAR, LONGNVARCHAR, NCLOB, BASE_GET_NCHARACTER_STREAM);
    public static final ResultSetExtractor<byte[]> GET_BYTES = buildSpecifyResultSetValueExtractor(BINARY, VARBINARY, LONGVARBINARY, BLOB, BASE_GET_BYTES);
    public static final ResultSetExtractor<Blob> GET_BLOB = buildSpecifyResultSetValueExtractor(BINARY, VARBINARY, LONGVARBINARY, BLOB, BASE_GET_BLOB);
    public static final ResultSetExtractor<InputStream> GET_INPUT_STREAM = buildSpecifyResultSetValueExtractor(BINARY, VARBINARY, LONGVARBINARY, BLOB, BASE_GET_BINARY_STREAM, CHAR, VARCHAR, LONGVARCHAR, CLOB, BASE_GET_ASCII_STREAM);
    public static final ResultSetExtractor<URL> GET_URL = buildSpecifyResultSetValueExtractor(DATALINK, BASE_GET_URL);
    public static final ResultSetExtractor<Array> GET_ARRAY = buildSpecifyResultSetValueExtractor(ARRAY, BASE_GET_ARRAY);
    public static final ResultSetExtractor<RowId> GET_ROW_ID = buildSpecifyResultSetValueExtractor(ROWID, BASE_GET_ROW_ID);
    public static final ResultSetExtractor<Ref> GET_REF = buildSpecifyResultSetValueExtractor(REF, BASE_GET_REF);
    public static final ResultSetExtractor<SQLXML> GET_SQLXML = buildSpecifyResultSetValueExtractor(SQLXML, BASE_GET_SQLXML);
    public static final ResultSetExtractor<Object> GET_OBJECT = BASE_GET_OBJECT;

    // 默认的 ResultSetExtractorManager 实例，包含了所有标准类型的提取器
    public static final ResultSetManager DEFAULT = ResultSetManager.builder()
            .add(Boolean.class, ResultSetManager.GET_BOOLEAN)
            .add(Byte.class, ResultSetManager.GET_BYTE)
            .add(Short.class, ResultSetManager.GET_SHORT)
            .add(Integer.class, ResultSetManager.GET_INT)
            .add(Long.class, ResultSetManager.GET_LONG)
            .add(Float.class, ResultSetManager.GET_FLOAT)
            .add(Double.class, ResultSetManager.GET_DOUBLE)
            .add(String.class, ResultSetManager.GET_STRING)
            .add(BigDecimal.class, ResultSetManager.GET_BIG_DECIMAL)
            .add(Date.class, ResultSetManager.GET_DATE)
            .add(Time.class, ResultSetManager.GET_TIME)
            .add(Timestamp.class, ResultSetManager.GET_TIMESTAMP)
            .add(Clob.class, ResultSetManager.GET_CLOB)
            .add(NClob.class, ResultSetManager.BASE_GET_NCLOB)
            .add(Reader.class, ResultSetManager.GET_READER)
            .add(byte[].class, ResultSetManager.GET_BYTES)
            .add(Blob.class, ResultSetManager.GET_BLOB)
            .add(InputStream.class, ResultSetManager.GET_INPUT_STREAM)
            .add(Array.class, ResultSetManager.GET_ARRAY)
            .add(URL.class, ResultSetManager.GET_URL)
            .add(RowId.class, ResultSetManager.GET_ROW_ID)
            .add(Ref.class, ResultSetManager.GET_REF)
            .add(SQLXML.class, ResultSetManager.GET_SQLXML)
            .add(Object.class, ResultSetManager.GET_OBJECT)
            .add(null, ResultSetManager.GET_OBJECT)
            .build();

    // 存储类型到提取器映射的不可变Map
    private final Map<Class<?>, ResultSetExtractor<?>> mapping;

    /**
     * 私有构造函数，创建一个 ResultSetExtractorManager 实例
     *
     * @param mapping 类型到提取器的映射关系
     */
    private ResultSetManager(Map<Class<?>, ResultSetExtractor<?>> mapping) {
        this.mapping = Collections.unmodifiableMap(mapping);
    }

    /**
     * 构建基础的 ResultSet 提取器
     *
     * @param dataGetterByIndex 通过列索引获取数据的函数
     * @param dataGetterByLabel 通过列名获取数据的函数
     * @param <T>               数据类型
     * @return ResultSetExtractor 实例
     */
    private static <T> ResultSetExtractor<T> buildBaseResultSetValueExtractor(ByIndex<T> dataGetterByIndex, ByLabel<T> dataGetterByLabel) {
        return (resultSet, key) -> {
            // 根据 key 是整数还是字符串选择不同的获取方法
            T data = key instanceof Integer ? dataGetterByIndex.apply(resultSet, (Integer) key) : dataGetterByLabel.apply(resultSet, key.toString());
            // 检查值是否为 SQL NULL
            if (resultSet.wasNull()) {
                return null;
            }
            return data;
        };
    }

    /**
     * 构建特定类型的 ResultSet 提取器
     *
     * @param args 参数列表，交替包含 JDBCType 和对应的 ResultSetExtractor
     * @param <T>  数据类型
     * @return ResultSetExtractor 实例
     */
    @SuppressWarnings("unchecked")
    private static <T> ResultSetExtractor<T> buildSpecifyResultSetValueExtractor(Object... args) {
        return (resultSet, key) -> {

            // 确定列索引并获取列的 JDBC 类型
            int columnIndex = key instanceof Integer ? (Integer) key : resultSet.findColumn(key.toString());
            // 获取列的 JDBC 类型
            JDBCType columnJDBCType = valueOf(resultSet.getMetaData().getColumnType(columnIndex));

            int typeCount = 0;
            // 遍历参数查找匹配的 JDBCType 和对应的提取器
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof JDBCType) {
                    typeCount++;
                    if (columnJDBCType == args[i]) {
                        // 找到匹配的类型后，继续查找对应的提取器
                        while (++i < args.length) {
                            if (args[i] instanceof ResultSetExtractor) {
                                // 使用找到的提取器提取数据
                                return ((ResultSetExtractor<T>) args[i]).extract(resultSet, key);
                            }
                        }
                    }
                } else {
                    if (i == args.length - 1) {
                        if (typeCount == 0) {
                            return ((ResultSetExtractor<T>) args[i]).extract(resultSet, key);
                        }
                    } else {
                        typeCount = 0;
                    }
                }
            }
            // 没有找到匹配的提取器
            throw new SQLException("No matching extractor found for column type: " + columnJDBCType);
        };
    }


    /**
     * 创建一个新的 Builder 实例
     *
     * @return Builder 实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 基于现有 ResultSetExtractorManager 创建一个新的 Builder 实例
     *
     * @param data 现有的 ResultSetExtractorManager 实例
     * @return Builder 实例
     */
    public static Builder builder(ResultSetManager data) {
        return new Builder(data);
    }

    /**
     * 创建 ResultSetTypedExtractor 实例
     *
     * @param resultSet ResultSet 对象
     * @return ResultSetTypedExtractor 实例
     * @throws SQLException 当 ResultSet 为 null 时抛出异常
     */
    public ResultSetRow createResultSetRow(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            throw new SQLException("ResultSet is null");
        }

        return new ResultSetRowImpl(resultSet);
    }

    /**
     * ResultSetTypedExtractor 的具体实现类
     */
    @SuppressWarnings("unchecked")
    private class ResultSetRowImpl extends ResultSetRow {

        /**
         * 构造函数
         *
         * @param resultSet ResultSet 对象
         */
        public ResultSetRowImpl(ResultSet resultSet) {
            super(resultSet);
        }

        /**
         * 根据键和类型获取值
         *
         * @param key  键（可以是列索引或列名）
         * @param type 目标类型
         * @param <T>  泛型类型
         * @return 提取的值
         * @throws SQLException 当类型为 null 或找不到对应提取器时抛出异常
         */
        @Override
        public <T> T get(Object key, Class<T> type) throws SQLException {
            if (type == null) {
                throw new SQLException("Type parameter is null");
            }

            // 根据类型查找对应的提取器
            ResultSetExtractor<T> resultSetExtractor = (ResultSetExtractor<T>) mapping.get(type);
            if (resultSetExtractor == null) {
                throw new SQLException("No ResultSetValueExtractor found for type: " + type.getName());
            }

            try {
                ResultSet resultSet = resultSet();
                // 如果键为 null，默认使用第一列
                if (key == null) {
                    return resultSetExtractor.extract(resultSet, 1);
                } else {
                    return resultSetExtractor.extract(resultSet, key);
                }
            } catch (Exception e) {
                throw new SQLException("Failed to extract value for key: " + key + ", type: " + type.getName(), e);
            }
        }
    }

    /**
     * Builder 模式，用于构建自定义的 ResultSetExtractorManager
     * <p>
     * 该内部类提供了灵活的方式来创建 ResultSetManager 实例，允许用户添加自定义的类型映射关系。
     * 支持两种构造方式：
     * 1. 创建全新的空映射
     * 2. 基于现有的 ResultSetManager 实例进行扩展
     * <p>
     * 提供了 add 方法用于注册新的类型和对应的提取器，以及 build 方法用于最终构建 ResultSetManager 实例。
     */
    public static class Builder {

        /**
         * 存储类型到提取器映射的可变Map
         * 用于在构建过程中临时存储各种类型及其对应的 ResultSetExtractor
         */
        private final Map<Class<?>, ResultSetExtractor<?>> mapping;

        /**
         * 默认构造函数
         */
        private Builder() {
            this.mapping = new HashMap<>();
        }


        /**
         * 基于现有 ResultSetExtractorManager 的构造函数
         *
         * @param resultSetManager 现有的 ResultSetExtractorManager 实例
         */
        private Builder(ResultSetManager resultSetManager) {
            this.mapping = new HashMap<>(resultSetManager.mapping.size() + 10, 1);
            this.mapping.putAll(resultSetManager.mapping);
        }

        /**
         * 添加类型和对应的提取器
         *
         * @param type               Java 类型
         * @param resultSetExtractor 对应的提取器
         * @param <T>                泛型类型
         * @return Builder 实例
         */
        public <T> Builder add(Class<T> type, ResultSetExtractor<T> resultSetExtractor) {
            if (resultSetExtractor != null) {
                this.mapping.put(type, resultSetExtractor);
            }
            return this;
        }

        /**
         * 构建 ResultSetExtractorManager 实例
         *
         * @return ResultSetExtractorManager 实例
         */
        public ResultSetManager build() {
            return new ResultSetManager(mapping);
        }
    }


    interface ByIndex<T> {

        T apply(ResultSet resultSet,int index)throws SQLException;
    }

    interface ByLabel<T> {

        T apply(ResultSet resultSet,String label)throws SQLException;
    }
}
