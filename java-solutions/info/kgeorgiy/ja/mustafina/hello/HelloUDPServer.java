package info.kgeorgiy.ja.mustafina.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HelloUDPServer implements HelloServer {
    private ExecutorService service;
    private DatagramSocket socket;

    public static void main(String[] args) {
        if (args.length == 2 && Arrays.stream(args).allMatch(Objects::nonNull)) {
            HelloUDPServer helloUDPServer = new HelloUDPServer();
            helloUDPServer.start(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } else {
            System.err.println("Error: Invalid arguments");
        }
    }

    @Override
    public void start(int port, int threads) {
        try {
            socket = new DatagramSocket(port);
            service = Executors.newFixedThreadPool(threads);
            service.submit(() -> {
                while (!Thread.interrupted() && !socket.isClosed()) {
                    try {
                        int size = socket.getSendBufferSize();
                        DatagramPacket packet = new DatagramPacket(new byte[size], size);
                        try {
                            socket.receive(packet);
                            Charset utf8 = StandardCharsets.UTF_8;
                            packet.setData(("Hello, " + new String(packet.getData(), packet.getOffset(),
                                    packet.getLength(), utf8)).getBytes(utf8));
                            socket.send(packet);
                        } catch (IOException e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                    } catch (SocketException e) {
                        System.err.println("Error: Here is an error in the underlying protocol " + e.getMessage());
                    }
                }
            });
        } catch (SocketException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        socket.close();
        service.shutdown();
        try {
            if (!service.awaitTermination(10, TimeUnit.SECONDS)) {
                System.err.println("Error: Executor hasn't been shutdown");
            }
        } catch (InterruptedException e) {
            System.err.println("Error: Interrupted while waiting " + e.getMessage());
        }
    }
}