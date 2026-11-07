package terraqo.morphix.copier;

@FunctionalInterface
public interface PropertyConverter<S, T> {
    T apply(S s) throws Exception;
}
