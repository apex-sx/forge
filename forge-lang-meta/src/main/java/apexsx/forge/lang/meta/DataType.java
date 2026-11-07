package apexsx.forge.lang.meta;

/**
 * @author Terraqo
 */
public enum DataType {
    BOOLEAN(Boolean.class),
    BYTE(Byte.class),
    SHORT(Short.class),
    INTEGER(Integer.class),
    LONG(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    BIG_INTEGER(java.math.BigInteger.class),
    BIG_DECIMAL(java.math.BigDecimal.class),
    STRING(String.class),
    BYTES(byte[].class),
    DATE(java.sql.Date.class),
    TIME(java.sql.Time.class),
    TIMESTAMP(java.sql.Timestamp.class),
    BLOB(java.sql.Blob.class),
    CLOB(java.sql.Clob.class),
    ;

    private final Class<?> javaType;

    DataType(Class<?> javaType) {
        this.javaType = javaType;
    }
}
