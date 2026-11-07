package apexsx.forge.lang.meta;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * @author Terraqo
 */
@Getter
public enum Database {
    ORACLE("Oracle", ".*Oracle.*", db -> {
        db.registerColumnType(new ColumnType("CHAR", 2000));
        db.registerColumnType(new ColumnType("NCHAR", 2000));
        db.registerColumnType(new ColumnType("VARCHAR2", 4000));
        db.registerColumnType(new ColumnType("NVARCHAR2", 4000));
        db.registerColumnType(new ColumnType("NUMBER", 38, 12));
        db.registerColumnType(new ColumnType("DATE"));
        db.registerColumnType(new ColumnType("TIMESTAMP"));
        db.registerColumnType(new ColumnType("TIMESTAMP WITH TIME ZONE"));
        db.registerColumnType(new ColumnType("TIMESTAMP WITH LOCAL TIME ZONE"));
        db.registerColumnType(new ColumnType("INTERVAL YEAR TO MONTH"));
        db.registerColumnType(new ColumnType("INTERVAL DAY TO SECOND"));
        db.registerColumnType(new ColumnType("CLOB"));
        db.registerColumnType(new ColumnType("NCLOB"));
        db.registerColumnType(new ColumnType("BLOB"));
        db.registerColumnType(new ColumnType("BFILE"));
        db.registerColumnType(new ColumnType("RAW", 2000));
        DriverType allDriverType = new DriverType(db, "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@");
        db.registerVersionType(new VersionType(db, "19c?", "19", "\\d*", allDriverType, () -> db.columnTypes));
        db.registerVersionType(new VersionType(db, "18c?", "18", "\\d*", allDriverType, () -> db.columnTypes));
        db.registerVersionType(new VersionType(db, "12c?", "12", "\\d*", allDriverType, () -> db.columnTypes));
        db.registerVersionType(new VersionType(db, "11g?", "11", "\\d*", allDriverType, () -> db.columnTypes));
    }
    ),
    MYSQL("MySql", ".*MySql.*", db -> {
        db.registerColumnType(new ColumnType("CHAR", 255));
        db.registerColumnType(new ColumnType("VARCHAR", 65535));
        db.registerColumnType(new ColumnType("TINYTEXT"));
        db.registerColumnType(new ColumnType("TEXT"));
        db.registerColumnType(new ColumnType("MEDIUMTEXT"));
        db.registerColumnType(new ColumnType("LONGTEXT"));
        db.registerColumnType(new ColumnType("TINYINT"));
        db.registerColumnType(new ColumnType("SMALLINT"));
        db.registerColumnType(new ColumnType("MEDIUMINT"));
        db.registerColumnType(new ColumnType("INT"));
        db.registerColumnType(new ColumnType("BIGINT"));
        db.registerColumnType(new ColumnType("DECIMAL", 65, 30));
        db.registerColumnType(new ColumnType("DATE"));
        db.registerColumnType(new ColumnType("TIME"));
        db.registerColumnType(new ColumnType("DATETIME"));
        db.registerColumnType(new ColumnType("TIMESTAMP"));
        db.registerColumnType(new ColumnType("YEAR"));
        db.registerColumnType(new ColumnType("BINARY", 255));
        db.registerColumnType(new ColumnType("VARBINARY", 65535));
        db.registerColumnType(new ColumnType("TINYBLOB"));
        db.registerColumnType(new ColumnType("BLOB"));
        db.registerColumnType(new ColumnType("MEDIUMBLOB"));
        db.registerColumnType(new ColumnType("LONGBLOB"));
        DriverType driverType8 = new DriverType(db, "com.mysql.cj.jdbc.Driver", "jdbc:mysql://");
        DriverType driverType5 = new DriverType(db, "com.mysql.jdbc.Driver", "jdbc:mysql://");
        db.registerVersionType(new VersionType(db, "8\\.\\d", "8", "\\d+", driverType8, () -> db.columnTypes));
        db.registerVersionType(new VersionType(db, "5\\.\\d", "5", "\\d+", driverType5, () -> db.columnTypes));
    });
    private final String name;
    private final Pattern namePattern;
    private final Map<String, VersionType> versionTypes = new LinkedHashMap<>();
    private final Map<String, ColumnType> columnTypes = new LinkedHashMap<>();

    Database(String name, String nameRegexp, Consumer<Database> consumer) {
        this.name = name;
        this.namePattern = Pattern.compile(nameRegexp, Pattern.CASE_INSENSITIVE);
        consumer.accept(this);
    }

    /**
     * 根据产品名称、产品版本和主次版本号解析出对应的数据库实例类型
     *
     * @param productName    产品名称（如"MySQL"、"Oracle"等）
     * @param productVersion 产品版本字符串（如"8.0.25"、"19c"等）
     * @param majorVersion   主版本号（整数形式）
     * @param minorVersion   次版本号（整数形式）
     * @return 匹配到的DatabaseInstanceType枚举值
     * @throws IllegalArgumentException 如果没有找到匹配的类型或输入参数为空
     */
    public VersionType parse(String productName, String productVersion, int majorVersion, int minorVersion) {
        // 参数验证 - 确保关键参数不为null
        if (productName == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }
        if (productVersion == null) {
            throw new IllegalArgumentException("Product version cannot be null");
        }

        // 遍历所有枚举值，查找匹配的类型
        for (VersionType type : versionTypes.values()) {
            if (type.matches(productName, productVersion, majorVersion, minorVersion)) {
                return type; // 找到匹配项，直接返回
            }
        }

        // 如果没有找到匹配项，抛出异常说明未识别的数据库类型
        throw new IllegalArgumentException("Unknown database type: " + productName + ", version: " + productVersion + ", major: " + majorVersion + ", minor: " + minorVersion);
    }

