package apexsx.forge.standard.iterator;

public class ArrayIterator<E> extends IndexIterator<E> {

    private final E[] elements;


    public ArrayIterator(E[] elements) {
        this.elements = elements;
    }

    @Override
    protected int size() {
        return elements.length;
    }

    @Override
    protected E get(int i) {
        return elements[i];
    }
}