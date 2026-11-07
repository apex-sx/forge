package apexsx.forge.data.jdbc;

import apexsx.forge.core.Logger;

import java.sql.*;

/**
 * @author zhitron
 */
public class JdbcUtils {

    private JdbcUtils() {

    }

    /**
     * 静默关闭 ResultSet 资源
     *
     * <p>安全地尝试关闭 ResultSet 资源，即使发生异常也不会抛出到调用方。
     * 如果资源为 null 则直接返回 false，否则尝试关闭资源并在成功时返回 true，
     * 发生异常时记录错误日志并返回 false。</p>
     *
     * @param resource 要关闭的 ResultSet 资源
     * @return 成功关闭返回 true，资源为 null 或关闭失败返回 false
     */
    public static boolean closeQuietly(ResultSet resource) {
        if (resource != null) {
            try {
                resource.close();
                return true;
            } catch (Exception e) {
                Logger.error("Error to close ResultSet!", e);
                return false;
            }
        }
        return false;
    }

    /**
     * 静默关闭 Statement 资源
     *
     * <p>安全地尝试关闭 Statement 资源，即使发生异常也不会抛出到调用方。
     * 如果资源为 null 则直接返回 false，否则尝试关闭资源并在成功时返回 true，
     * 发生异常时记录错误日志并返回 false。</p>
     *
     * @param resource 要关闭的 Statement 资源
     * @return 成功关闭返回 true，资源为 null 或关闭失败返回 false
     */
    public static boolean closeQuietly(Statement resource) {
        if (resource != null) {
            try {
                resource.close();
                return true;
            } catch (Exception e) {
                Logger.error("Error to close Statement!", e);
                return false;
            }
        }
        return false;
    }

    /**
     * 静默关闭 PreparedStatement 资源
     *
     * <p>安全地尝试关闭 PreparedStatement 资源，即使发生异常也不会抛出到调用方。
     * 如果资源为 null 则直接返回 false，否则尝试关闭资源并在成功时返回 true，
     * 发生异常时记录错误日志并返回 false。</p>
     *
     * @param resource 要关闭的 PreparedStatement 资源
     * @return 成功关闭返回 true，资源为 null 或关闭失败返回 false
     */
    public static boolean closeQuietly(PreparedStatement resource) {
        if (resource != null) {
            try {
                resource.close();
                return true;
            } catch (Exception e) {
                Logger.error("Error to close PreparedStatement!", e);
                return false;
            }
        }
        return false;
    }

}
