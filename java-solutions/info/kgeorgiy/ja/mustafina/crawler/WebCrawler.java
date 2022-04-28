package info.kgeorgiy.ja.mustafina.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class WebCrawler implements Crawler {
    private final Downloader downloader;
    private final ExecutorService downloaders;
    private final ExecutorService extractors;

    // :NOTE: usage: WebCrawler url [depth [downloads [extractors [perHost]]]] -> default values
    public static void main(String[] args) {
        try (Crawler crawler = new WebCrawler(new CachingDownloader(), Integer.parseInt(args[2]),
                Integer.parseInt(args[3]), Integer.parseInt(args[4]))) {
            crawler.download(args[0], Integer.parseInt(args[1]));
        } catch (IOException e) {
            System.err.println("CachingDownloader exception " + e.getMessage());
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
        // :NOTE: awaitTermination
        if (!downloaders.isShutdown() || !extractors.isShutdown()) {
            System.err.println("Executor hasn't been shutdown");
        }
    }
}