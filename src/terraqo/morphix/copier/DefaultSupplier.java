package terraqo.morphix.copier;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Terraqo
 */
public interface DefaultSupplier {
    DefaultSupplier UNIVERSAL_INSTANCE = new DefaultSupplier() {
        private static final Number ZERO = 0;

        private static final Map<Class<?>, Object> DEFAULT_VALUE = Map.ofEntries(
                Map.entry(String.class, ""),
                Map.entry(char.class, '\u0000'),
                Map.entry(Character.class, '\u0000'),
                Map.entry(boolean.class, Boolean.FALSE),
                Map.entry(Boolean.class, Boolean.FALSE),
                Map.entry(byte.class, ZERO.byteValue()),
                Map.entry(Byte.class, ZERO.byteValue()),
                Map.entry(short.class, ZERO.shortValue()),
                Map.entry(Short.class, ZERO.shortValue()),
                Map.entry(int.class, ZERO.intValue()),
                Map.entry(Integer.class, ZERO.intValue()),
                Map.entry(long.class, ZERO.longValue()),
                Map.entry(Long.class, ZERO.longValue()),
                Map.entry(float.class, ZERO.floatValue()),
                Map.entry(Float.class, ZERO.floatValue()),
                Map.entry(double.class, ZERO.doubleValue()),
                Map.entry(Double.class, ZERO.doubleValue()),
                Map.entry(BigInteger.class, BigInteger.ZERO),
                Map.entry(BigDecimal.class, BigDecimal.ZERO),
                Map.entry(Date.class, ((Supplier<Date>) Date::new)),
                Map.entry(Instant.class, ((Supplier<Instant>) Instant::now)),
                Map.entry(LocalDate.class, ((Supplier<LocalDate>) LocalDate::now)),
                Map.entry(LocalTime.class, ((Supplier<LocalTime>) LocalTime::now)),
                Map.entry(LocalDateTime.class, ((Supplier<LocalDateTime>) LocalDateTime::now)),
                Map.entry(ZonedDateTime.class, ((Supplier<ZonedDateTime>) ZonedDateTime::now)),
                Map.entry(OffsetDateTime.class, ((Supplier<OffsetDateTime>) OffsetDateTime::now))
        );

        @Override
        public <T> T getDefaultValue(Class<T> type) {
            Object value = DEFAULT_VALUE.get(type);
            if (value instanceof Supplier<?>) {
                value = ((Supplier<?>) value).get();
            }
            return (T) value;
        }
    };

    <T> T getDefaultValue(Class<T> type);
}
