package terraqo.morphix.copier;

import java.util.Objects;

/**
 * @author Terraqo
 */
record BeanCopierKey(Class<?> sourceClass, Class<?> targetClass, BeanCopierSetting setting) {
    BeanCopierKey {
        Objects.requireNonNull(sourceClass);
        Objects.requireNonNull(targetClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceClass, targetClass, System.identityHashCode(setting));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BeanCopierKey other)) return false;
        return sourceClass == other.sourceClass &&
                targetClass == other.targetClass &&
                setting == other.setting;
    }
}
