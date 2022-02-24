package info.kgeorgiy.ja.mustafina.arrayset;

import java.util.*;

public class ArraySet<E> extends AbstractSet<E> implements SortedSet<E> {

    private final ArrayList<E> array;
    private final Comparator<E> comparator;

    public ArraySet() {
        this(Collections.emptyList(), null);
    }

    @Override
    public Iterator<E> iterator() {
        return array.iterator();
    }

    public ArraySet(Comparator<E> comparator) {
        this(Collections.emptyList(), comparator);
    }

    public ArraySet(Collection<E> collection) {
        this(collection, null);
    }

    public ArraySet(Collection<E> collection, Comparator<E> comparator) {
        this.comparator = comparator;
        TreeSet<E> treeSet = new TreeSet<>(comparator);
        treeSet.addAll(collection);
        array = new ArrayList<>(treeSet);

    }

    @Override
    public int size() {
        return array.size();
    }

    @Override
    public boolean isEmpty() {
        return array.size() == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object e) {
        return findElement((E) e) >= 0;
    }

    @Override
    public Comparator<E> comparator() {
        return comparator;
    }

    private int findElement(E element) {
        return Collections.binarySearch(array, element, comparator);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return getSet(null, toElement);
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        if (comparator.compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException();
        }
        return getSet(fromElement, toElement);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return getSet(fromElement, null);
    }

    private SortedSet<E> getSet(E fromElement, E toElement) {
        int toIndex;
        if (toElement != null) {
            toIndex = findElement(toElement);
            if (toIndex < 0) {
                toIndex = Math.abs(toIndex) - 1;
            }
        } else {
            toIndex = array.size();
        }
        int fromIndex;
        if (fromElement != null) {
            fromIndex = findElement(fromElement);
            if (fromIndex < 0) {
                fromIndex = Math.abs(fromIndex) - 1;
            }
        } else {
            fromIndex = 0;
        }
        List<E> subSet = array.subList(fromIndex, toIndex);
        return new ArraySet<>(subSet, comparator);
    }

    @Override
    public E last() {
        return firstOrLastElement(array.size() - 1);
    }

    @Override
    public E first() {
        return firstOrLastElement(0);
    }

    private E firstOrLastElement(int index) {
        if (array.size() == 0) {
            throw new NoSuchElementException();
        }
        return array.get(index);
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}