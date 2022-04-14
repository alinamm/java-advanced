package info.kgeorgiy.ja.mustafina.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ParallelMapperImpl implements ParallelMapper {
    private final Queue<Runnable> tasks = new ArrayDeque<>();
    private final List<Thread> action;

    public ParallelMapperImpl(int threads) {
        this.action = IntStream.range(0, threads).mapToObj(t -> new Thread(() -> {
            try {
                Runnable runnable;
                while (!Thread.interrupted()) {
                    synchronized (tasks) {
                        while (tasks.size() == 0) {
                            tasks.wait();
                        }
                        runnable = tasks.poll();
                        tasks.notify();
                    }
                    runnable.run();
                }
            } catch (InterruptedException ignored) {
            }
        })).peek(Thread::start).toList();
    }

    private static class Result<R> {
        private int space;
        private final List<R> res;

        public Result(int size) {
            space = size;
            res = new ArrayList<>(Collections.nCopies(size, null));
        }

        synchronized public List<R> getRes() throws InterruptedException {
            while (space > 0) wait();
            return res;
        }

        synchronized public void set(int i, R arg) {
            res.set(i, arg);
            // :NOTE: brackets
            if (--space == 0) notify();
        }
    }

    /**
     * Maps function {@code f} over specified {@code args}.
     * Mapping for each element performs in parallel.
     *
     * @throws InterruptedException if calling thread was interrupted
     */
    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> f, List<? extends T> args)
            throws InterruptedException {
        final int size = args.size();
        final Result<R> res = new Result<>(size);
        for (int i = 0; i < size; i++) {
            synchronized (tasks) {
                int ind = i;
                tasks.add(() -> res.set(ind, f.apply(args.get(ind))));
                tasks.notify();
            }
        }
        return res.getRes();
    }

    /**
     * Stops all threads. All unfinished mappings leave in undefined state.
     */
    @Override
    public void close() {
        for (Thread a : action) {
            a.interrupt();
            try {
                a.join();
            } catch (InterruptedException ignored) {
            }
        }
    }
}