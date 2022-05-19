package info.kgeorgiy.ja.mustafina.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.*;

public class HelloUDPNonblockingServer implements HelloServer {
    private static final General general = new General();
    private ExecutorService service;
    private Selector selector;
    private DatagramChannel channel;

    public static void main(String[] args) {
        General general = new General();
        general.serverMain(args);
    }

    @Override
    public void start(int port, int threads) {
        if (channel != null) {
            return;
        }
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
            channel.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            System.err.println(general.IOEXCEPTION_MESSAGE + e.getMessage());
        }
        Queue<Content> queue = new ConcurrentLinkedDeque<>();
        service = Executors.newFixedThreadPool(threads);
        Executors.newSingleThreadExecutor().submit(() -> {
            while (!channel.socket().isClosed() && !Thread.interrupted()) {
                try {
                    if (selector.select() > 0) {
                        process(queue);
                    }
                } catch (IOException e) {
                    System.err.println(general.IOEXCEPTION_MESSAGE + e.getMessage());
                }
            }
        });
    }

    private void process(Queue<Content> queue) throws IOException {
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            if (key.isValid()) {
                if (key.isReadable()) {
                    service.submit(getRunnable(queue, key));
                }
                if (key.isWritable()) {
                    if (!queue.isEmpty()) {
                        Content content = queue.poll();
                        channel.send(content.getBuffer(), content.getAddress());
                    }
                    key.interestOpsOr(SelectionKey.OP_READ);
                    selector.wakeup();
                }
            }
            iterator.remove();
        }
    }

    private Runnable getRunnable(Queue<Content> queue, SelectionKey key) throws IOException {
        Charset utf8 = StandardCharsets.UTF_8;
        int size = 0;
        try {
            size = channel.socket().getReceiveBufferSize();
        } catch (SocketException e) {
            System.err.println("Error: there is an error in the underlying protocol " + e.getMessage());
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        SocketAddress socketAddress = channel.receive(byteBuffer);
        return (() -> {
            byteBuffer.flip();
            String result = "Hello, " + utf8.decode(byteBuffer);
            queue.add(new Content(ByteBuffer.wrap(result.getBytes(utf8)), socketAddress));
            key.interestOpsOr(SelectionKey.OP_WRITE);
            selector.wakeup();
        });
    }

    @Override
    public void close() {
        try {
            selector.close();
            channel.close();
        } catch (IOException e) {
            System.err.println(general.IOEXCEPTION_MESSAGE + e.getMessage());
        }
        general.close(service);
    }

    private record Content(ByteBuffer buffer, SocketAddress address) {

        public ByteBuffer getBuffer() {
            return buffer;
        }

        public SocketAddress getAddress() {
            return address;
        }
    }
}
