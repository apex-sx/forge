/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import com.github.potatoxf.generals.Kit;
import com.github.potatoxf.generals.utils.jdbc.PreparedStatementSetter;
import com.github.potatoxf.generals.utils.jdbc.ResultSetGetter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * jdbc工具类
 *
 * @author potatoxf
 */
@SuppressWarnings("unused")
public interface GeneralJDBC {
    Set<Class<?>> SUPPORT_TYPE = Kit.ofUnmodifiableSet(
            Boolean.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, BigDecimal.class, BigInteger.class,
            String.class, byte[].class, java.util.Date.class, java.sql.Date.class, java.sql.Time.class, java.sql.Timestamp.class, java.net.URL.class
    );
    Set<Class<?>> SUPPORT_IMPLEMENTS_TYPE = Kit.ofUnmodifiableSet(
            java.io.InputStream.class, java.io.Reader.class, java.sql.Blob.class, java.sql.Clob.class, java.sql.NClob.class,
            java.sql.Array.class, java.sql.Ref.class, java.sql.RowId.class, java.sql.SQLXML.class
    );
    SortedMap<Integer, ResultSetGetter> RESULT_SET_GETTER = GeneralJDBC.initResultSetGetter(null);
    SortedMap<Class<?>, PreparedStatementSetter> PREPARED_STATEMENT_SETTER = GeneralJDBC.initPreparedStatementSetter(null);

    /**
     * 初始化{@link ResultSetGetter}类型映射器
     *
     * @param setting 自定义配置器
     * @return {@code Map<Integer, ResultSetValueGetter>}
     */
    static SortedMap<Integer, ResultSetGetter> initResultSetGetter(Consumer<SortedMap<Integer, ResultSetGetter>> setting) {
        return Kit.initOrUpdateSortedMap(RESULT_SET_GETTER, setting, map -> {
            map.put(Types.BIT, ResultSet::getByte);
            map.put(Types.TINYINT, ResultSet::getByte);
            map.put(Types.SMALLINT, ResultSet::getShort);
            map.put(Types.INTEGER, ResultSet::getInt);
            map.put(Types.BIGINT, ResultSet::getLong);
            map.put(Types.FLOAT, ResultSet::getFloat);
            map.put(Types.REAL, ResultSet::getObject);
            map.put(Types.DOUBLE, ResultSet::getDouble);
            map.put(Types.NUMERIC, ResultSet::getBigDecimal);
            map.put(Types.DECIMAL, ResultSet::getBigDecimal);
            map.put(Types.CHAR, ResultSet::getString);
            map.put(Types.VARCHAR, ResultSet::getString);
            map.put(Types.LONGVARCHAR, ResultSet::getString);
            map.put(Types.DATE, ResultSet::getDate);
            map.put(Types.TIME, ResultSet::getTime);
            map.put(Types.TIMESTAMP, ResultSet::getTimestamp);
            map.put(Types.BINARY, ResultSet::getBinaryStream);
            map.put(Types.VARBINARY, ResultSet::getBinaryStream);
            map.put(Types.LONGVARBINARY, ResultSet::getBinaryStream);
            map.put(Types.NULL, ResultSet::getObject);
            map.put(Types.OTHER, ResultSet::getObject);
            map.put(Types.JAVA_OBJECT, ResultSet::getObject);
            map.put(Types.DISTINCT, ResultSet::getObject);
            map.put(Types.STRUCT, ResultSet::getObject);
            map.put(Types.ARRAY, ResultSet::getArray);
            map.put(Types.BLOB, ResultSet::getBlob);
            map.put(Types.CLOB, ResultSet::getClob);
            map.put(Types.REF, ResultSet::getRef);
            map.put(Types.DATALINK, ResultSet::getObject);
            map.put(Types.BOOLEAN, ResultSet::getBoolean);
            map.put(Types.ROWID, ResultSet::getRowId);
            map.put(Types.NCHAR, ResultSet::getString);
            map.put(Types.NVARCHAR, ResultSet::getString);
            map.put(Types.LONGNVARCHAR, ResultSet::getString);
            map.put(Types.NCLOB, ResultSet::getNClob);
            map.put(Types.SQLXML, ResultSet::getSQLXML);
            map.put(Types.REF_CURSOR, ResultSet::getObject);
            map.put(Types.TIME_WITH_TIMEZONE, ResultSet::getTime);
            map.put(Types.TIMESTAMP_WITH_TIMEZONE, ResultSet::getTimestamp);
        }, Integer::compareTo);
    }

