package apexsx.forge.lang.meta;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Terraqo
 */
public class DatabaseConnection {
    private final Map<String, Store> stores = new HashMap<>();

    public DatabaseConnection() {
    }


    public Builder addCredential() {
        return new Builder();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DatabaseConnection databaseConnectionManager = new DatabaseConnection();

        // 使用builder模式创建ConnectionInformation实例

        Credential credential = databaseConnectionManager.addCredential()
                .setName("mysql数据库连接")
                .setDb("mysql5.7")
                .setHost("zhitron-nas")  // 根据注释中的URL修改
                .setPort(3306)          // 根据注释中的URL修改
                .setDatabase("sz_admin_preview")  // 根据注释中的URL修改
                .setUsername("admin")     // 根据注释中的设置修改
                .setPassword("Admin@111111")
                .build();

        System.out.println("Test Connection Result: " + credential);
    }

    public Connection getConnection(String name) throws SQLException {
        Store store = stores.get(name);
        if (store == null) {
            throw new SQLException("Database connection not found: " + name);
        }
        String url = store.urlBuilder.buildConnectionURL(store.driverType, store.credential);
        try {
            Class.forName("driverClassName");
        } catch (ClassNotFoundException e) {
            throw new SQLException("数据库驱动类未找到");
        }
        // 尝试建立数据库连接
        return DriverManager.getConnection(url, store.credential.username(), store.credential.password());
    }

    /**
     * @author Terraqo
     */
    @Setter
    @Accessors(chain = true)  // 启用链式调用
    public class Builder {
        private String db;
        private UrlBuilder urlBuilder;
        private String name;
        private String host;
        private int port;
        private String database;
        private String username;
        private String password;

        private Builder() {

        }

        public Credential build() throws SQLException {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            if (stores.containsKey(name)) {
                throw new SQLException("Database connection already exists: " + name);
            }
            if (db == null) {
                throw new IllegalArgumentException("Driver type cannot be null");
            }
            if (host == null || host.isEmpty()) {
                throw new IllegalArgumentException("Host cannot be null");
            }
            if (port < 0) {
                throw new IllegalArgumentException("Port cannot be null");
            }
            if (database == null || database.isEmpty()) {
                throw new IllegalArgumentException("Database cannot be null");
            }
            if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null");
            }
            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null");
            }
            Credential credential = new Credential(name, host, port, database, username, password);
            Store store = new Store(null, credential, urlBuilder);
            stores.put(name, store);
            return credential;
        }
    }


    public record Store(Database.DriverType driverType, Credential credential,
                        UrlBuilder urlBuilder) implements Comparable<Store> {

        @Override
        public int compareTo(Store that) {
            int result = this.driverType.compareTo(that.driverType);
            if (result != 0) {
                return result;
            }
            return this.credential.compareTo(that.credential);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Store that)) return false;
            return Objects.equals(credential, that.credential);
        }

        @Override
        public int hashCode() {
            return Objects.hash(credential);
        }

        @Override
        public String toString() {
            return "[" + driverType + "]" + this.credential;
        }
    }

    public record Credential(String name, String host, int port, String database,
                             String username, String password) implements Comparable<Credential> {

        @Override
        public int compareTo(Credential that) {
            return this.toString().compareTo(that.toString());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Credential that)) return false;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return username + "@" + host + ":" + port + "/" + database + "(" + name + ")";
        }
    }

    public interface UrlBuilder {

        String buildConnectionURL(Database.DriverType driverType, Credential credential);
    }

    /**
     * @author Terraqo
     */
    @Getter
    public class DatabaseDriverTypeUrlBuilder implements DatabaseConnection.UrlBuilder {
        public String buildConnectionURL(Database.DriverType driverType, DatabaseConnection.Credential credential) {
            Database database = driverType.getDatabase();
            if (database == Database.MYSQL) {
                return driverType.getJdbcUrlPrefix() + credential.host() + ":" + credential.port() + "/" + credential.database();
            } else if (database == Database.ORACLE) {
                return driverType.getJdbcUrlPrefix() + credential.host() + ":" + credential.port() + ":" + credential.database();
            }
            return null;
        }


    }
}
