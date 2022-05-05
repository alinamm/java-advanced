package info.kgeorgiy.ja.mustafina.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HelloUDPClient implements HelloClient {
    private static final int TIME_OUT = 250;

    public static void main(String[] args) {
        if (args.length == 5 && Arrays.stream(args).allMatch(Objects::nonNull)) {
            HelloUDPClient helloUDPClient = new HelloUDPClient();
            helloUDPClient.run(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]),
                    Integer.parseInt(args[4]));
        } else {
            System.err.println("Error: Invalid arguments");
        }
    }

    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        try {
            var service = Executors.newFixedThreadPool(threads);
            var address = new InetSocketAddress(host, port);
            for (int threadNum = 0; threadNum < threads; threadNum++) {
                int finalThreadNum = threadNum;
                var socket = new DatagramSocket();
                int size = socket.getReceiveBufferSize();
                service.submit(() -> {
                    for (int requestNum = 0; requestNum < requests; requestNum++) {
                        process(requestNum, finalThreadNum, socket, address, size, prefix);
                    }
                });
            }
            service.shutdown();
            try {
                if (!service.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.err.println("Executor hasn't been shutdown");
                }
            } catch (InterruptedException e) {
                System.err.println("Error: Interrupted while waiting " + e.getMessage());
            }
        } catch (SocketException e) {
            System.err.println("Error: The socket could not be opened " + e.getMessage());
        }
    }

    public void process(int requestNum, int threadNum, DatagramSocket socket, InetSocketAddress address, int size, String prefix) {
        var requestPacket = new DatagramPacket(new byte[0], 0, address);
        var responsePacket = new DatagramPacket(new byte[size], size);
        try {
            socket.setSoTimeout(TIME_OUT);
        } catch (SocketException e) {
            System.err.println("Error: There is an error in the underlying protocol " + e.getMessage());
        }
        String response = "";
        String request = prefix + threadNum + "_" + requestNum;
        Charset utf8 = StandardCharsets.UTF_8;
        requestPacket.setData(request.getBytes(utf8));
        while (!Thread.interrupted()) {
            try {
                socket.send(requestPacket);
                socket.receive(responsePacket);
                response = new String(responsePacket.getData(), responsePacket.getOffset(), responsePacket.getLength(), utf8);
                if (response.equals("Hello, " + request)) {
                    break;
                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Request: " + request);
        System.out.println("Response: " + response);
    }
}