    /**
     * 初始化{@link PreparedStatementSetter}类型映射器
     *
     * @param setting 自定义配置器
     * @return {@code Map<Class<?>, PreparedStatementSetter>}
     */
    static SortedMap<Class<?>, PreparedStatementSetter> initPreparedStatementSetter(Consumer<SortedMap<Class<?>, PreparedStatementSetter>> setting) {
        return Kit.initOrUpdateSortedMap(PREPARED_STATEMENT_SETTER, setting, map -> {
            map.put(Boolean.class, (ps, i, v) -> ps.setBoolean(i, (Boolean) v));
            map.put(Byte.class, (ps, i, v) -> ps.setByte(i, (Byte) v));
            map.put(Short.class, (ps, i, v) -> ps.setShort(i, (Short) v));
            map.put(Integer.class, (ps, i, v) -> ps.setInt(i, (Integer) v));
            map.put(Long.class, (ps, i, v) -> ps.setLong(i, (Long) v));
            map.put(Float.class, (ps, i, v) -> ps.setFloat(i, (Float) v));
            map.put(Double.class, (ps, i, v) -> ps.setDouble(i, (Double) v));
            map.put(BigDecimal.class, (ps, i, v) -> ps.setBigDecimal(i, (BigDecimal) v));
            map.put(BigInteger.class, (ps, i, v) -> ps.setBigDecimal(i, new BigDecimal(v.toString())));
            map.put(String.class, (ps, i, v) -> ps.setString(i, (String) v));
            map.put(byte[].class, (ps, i, v) -> ps.setBytes(i, (byte[]) v));
            map.put(java.util.Date.class, (ps, i, v) -> ps.setDate(i, new java.sql.Date(((Date) v).getTime())));
            map.put(java.sql.Date.class, (ps, i, v) -> ps.setDate(i, (java.sql.Date) v));
            map.put(java.sql.Time.class, (ps, i, v) -> ps.setTime(i, (java.sql.Time) v));
            map.put(java.sql.Timestamp.class, (ps, i, v) -> ps.setTimestamp(i, (java.sql.Timestamp) v));
            map.put(java.net.URL.class, (ps, i, v) -> ps.setURL(i, (java.net.URL) v));
            map.put(java.io.InputStream.class, (ps, i, v) -> ps.setBinaryStream(i, (java.io.InputStream) v));
            map.put(java.io.Reader.class, (ps, i, v) -> ps.setNClob(i, (java.io.Reader) v));
            map.put(java.sql.Blob.class, (ps, i, v) -> ps.setBlob(i, (java.sql.Blob) v));
            map.put(java.sql.Clob.class, (ps, i, v) -> ps.setClob(i, (java.sql.Clob) v));
            map.put(java.sql.NClob.class, (ps, i, v) -> ps.setNClob(i, (java.sql.NClob) v));
            map.put(java.sql.Array.class, (ps, i, v) -> ps.setArray(i, (java.sql.Array) v));
            map.put(java.sql.Ref.class, (ps, i, v) -> ps.setRef(i, (java.sql.Ref) v));
            map.put(java.sql.RowId.class, (ps, i, v) -> ps.setRowId(i, (java.sql.RowId) v));
            map.put(java.sql.SQLXML.class, (ps, i, v) -> ps.setSQLXML(i, (java.sql.SQLXML) v));
        }, Comparator.comparing(Class::getName));
    }

    /**
     * 获取{@link ResultSet}的值
     *
     * @param resultSet   {@link ResultSet}
     * @param columnIndex 列索引
     * @return 返回值
     * @throws SQLException 如果发生错误
     */
    static Object getValue(ResultSet resultSet, int columnIndex) throws SQLException {
        return GeneralJDBC.getValue(resultSet, columnIndex, RESULT_SET_GETTER, null, null);
    }

    /**
     * 获取{@link ResultSet}的值
     *
     * @param resultSet   {@link ResultSet}
     * @param columnIndex 列索引
     * @param typeMap     {@link Types}类型值映射结果集值获取器
     * @return 返回值
     * @throws SQLException 如果发生错误
     */
    static Object getValue(ResultSet resultSet, int columnIndex,
                           Map<Integer, ResultSetGetter> typeMap) throws SQLException {
        return GeneralJDBC.getValue(resultSet, columnIndex, typeMap, null, null);
    }

