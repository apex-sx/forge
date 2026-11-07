package pxf.toolkit.basic.lang.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;

/**
 * {@code List}包裹器
 *
 * @author potatoxf
 * @date 2020/11/11
 */
public class ControllableAppendListWrapper<E> extends BaseListWrapper<E> {

  private static final long serialVersionUID = -8875606683102241492L;
  private final int amount;
  private final AppendMode mode;
  private final AtomicInteger tempIndex;

  public ControllableAppendListWrapper() {
    this(AppendMode.NORMAL, 16);
  }

  public ControllableAppendListWrapper(AppendMode mode, int amount) {
    super(new ArrayList<>(amount));
    this.amount = amount;
    switch (mode) {
      case FORBID:
        this.mode = AppendMode.FORBID;
        this.tempIndex = null;
        break;
      case BEYOND_IGNORED:
        this.mode = AppendMode.BEYOND_IGNORED;
        this.tempIndex = null;
        break;
      case BEYOND_OVERRIDE_BEFORE:
        this.mode = AppendMode.BEYOND_OVERRIDE_BEFORE;
        this.tempIndex = new AtomicInteger(-1);
        break;
      case BEYOND_OVERRIDE_AFTER:
        this.mode = AppendMode.BEYOND_OVERRIDE_AFTER;
        this.tempIndex = new AtomicInteger(this.amount);
        break;
      default:
        this.mode = AppendMode.NORMAL;
        this.tempIndex = null;
        break;
    }
  }

  public ControllableAppendListWrapper(List<E> list, AppendMode mode, int amount) {
    super(list);
    this.amount = amount;
    switch (mode) {
      case FORBID:
        this.mode = AppendMode.FORBID;
        this.tempIndex = null;
        break;
      case BEYOND_IGNORED:
        this.mode = AppendMode.BEYOND_IGNORED;
        this.tempIndex = null;
        break;
      case BEYOND_OVERRIDE_BEFORE:
        this.mode = AppendMode.BEYOND_OVERRIDE_BEFORE;
        this.tempIndex = new AtomicInteger(-1);
        break;
      case BEYOND_OVERRIDE_AFTER:
        this.mode = AppendMode.BEYOND_OVERRIDE_AFTER;
        this.tempIndex = new AtomicInteger(0);
        break;
      default:
        this.mode = AppendMode.NORMAL;
        this.tempIndex = null;
        break;
    }
  }

  @Override
  public boolean add(E e) {
    boolean result = false;
    switch (mode) {
      case FORBID:
        break;
      case BEYOND_IGNORED:
        if (size() < amount) {
          result = collection.add(e);
        }
        break;
      case BEYOND_OVERRIDE_BEFORE:
        if (size() < amount) {
          result = collection.add(e);
        } else {
          if (tempIndex.get() >= amount - 1) {
            tempIndex.set(-1);
          }
          int i = tempIndex.incrementAndGet();
          collection.set(i, e);
          result = true;
        }
        break;
      case BEYOND_OVERRIDE_AFTER:
        if (size() < amount) {
          result = collection.add(e);
        } else {
          if (tempIndex.get() >= amount) {
            tempIndex.set(0);
          }
          int i = tempIndex.incrementAndGet();
          collection.set(amount - i, e);
          result = true;
        }
        break;
      default:
        result = collection.add(e);
        break;
    }
    return result;
  }

  @Override
  public boolean addAll(@Nonnull Collection<? extends E> c) {
    boolean result = false;
    switch (mode) {
      case FORBID:
        break;
      case BEYOND_IGNORED:
        if (size() + c.size() < amount) {
          result = collection.addAll(c);
        } else {
          int restSize = amount - size();
          fillRemainingSpace(c, restSize);
          result = true;
        }
        break;
      case BEYOND_OVERRIDE_BEFORE:
        if (size() + c.size() < amount) {
          result = collection.addAll(c);
        } else {
          int restSize = amount - size();
          Iterator<? extends E> remainingIterator = fillRemainingSpace(c, restSize);
          while (remainingIterator.hasNext()) {
            if (tempIndex.get() >= amount - 1) {
              tempIndex.set(-1);
            }
            int i = tempIndex.incrementAndGet();
            E e = remainingIterator.next();
            collection.set(i, e);
          }
          result = true;
        }
        break;
      case BEYOND_OVERRIDE_AFTER:
        if (size() + c.size() < amount) {
          result = collection.addAll(c);
        } else {
          int restSize = amount - size();
          Iterator<? extends E> remainingIterator = fillRemainingSpace(c, restSize);
          while (remainingIterator.hasNext()) {
            if (tempIndex.get() >= amount) {
              tempIndex.set(0);
            }
            int i = tempIndex.incrementAndGet();
            E e = remainingIterator.next();
            collection.set(amount - i, e);
          }
          result = true;
        }
        break;
      default:
        result = collection.addAll(c);
        break;
    }
    return result;
  }

  @Override
  public boolean addAll(int index, @Nonnull Collection<? extends E> c) {
    throw new UnsupportedOperationException("Does not support adding elements by specified index");
  }

  @Override
  public void add(int index, E element) {
    throw new UnsupportedOperationException("Does not support adding elements by specified index");
  }

  private Iterator<? extends E> fillRemainingSpace(
      @Nonnull Collection<? extends E> c, int restSize) {
    Iterator<? extends E> iterator = c.iterator();
    for (int i = 0; i < restSize; i++) {
      E e = iterator.next();
      collection.add(e);
    }
    return iterator;
  }
}
