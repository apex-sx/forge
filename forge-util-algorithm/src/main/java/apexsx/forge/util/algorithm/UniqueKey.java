package apexsx.forge.util.algorithm;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Terraqo
 */
public class UniqueKey {
    private final Object[] keys;
    private final int hash;

    public UniqueKey(Object... keys) {
        this.keys = keys;
        int hash = 1;
        for (Object key : keys) {
            hash = 31 * hash + (key == null ? 0 : key.hashCode());
        }
        this.hash = hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UniqueKey uniqueKey = (UniqueKey) object;
        return hash == uniqueKey.hash && Arrays.equals(keys, uniqueKey.keys);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        return Arrays.stream(keys).map(String::valueOf).collect(Collectors.joining(","));
    }
}
