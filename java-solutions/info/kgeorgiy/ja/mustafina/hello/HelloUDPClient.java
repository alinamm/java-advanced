package info.kgeorgiy.ja.mustafina.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.charset.*;
import java.util.concurrent.*;

public class HelloUDPClient implements HelloClient {
    private static final General general = new General();

    public static void main(String[] args) {
        general.clientMain(args);
    }

    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        var service = Executors.newFixedThreadPool(threads);
        var address = new InetSocketAddress(host, port);
        for (int threadNum = 0; threadNum < threads; threadNum++) {
            int finalThreadNum = threadNum;
            service.submit(() -> {
                try (var socket = new DatagramSocket()) {
                    int size = socket.getReceiveBufferSize();
                    for (int requestNum = 0; requestNum < requests; requestNum++) {
                        process(requestNum, finalThreadNum, socket, address, size, prefix);
                    }
                } catch (SocketException e) {
                    System.err.println("Error: " + e.getMessage());
                }
            });
        }
        general.close(service);
    }

    public void process(int requestNum, int threadNum, DatagramSocket socket, InetSocketAddress address, int size, String prefix) {
        var requestPacket = new DatagramPacket(new byte[0], 0, address);
        var responsePacket = new DatagramPacket(new byte[size], size);
        try {
            socket.setSoTimeout(general.TIME_OUT);
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
                if (general.match(prefix, threadNum, requestNum, response)) {
                    break;
                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        general.write(request, response);
    }
}
