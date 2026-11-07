package apexsx.forge.data.jdbc;

import java.sql.SQLException;

/**
 *
 * @author ApexSX
 */
@FunctionalInterface
public interface JdbcFunction<S,T> {

    T apply(S source)throws SQLException;
}