    private void registerColumnType(ColumnType columnType) {
        columnTypes.put(columnType.name, columnType);
    }

    private void registerVersionType(VersionType versionTypes) {
        this.versionTypes.put(versionTypes.toString(), versionTypes);
        versionTypes.driverType.sorted = this.versionTypes.size();
    }

    public boolean isMatch(String name) {
        return namePattern.matcher(name).matches();
    }

    @Getter
    public static class VersionType implements Comparable<VersionType> {
        // 数据库类型，如MySQL、Oracle等
        private final Database database;
        // 产品版本的正则表达式模式，用于匹配完整的产品版本字符串
        private final Pattern productVersionPattern;
        // 主版本号的正则表达式模式，用于匹配主版本号部分
        private final Pattern majorVersionPattern;
        // 次版本号的正则表达式模式，用于匹配次版本号部分
        private final Pattern minorVersionPattern;
        private final DriverType driverType;
        private final Map<String, ColumnType> columnTypes;
        protected int sorted;

        @Override
        public int compareTo(VersionType other) {
            return Integer.compare(this.sorted, other.sorted);
        }

        public VersionType(Database database, String productVersionRegexp, String majorVersionRegexp, String minorVersionRegexp, DriverType driverType, Supplier<Map<String, ColumnType>> columnTypesSupplier) {
            this.database = database;
            this.driverType = driverType;
            // 编译正则表达式，设置不区分大小写标志
            this.productVersionPattern = Pattern.compile(productVersionRegexp, Pattern.CASE_INSENSITIVE);
            this.majorVersionPattern = Pattern.compile(majorVersionRegexp, Pattern.CASE_INSENSITIVE);
            this.minorVersionPattern = Pattern.compile(minorVersionRegexp, Pattern.CASE_INSENSITIVE);
            this.columnTypes = columnTypesSupplier.get();
        }

        /**
         * 判断当前数据库实例类型是否与提供的参数匹配
         *
         * @param productName    产品名称
         * @param productVersion 产品版本字符串
         * @param majorVersion   主版本号
         * @param minorVersion   次版本号
         * @return 如果所有条件都匹配则返回true，否则返回false
         */
        public boolean matches(String productName, String productVersion, int majorVersion, int minorVersion) {
            // 组合匹配条件：
            // 1. 数据库类型匹配（通过DatabaseType.matches方法）
            // 2. 产品版本匹配（使用正则表达式检查productVersion是否包含符合pattern的部分）
            // 3. 主版本号匹配（将int转为string后进行正则匹配）
            // 4. 次版本号匹配（将int转为string后进行正则匹配）
            return database.isMatch(productName) &&
                    productVersionPattern.matcher(productVersion).find() &&
                    majorVersionPattern.matcher(String.valueOf(majorVersion)).find() &&
                    minorVersionPattern.matcher(String.valueOf(minorVersion)).find();
        }
    }

    @Getter
    public static class DriverType implements Comparable<DriverType> {
        private final Database database;
        private final String driverClassName;
        private final String jdbcUrlPrefix;
        protected int sorted;

        @Override
        public int compareTo(DriverType other) {
            return Integer.compare(this.sorted, other.sorted);
        }

        private DriverType(Database database, String driverClassName, String jdbcUrlPrefix) {
            this.database = database;
            this.driverClassName = driverClassName;
            this.jdbcUrlPrefix = jdbcUrlPrefix;
        }
    }

    @Getter
    public static class ColumnType implements Comparable<ColumnType> {
        private final String name;
        private final int columnTypeMaxLength;
        private final int columnTypeMaxPrecision;
        protected int sorted;

        @Override
        public int compareTo(ColumnType other) {
            return Integer.compare(this.sorted, other.sorted);
        }

        private ColumnType(String name) {
            this(name, -1, -1);
        }

        private ColumnType(String name, int columnTypeMaxLength) {
            this(name, columnTypeMaxLength, -1);
        }

        private ColumnType(String name, int columnTypeMaxLength, int columnTypeMaxPrecision) {
            this.name = name.trim();
            this.columnTypeMaxLength = columnTypeMaxLength;
            this.columnTypeMaxPrecision = columnTypeMaxPrecision;
        }

        public boolean isValidLength(int length) {
            if (columnTypeMaxLength < 0) {
                return true;
            }
            return length <= columnTypeMaxLength;
        }

        public boolean isValidPrecision(int precision) {
            if (columnTypeMaxPrecision < 0) {
                return true;
            }
            return precision <= columnTypeMaxPrecision;
        }

        public boolean isValidPrecision(int length, int precision) {
            return isValidLength(length) && isValidPrecision(precision);
        }

    }
}
