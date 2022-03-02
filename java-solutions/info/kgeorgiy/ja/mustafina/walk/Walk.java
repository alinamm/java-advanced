package info.kgeorgiy.ja.mustafina.walk;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Walk {
    public static String ERROR_WHILE_READING_FILE = "0".repeat(40);

    public static void main(String[] args) {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            System.err.println("Invalid arguments");
            return;
        }

        if (checkPath(args[0], "input")) {
            return;
        }
        if (checkPath(args[1], "output")) {
            return;
        }

        File outputFile = new File(args[1]);
        if (outputFile.getParent() != null && !outputFile.getParentFile().isDirectory()) {
            try {
                Path path = Paths.get(outputFile.getPath());
                Files.createDirectories(path);
            } catch (IOException e) {
                System.err.println("Failed to create parent directory" + " " + e.getMessage());
                return;
            }
        }

        try (BufferedReader reader = Files.newBufferedReader(Path.of(args[0]));
             BufferedWriter writer = Files.newBufferedWriter(Path.of(args[1]))) {

            String path;
            while ((path = reader.readLine()) != null) {
                StringBuilder result = new StringBuilder();
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                Path filePath = null;
                try {
                    filePath = Path.of(path);
                } catch (InvalidPathException e) {
                    result.append(ERROR_WHILE_READING_FILE);
                }
                if (filePath != null && Files.notExists(filePath)) {
                    result.append(ERROR_WHILE_READING_FILE);
                }
                if (result.length() == 0) {
                    try (InputStream inputStream = Files.newInputStream(Path.of(path))) {
                        int n = 0;
                        byte[] buffer = new byte[8000];
                        while (n != -1) {
                            n = inputStream.read(buffer);
                            if (n > 0) {
                                md.update(buffer, 0, n);
                            }
                        }
                        for (byte b : md.digest()) {
                            result.append(String.format("%02x", b));
                        }
                    } catch (IllegalArgumentException | UnsupportedOperationException | IOException | SecurityException | NullPointerException e) {
                        result.append(ERROR_WHILE_READING_FILE);
                    }
                }
                writer.write(result + " " + path + "\n");
            }
        } catch (IOException e) {
            System.err.println("Input or output exception" + " " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Failed to find algorithm" + " " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Security exception" + " " + e.getMessage());
        }
    }

    public static boolean checkPath(String path, String message) {
        try {
            Path filePath = Path.of(path);
        } catch (InvalidPathException e) {
            System.err.println("Invalid path to " + message + " file" + " " + e.getMessage());
            return true;
        }
        return false;
    }
}
