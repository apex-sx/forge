package apexsx.forge.lang.meta;

/**
 * @author Terraqo
 */
public record Range(int min, int max) {

    public static Range of(int min, int max) {
        return new Range(min, max);
    }
}