    /**
     * 获取{@link ResultSet}的值
     *
     * @param resultSet    {@link ResultSet}
     * @param columnIndex  列索引
     * @param typeMap      {@link Types}类型值映射结果集值获取器
     * @param classNameMap {@link Class}类型名映射结果集值获取器
     * @return 返回值
     * @throws SQLException 如果发生错误
     */
    static Object getValue(ResultSet resultSet, int columnIndex,
                           Map<Integer, ResultSetGetter> typeMap,
                           Map<String, ResultSetGetter> classNameMap) throws SQLException {
        return GeneralJDBC.getValue(resultSet, columnIndex, typeMap, classNameMap, null);
    }

    /**
     * 获取{@link ResultSet}的值
     *
     * @param resultSet    {@link ResultSet}
     * @param columnIndex  列索引
     * @param typeMap      {@link Types}类型值映射结果集值获取器
     * @param classNameMap {@link Class}类型名映射结果集值获取器
     * @param typeNameMap  {@link String}类型名映射结果集值获取器
     * @return 返回值
     * @throws SQLException 如果发生错误
     */
    static Object getValue(ResultSet resultSet, int columnIndex,
                           Map<Integer, ResultSetGetter> typeMap,
                           Map<String, ResultSetGetter> classNameMap,
                           Map<String, ResultSetGetter> typeNameMap) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnType = metaData.getColumnType(columnIndex);
        Object object;
        if (typeMap != null && typeMap.containsKey(columnType)) {
            object = typeMap.get(columnType).apply(resultSet, columnIndex);
        } else {
            String columnClassName = metaData.getColumnClassName(columnIndex);
            if (columnClassName != null && classNameMap.containsKey(columnClassName)) {
                object = classNameMap.get(columnClassName).apply(resultSet, columnIndex);
            } else {
                String columnTypeName = metaData.getColumnTypeName(columnIndex);
                if (columnTypeName != null && typeNameMap.containsKey(columnTypeName)) {
                    object = typeNameMap.get(columnTypeName).apply(resultSet, columnIndex);
                } else {
                    object = resultSet.getObject(columnIndex);
                }
            }
        }
        if (resultSet.wasNull()) {
            object = null;
        }
        return object;
    }


    /**
     * 设置{@link PreparedStatement}参数值
     *
     * @param preparedStatement {@link PreparedStatement}
     * @param parameterIndex    参数索引
     * @param value             要设置的值
     * @throws SQLException 如果发生错误
     */
    static void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value) throws SQLException {
        GeneralJDBC.setValue(preparedStatement, parameterIndex, value, PREPARED_STATEMENT_SETTER);
    }

    /**
     * 设置{@link PreparedStatement}参数值
     *
     * @param preparedStatement {@link PreparedStatement}
     * @param parameterIndex    参数索引
     * @param value             要设置的值
     * @param typeMap           {@link Class}类型值映射句柄设置器
     * @throws SQLException 如果发生错误
     */
    static void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value,
                         Map<Class<?>, PreparedStatementSetter> typeMap) throws SQLException {
        Class<?> type = GeneralJDBC.jdbcType(value);
        if (typeMap != null && typeMap.containsKey(type)) {
            typeMap.get(type).apply(preparedStatement, parameterIndex, value);
        } else {
            throw new IllegalArgumentException("This type " + type + " is not supported by JDBC PreparedStatement");
        }
    }

    static Class<?> jdbcType(Object value) {
        if (value == null) throw new IllegalArgumentException("Empty input values are not allowed");
        Class<?> type = value instanceof Class ? (Class<?>) value : value.getClass();
        if (SUPPORT_TYPE.contains(type)) return type;
        if (SUPPORT_IMPLEMENTS_TYPE.contains(type)) {
            for (Class<?> supportImplementsType : SUPPORT_IMPLEMENTS_TYPE) {
                if (supportImplementsType.isAssignableFrom(type)) {
                    return supportImplementsType;
                }
            }
        }
        throw new IllegalArgumentException("This type " + type + " is not supported by JDBC");
    }

}
