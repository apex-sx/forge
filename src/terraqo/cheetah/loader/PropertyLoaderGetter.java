package terraqo.cheetah.loader;

/**
 * @author Terraqo
 */
public interface PropertyLoaderGetter<T> {

    T get( String property) throws Exception;
}
