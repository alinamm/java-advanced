package info.kgeorgiy.ja.mustafina.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ScalarIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class IterativeParallelism implements ScalarIP {
    private final ParallelMapper parallelMapper;

    public IterativeParallelism(final ParallelMapper parallelMapper) {
        this.parallelMapper = parallelMapper;
    }

    public IterativeParallelism() {
        this.parallelMapper = null;
    }

    private <T> List<List<? extends T>> splitValues(final int threads, final List<? extends T> values) {
        final int len = values.size() / threads;
        final int mod = values.size() % threads;
        int left = 0;
        int right;
        List<List<? extends T>> list = new ArrayList<>();
        for (int i = 0; i < Math.min(threads, values.size()); i++) {
            right = i < mod ? left + len + 1 : left + len;
            list.add(values.subList(left, right));
            left = right;
        }
        return list;
    }

    private <T, R> List<R> parallel(final int threads, final List<? extends T> values, final Function<List<? extends T>, ? extends R> function)
            throws InterruptedException {
        List<List<? extends T>> list = splitValues(threads, values);
        if (parallelMapper == null) {
            final List<R> result = new ArrayList<>(Collections.nCopies(list.size(), null));
            List<Thread> action = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                final int ind = i;
                action.add(new Thread(() -> result.set(ind, function.apply(list.get(ind)))));
            }
            action = action.stream().peek(Thread::start).toList();
            for (Thread a : action) {
                a.join();
            }
            return result;
        } else {
            return parallelMapper.map(function, list);
        }
    }

    /**
     * Returns maximum value.
     *
     * @param threads    number or concurrent threads.
     * @param values     values to get maximum of.
     * @param comparator value comparator.
     * @return maximum of given values
     * @throws InterruptedException   if executing thread was interrupted.
     * @throws NoSuchElementException if no values are given.
     */
    @Override
    public <T> T maximum(final int threads, final List<? extends T> values, final Comparator<? super T> comparator) throws InterruptedException {
        if (threads > 0) {
            final List<T> res = parallel(threads, values, list -> list.stream().max(comparator).orElseThrow());
            return res.stream().max(comparator).orElseThrow();
        }
        return values.stream().max(comparator).orElseThrow();
    }

    /**
     * Returns minimum value.
     *
     * @param threads    number or concurrent threads.
     * @param values     values to get minimum of.
     * @param comparator value comparator.
     * @return minimum of given values
     * @throws InterruptedException   if executing thread was interrupted.
     * @throws NoSuchElementException if no values are given.
     */
    @Override
    public <T> T minimum(final int threads, final List<? extends T> values, final Comparator<? super T> comparator) throws InterruptedException {
        return maximum(threads, values, comparator.reversed());
    }

    /**
     * Returns whether all values satisfies predicate.
     *
     * @param threads   number or concurrent threads.
     * @param values    values to test.
     * @param predicate test predicate.
     * @return whether all values satisfies predicate or {@code true}, if no values are given
     * @throws InterruptedException if executing thread was interrupted.
     */
    @Override
    public <T> boolean all(final int threads, final List<? extends T> values, final Predicate<? super T> predicate) throws InterruptedException {
        if (threads > 0) {
            final List<Boolean> res = parallel(threads, values, list -> list.stream().allMatch(predicate));
            return !res.contains(false);
        }
        return values.stream().allMatch(predicate);
    }

    /**
     * Returns whether any of values satisfies predicate.
     *
     * @param threads   number or concurrent threads.
     * @param values    values to test.
     * @param predicate test predicate.
     * @return whether any value satisfies predicate or {@code false}, if no values are given
     * @throws InterruptedException if executing thread was interrupted.
     */
    @Override
    public <T> boolean any(final int threads, final List<? extends T> values, final Predicate<? super T> predicate) throws InterruptedException {
        return !all(threads, values, predicate.negate());
    }
}