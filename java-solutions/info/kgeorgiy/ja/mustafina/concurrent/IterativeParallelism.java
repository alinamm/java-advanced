package info.kgeorgiy.ja.mustafina.concurrent;


import info.kgeorgiy.java.advanced.concurrent.ScalarIP;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public class IterativeParallelism implements ScalarIP {

    public static class RunnableAndStream<T, R> {
        T runnable;
        R stream;

        public RunnableAndStream(T runnable, R stream) {
            this.runnable = runnable;
            this.stream = stream;
        }
    }

    public static class RunnableEx<T, R> implements Runnable {
        private final Function<List<? extends T>, R> function;
        private final List<? extends T> list;
        private R result;

        public RunnableEx(List<? extends T> list, Function<List<? extends T>, R> function) {
            this.list = list;
            this.function = function;
        }

        @Override
        public void run() {
            this.result = function.apply(list);
        }
    }

    public <T, R> List<R> parallel(int threads, List<? extends T> values, Function<List<? extends T>, R> function)
            throws InterruptedException {
        List<RunnableAndStream<RunnableEx<T, R>, Thread>> action = new ArrayList<>();
        int len = values.size() / threads;
        for (int i = 0; i < threads; i++) {
            List<? extends T> temp = values.subList(i * len, i < threads - 1 ? (i + 1) * len : values.size());
            if (!temp.isEmpty()) {
                RunnableEx<T, R> runnableEx = new RunnableEx<>(temp, function);
                action.add(new RunnableAndStream<>(runnableEx, new Thread(runnableEx)));
            }
        }
        action = action.stream().peek(a -> a.stream.start()).toList();
        List<R> res = new ArrayList<>();
        for (RunnableAndStream<RunnableEx<T, R>, Thread> a : action) {
            a.stream.join();
            res.add(a.runnable.result);
        }
        return res;
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
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        List<T> res = parallel(threads, values, (list) -> list.stream().max(comparator).orElseThrow());
        return res.stream().max(comparator).orElseThrow();
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
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        List<T> res = parallel(threads, values, (list) -> list.stream().min(comparator).orElseThrow());
        return res.stream().min(comparator).orElseThrow();
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
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        List<Boolean> res = parallel(threads, values, (list) -> list.stream().allMatch(predicate));
        return !res.contains(false);
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
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        List<Boolean> res = parallel(threads, values, (list) -> list.stream().anyMatch(predicate));
        return res.contains(true);
    }
}
