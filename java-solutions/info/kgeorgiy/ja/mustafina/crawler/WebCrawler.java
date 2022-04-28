package info.kgeorgiy.ja.mustafina.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.util.*;
import java.io.IOException;
import java.util.concurrent.*;

public class WebCrawler implements Crawler {
    private final Downloader downloader;
    private final ExecutorService downloaders;
    private final ExecutorService extractors;

    public static void main(String[] args) {
        try (Crawler crawler = new WebCrawler(new CachingDownloader(), getValue(2, args), getValue(3, args),
                getValue(4, args))) {
            crawler.download(args[0], getValue(1, args));
        } catch (IOException e) {
            System.err.println("CachingDownloader exception " + e.getMessage());
        }
    }

    private static int getValue(int i, String[] args) {
        if (args.length < i) {
            return 0;
        } else {
            return Integer.parseInt(args[i]);
        }
    }

    @SuppressWarnings("unused")
    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        this.downloader = downloader;
        this.extractors = Executors.newFixedThreadPool(extractors);
        this.downloaders = Executors.newFixedThreadPool(downloaders);
    }

    @Override
    public Result download(String s, int i) {
        Set<String> checked = new ConcurrentSkipListSet<>();
        Set<String> urls = new ConcurrentSkipListSet<>();
        urls.add(s);
        Phaser phaser = new Phaser(1);
        Map<String, IOException> downloadErrors = new ConcurrentHashMap<>();
        int depth = 0;
        while (depth < i) {
            List<String> list = new ArrayList<>();
            for (String u : urls) {
                if (!checked.contains(u)) {
                    list.add(u);
                }
                urls.remove(u);
            }
            depth++;
            for (String l : list) {
                int finalDepth = depth;
                phaser.register();
                checked.add(l);
                downloaders.submit(() -> {
                    try {
                        Document doc = downloader.download(l);
                        if (finalDepth != i) {
                            phaser.register();
                            extractors.submit(() -> {
                                List<String> links = new ArrayList<>();
                                try {
                                    links = doc.extractLinks();
                                } catch (IOException e) {
                                    System.err.println("Failed to extract links " + e.getMessage());
                                }
                                urls.addAll(links);
                                phaser.arriveAndDeregister();
                            });
                        }
                    } catch (IOException e) {
                        downloadErrors.put(l, e);
                    }
                    phaser.arriveAndDeregister();
                });
            }
            phaser.arriveAndAwaitAdvance();
        }
        for (String downloaded : downloadErrors.keySet()) {
            checked.remove(downloaded);
        }
        return new Result(checked.stream().toList(), downloadErrors);
    }

    @Override
    public void close() {
        downloaders.shutdown();
        extractors.shutdown();
        try {
            if (!extractors.awaitTermination(10, TimeUnit.SECONDS) ||
                    !downloaders.awaitTermination(10, TimeUnit.SECONDS)) {
                System.err.println("Executor hasn't been shutdown");
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted while waiting " + e.getMessage());
        }
    }
}