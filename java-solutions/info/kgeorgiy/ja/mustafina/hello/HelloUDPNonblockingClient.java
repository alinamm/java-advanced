package info.kgeorgiy.ja.mustafina.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.IntStream;

public class HelloUDPNonblockingClient implements HelloClient {
    private static final General general = new General();

    public static void main(String[] args) {
        general.clientMain(args);
    }

    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        try {
            Selector selector = Selector.open();
            DatagramChannel[] datagramChannels = new DatagramChannel[threads];
            SocketAddress address = new InetSocketAddress(InetAddress.getByName(host), port);
            IntStream.range(0, threads).forEach(thread -> {
                try {
                    DatagramChannel channel = DatagramChannel.open();
                    channel.configureBlocking(false);
                    channel.connect(address);
                    channel.register(selector, SelectionKey.OP_WRITE, new Content(thread, requests,
                            ByteBuffer.allocate(channel.socket().getReceiveBufferSize())));
                    datagramChannels[thread] = channel;
                } catch (IOException e) {
                    System.err.println(general.IOEXCEPTION_MESSAGE + e.getMessage());
                }
            });
            process(selector, prefix, address);
            Arrays.stream(datagramChannels).forEach(channel -> {
                try {
                    channel.close();
                } catch (IOException e) {
                    System.err.println(general.IOEXCEPTION_MESSAGE + e.getMessage());
                }
            });
            selector.close();
        } catch (IOException e) {
            System.err.println(general.IOEXCEPTION_MESSAGE + e.getMessage());
        }
    }

    private void process(Selector selector, String prefix, SocketAddress address) {
        while (!(Thread.interrupted() || selector.keys().isEmpty())) {
            general.select(selector);
            if (!selector.selectedKeys().isEmpty()) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    Content content = (Content) key.attachment();
                    String request = prefix + content.getThreadNum() + "_" + content.getRequestNum();
                    if (key.isWritable()) {
                        DatagramChannel channel = (DatagramChannel) key.channel();
                        try {
                            channel.send(ByteBuffer.wrap(request.getBytes(StandardCharsets.UTF_8)), address);
                        } catch (IOException e) {
                            System.err.println(general.IOEXCEPTION_MESSAGE + e.getMessage());
                        }
                        key.interestOps(SelectionKey.OP_READ);
                    }
                    if (key.isReadable()) {
                        try {
                            DatagramChannel channel = (DatagramChannel) key.channel();
                            ByteBuffer buff = content.getBuffer().clear();
                            channel.receive(buff);
                            buff.flip();
                            byte[] ans = new byte[buff.remaining()];
                            buff.get(ans);
                            String response = new String(ans, StandardCharsets.UTF_8);
                            if (general.match(prefix, content.getThreadNum(), content.getRequestNum(), response)) {
                                general.write(request, response);
                                key.interestOps(SelectionKey.OP_WRITE);
                                if (!content.add()) {
                                    key.channel().close();
                                }
                            }
                        } catch (IOException e) {
                            System.err.println(general.IOEXCEPTION_MESSAGE + e.getMessage());
                        }
                    }
                    iterator.remove();
                }
            } else {
                for (SelectionKey key : selector.keys()) {
                    key.interestOps(SelectionKey.OP_WRITE);
                }
            }
        }
    }

    private static class Content {
        private final ByteBuffer buffer;
        private final int threadNum;
        private final int capacity;
        private int requestNum = 0;

        public Content(int thread, int capacity, ByteBuffer buffer) {
            threadNum = thread;
            this.buffer = buffer;
            this.capacity = capacity;
        }

        public int getRequestNum() {
            return requestNum;
        }

        public ByteBuffer getBuffer() {
            return buffer;
        }

        public int getThreadNum() {
            return threadNum;
        }

        private void inc() {
            requestNum++;
        }

        public boolean add() {
            inc();
            return requestNum < capacity;
        }
    }
}
