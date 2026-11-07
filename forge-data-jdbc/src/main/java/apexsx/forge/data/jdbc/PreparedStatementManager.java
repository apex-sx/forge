package apexsx.forge.data.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理PreparedStatement参数设置的映射器
 * 该类负责将Java对象类型映射到对应的PreparedStatement参数设置方法
 * 支持使用默认映射器或构建自定义映射器
 *
 * @author zhitron
 */
public class PreparedStatementManager {
    /**
     * 预定义的默认映射器实例，包含常用Java数据类型与PreparedStatement方法的映射关系
     * 支持String、Integer、Long、Double等基本类型以及SQL特定类型如Date、Blob等
     */
    public static final PreparedStatementManager DEFAULT = PreparedStatementManager.builder()
            .add(String.class, PreparedStatement::setString)
            .add(Integer.class, PreparedStatement::setInt)
            .add(Long.class, PreparedStatement::setLong)
            .add(Double.class, PreparedStatement::setDouble)
            .add(Float.class, PreparedStatement::setFloat)
            .add(Boolean.class, PreparedStatement::setBoolean)
            .add(Byte.class, PreparedStatement::setByte)
            .add(Short.class, PreparedStatement::setShort)
            .add(byte[].class, PreparedStatement::setBytes)
            .add(Date.class, PreparedStatement::setDate)
            .add(Time.class, PreparedStatement::setTime)
            .add(Timestamp.class, PreparedStatement::setTimestamp)
            .add(BigDecimal.class, PreparedStatement::setBigDecimal)
            .add(Blob.class, PreparedStatement::setBlob)
            .add(Reader.class, PreparedStatement::setCharacterStream)
            .add(InputStream.class, PreparedStatement::setBinaryStream)
            .add(Clob.class, PreparedStatement::setClob)
            .add(NClob.class, PreparedStatement::setNClob)
            .add(Array.class, PreparedStatement::setArray)
            .add(Ref.class, PreparedStatement::setRef)
            .add(URL.class, PreparedStatement::setURL)
            .add(SQLXML.class, PreparedStatement::setSQLXML)
            .add(Object.class, PreparedStatement::setObject)
            .add(null, PreparedStatement::setObject)
            .build();

    /**
     * 存储Class类型到PreparedStatementSetter映射关系的只读Map
     */
    private final Map<Class<?>, PreparedStatementSetter<?>> mapping;

    /**
     * 私有构造函数，用于创建不可变的PreparedStatementManager实例
     *
     * @param mapping 包含类型映射关系的Map
     */
    private PreparedStatementManager(Map<Class<?>, PreparedStatementSetter<?>> mapping) {
        this.mapping = Collections.unmodifiableMap(mapping);
    }

    /**
     * 获取一个新的Builder实例用于构建自定义的PreparedStatementManager
     *
     * @return 新的Builder实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 基于现有的PreparedStatementManager创建一个新的Builder实例
     *
     * @param data 用于初始化Builder的现有PreparedStatementManager实例
     * @return 新的Builder实例
     */
    public static Builder builder(PreparedStatementManager data) {
        return new Builder(data);
    }


    /**
     * 根据指定的Class类型获取对应的PreparedStatementSetter
     *
     * @param clazz 需要获取Setter的Class类型
     * @return 对应类型的PreparedStatementSetter，如果未找到则返回null
     */
    @SuppressWarnings("unchecked")
    public PreparedStatementSetter<Object> get(Class<?> clazz) {
        PreparedStatementSetter<?> preparedStatementSetter = mapping.get(clazz);
        if (preparedStatementSetter == null) {
            return null;
        }
        return (PreparedStatementSetter<Object>) preparedStatementSetter;
    }

    /**
     * 用于构建PreparedStatementManager实例的内部Builder类
     */
    public static class Builder {
        /**
         * 存储构建过程中Class类型与PreparedStatementSetter映射关系的可变Map
         */
        private final Map<Class<?>, PreparedStatementSetter<?>> mapping;

        /**
         * 创建一个空的Builder实例
         */
        private Builder() {
            this.mapping = new HashMap<>();
        }

        /**
         * 基于现有PreparedStatementManager创建新的Builder实例
         *
         * @param preparedStatementManager 用于初始化Builder的现有实例
         */
        private Builder(PreparedStatementManager preparedStatementManager) {
            this.mapping = new HashMap<>(preparedStatementManager.mapping.size() + 10, 1);
            this.mapping.putAll(preparedStatementManager.mapping);
        }

        /**
         * 添加指定Class类型与PreparedStatementSetter之间的映射关系
         *
         * @param <T>                     映射的类型参数
         * @param type                    需要映射的Java Class类型
         * @param preparedStatementSetter 对应该类型的PreparedStatementSetter
         * @return 当前Builder实例，支持链式调用
         */
        public <T> Builder add(Class<T> type, PreparedStatementSetter<T> preparedStatementSetter) {
            if (preparedStatementSetter != null) {
                mapping.put(type, preparedStatementSetter);
            }
            return this;
        }

        /**
         * 使用已添加的映射关系构建最终的PreparedStatementManager实例
         *
         * @return 不可变的PreparedStatementManager实例
         */
        public PreparedStatementManager build() {
            return new PreparedStatementManager(mapping);
        }
    }
}
