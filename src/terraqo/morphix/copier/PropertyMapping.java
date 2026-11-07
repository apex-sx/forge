package terraqo.morphix.copier;

import java.lang.invoke.MethodHandle;

/**
 * @author Terraqo
 */
record PropertyMapping(String propertyName, MethodHandle getter, MethodHandle setter, Class<?> targetType) {
}
