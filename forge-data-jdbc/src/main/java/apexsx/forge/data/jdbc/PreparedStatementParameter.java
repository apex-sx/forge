package apexsx.forge.data.jdbc;


import apexsx.forge.core.Logger;

import java.sql.JDBCType;
import java.sql.SQLType;

/**
 * PreparedStatement参数封装类
 * 用于封装预编译SQL语句中的参数值和对应的SQL类型
 *
 * @author zhitron
 */
public class PreparedStatementParameter {
    /**
     * 参数值
     */
    private final Object parameterValue;

    /**
     * 参数的SQL类型
     */
    private final SQLType parameterSQLType;

    /**
     * 构造函数 - 使用SQLType类型参数
     *
     * @param parameterValue   参数值
     * @param parameterSQLType 参数的SQL类型
     */
    public PreparedStatementParameter(Object parameterValue, SQLType parameterSQLType) {
        this.parameterValue = parameterValue;
        this.parameterSQLType = parameterSQLType;
    }

    /**
     * 构造函数 - 使用int类型参数（JDBC类型代码）
     *
     * @param parameterValue   参数值
     * @param parameterSQLType 参数的JDBC类型代码
     */
    public PreparedStatementParameter(Object parameterValue, int parameterSQLType) {
        this.parameterValue = parameterValue;
        this.parameterSQLType = JDBCType.valueOf(parameterSQLType);
    }

    /**
     * 构造函数 - 使用String类型参数（JDBC类型名称）
     *
     * @param parameterValue   参数值
     * @param parameterSQLType 参数的JDBC类型名称
     */
    public PreparedStatementParameter(Object parameterValue, String parameterSQLType) {
        this.parameterValue = parameterValue;
        // 处理String类型的参数（JDBC类型名称）
        JDBCType jdbcType = null;
        try {
            jdbcType = JDBCType.valueOf(parameterSQLType);
        } catch (IllegalArgumentException e) {
            if (Logger.isEnabledWarn()) {
                Logger.warn("Failed convert to JDBC type: %s", parameterSQLType);
            }
        }
        this.parameterSQLType = jdbcType;
    }

    /**
     * 获取参数值
     *
     * @return 参数值
     */
    public Object getParameterValue() {
        return parameterValue;
    }

    /**
     * 获取参数的SQL类型
     *
     * @return 参数的SQL类型
     */
    public SQLType getParameterSQLType() {
        return parameterSQLType;
    }
}
