package potatoxf.helper.basic.db.sql.formatter.languages;

import potatoxf.helper.basic.db.sql.formatter.core.AbstractFormatter;
import potatoxf.helper.basic.db.sql.formatter.core.FormatConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public enum Dialect {
  Db2(Db2Formatter::new),
  MariaDb(MariaDbFormatter::new),
  MySql(MySqlFormatter::new),
  N1ql(N1qlFormatter::new),
  PlSql(PlSqlFormatter::new, "pl/sql"),
  PostgreSql(PostgreSqlFormatter::new),
  Redshift(RedshiftFormatter::new),
  SparkSql(SparkSqlFormatter::new, "spark"),
  StandardSql(StandardSqlFormatter::new, "sql"),
  TSql(TSqlFormatter::new),
  ;

  public final Function<FormatConfig, AbstractFormatter> func;
  public final List<String> aliases;

  Dialect(Function<FormatConfig, AbstractFormatter> func, String... aliases) {
    this.func = func;
    this.aliases = Arrays.asList(aliases);
  }

  public static Optional<Dialect> nameOf(String name) {
    return Arrays.stream(values())
        .filter(
            d ->
                d.name().equalsIgnoreCase(name)
                    || d.aliases.stream().anyMatch(s -> s.equalsIgnoreCase(name)))
        .findFirst();
  }
}
