package info.kgeorgiy.ja.mustafina.hello;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class General {
    public final int TIME_OUT = 100;
    public final String IOEXCEPTION_MESSAGE = "Error: Input or Output error occurs ";

    public void clientMain(String[] args) {
        if (check(args, 5)) {
            HelloUDPClient helloUDPClient = new HelloUDPClient();
            try {
                helloUDPClient.run(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]),
                        Integer.parseInt(args[4]));
            } catch (NumberFormatException e) {
                System.err.println("Error: Invalid type of arguments");
            }
        }
    }

    public void serverMain(String[] args) {
        if (check(args, 2)) {
            HelloUDPServer helloUDPServer = new HelloUDPServer();
            try {
                helloUDPServer.start(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            } catch (NumberFormatException e) {
                System.err.println("Error: Invalid type of arguments");
            }
        }
    }

    private boolean check(String[] args, int num) {
        if (args != null && args.length == num && Arrays.stream(args).allMatch(Objects::nonNull)) {
            return true;
        } else {
            System.err.println("Error: Invalid arguments");
        }
        return false;
    }

    public void close(ExecutorService service) {
        service.shutdown();
        try {
            if (!service.awaitTermination(TIME_OUT, TimeUnit.SECONDS)) {
                System.err.println("Error: Executor hasn't been shutdown");
            }
        } catch (InterruptedException e) {
            System.err.println("Error: Interrupted while waiting " + e.getMessage());
        }
    }

    public void select(Selector selector) {
        try {
            selector.select(TIME_OUT);
        } catch (IOException e) {
            System.err.println("Error: Input or Output error occurs" + e.getMessage());
        }
    }

    public boolean match(String prefix, int threadNum, int requestNum, String response) {
        String text = "\\D*";
        return response.matches(text + prefix + threadNum + "_" + requestNum);
    }

    public void write(String request, String response) {
        System.out.println("Request: " + request);
        System.out.println("Response: " + response);
    }
}
