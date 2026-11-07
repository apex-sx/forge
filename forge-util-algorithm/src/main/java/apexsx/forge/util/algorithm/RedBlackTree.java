package apexsx.forge.util.algorithm;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author Terraqo
 */
public class RedBlackTree<E> implements Comparable<RedBlackTree<E>> {
    private final int hash;
    private final E record;
    private RedBlackTree<E> parent;
    private RedBlackTree<E> left;
    private RedBlackTree<E> right;
    private boolean color;
    private int size;

    public RedBlackTree(int hash, E record) {
        this.hash = hash;
        this.record = record;
    }

    public int size() {
        return size;
    }

    public RedBlackTree<E> put(int hash, E record) {
        return null;
    }

    public RedBlackTree<E> delete(int hash, E record) {
        return null;
    }

    public RedBlackTree<E> find(int hash, E record) {
        return null;
    }

    public RedBlackTree<E> findMin() {
        return null;
    }

    public RedBlackTree<E> findMax() {
        return null;
    }

    public RedBlackTree<E> findFloor(int hash, E record) {
        return null;
    }

    public RedBlackTree<E> findCeiling(int hash, E record) {
        return null;
    }

    public boolean contains(int hash, E record) {
        return false;
    }

    @Override
    public int compareTo(RedBlackTree<E> other) {
        if (this.record == other.record) {
            return 0;
        }
        if (this.record == null) {
            return -1;
        }
        if (other.record == null) {
            return 1;
        }
        if (this.hash < other.hash) {
            return -1;
        }
        if (this.hash > other.hash) {
            return 1;
        }
        if (Objects.equals(this.record, other.record)) {
            return 0;
        }
        if (this.record instanceof Comparable) {
            Class<?> c = this.record.getClass();
            Type[] as;
            ParameterizedType p;
            Type[] ts = c.getGenericInterfaces();
            for (Type t : ts) {
                if ((t instanceof ParameterizedType) &&
                        ((p = (ParameterizedType) t).getRawType() == Comparable.class) &&
                        (as = p.getActualTypeArguments()) != null &&
                        as.length == 1 && as[0] == c) // type arg is c
                    return 0;
            }

        }
        @SuppressWarnings("unchecked")
        int cmp = ((Comparable<? super E>) this.record).compareTo(other.record);
        return cmp;
    }

    final RedBlackTree<E> find(int h, Object k, Class<?> kc) {
        RedBlackTree<E> p = this;
        do {
            int ph, dir;
            E pk;
            RedBlackTree<E> pl = p.left, pr = p.right, q;
            if ((ph = p.hash) > h)
                p = pl;
            else if (ph < h)
                p = pr;
            else if ((pk = p.record) == k || (k != null && k.equals(pk)))
                return p;
            else if (pl == null)
                p = pr;
            else if (pr == null)
                p = pl;
            else if ((kc != null ||
                    (kc = comparableClassFor(k)) != null) &&
                    (dir = compareComparables(kc, k, pk)) != 0)
                p = (dir < 0) ? pl : pr;
            else if ((q = pr.find(h, k, kc)) != null)
                return q;
            else
                p = pl;
        } while (p != null);
        return null;
    }

    /**
     * Returns x's Class if it is of the form "class C implements
     * Comparable<C>", else null.
     */
    static Class<?> comparableClassFor(Object x) {

        return null;
    }

    /**
     * Returns k.compareTo(x) if x matches kc (k's screened comparable
     * class), else 0.
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // for cast to Comparable
    static int compareComparables(Class<?> kc, Object k, Object x) {
        return (x == null || x.getClass() != kc ? 0 :
                ((Comparable) k).compareTo(x));
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        RedBlackTree<?> that = (RedBlackTree<?>) object;
        return hash == that.hash && Objects.equals(record, that.record);
    }

    @Override
    public String toString() {
        return String.valueOf(record);
    }
}
