package info.kgeorgiy.ja.mustafina.implementor;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.*;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

public class Implementor implements Impler {
    private static final String TAB = "\t";
    private static final String NEW_LINE = System.lineSeparator();

    public static void main(String[] args) {
        try {
            Implementor implementor = new Implementor();
            implementor.implement(Class.forName(args[0]), Path.of(""));
        } catch (ImplerException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Can`t find class " + e.getMessage());
        }
    }

    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        int modifiers = token.getModifiers();
        // :NOTE: check interface
        if (Modifier.isPrivate(modifiers) || Modifier.isFinal(modifiers)) {
            throw new ImplerException("Invalid token");
        }
        try {
            Path path = Path.of(root + "/" + token.getPackageName().replace(".", "/"));
            Files.createDirectories(path);
            Path file = path.resolve(token.getSimpleName() + "Impl.java");
            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(file)) {
                bufferedWriter.write(token.getPackageName().isEmpty() ? "" : ("package " + token.getPackageName() + ";" + NEW_LINE)
                        // :NOTE: simpleName instead of canonicalName
                        + " public class " + token.getSimpleName() + "Impl " + "implements " + token.getCanonicalName()
                        + "{" + NEW_LINE + methods(token) + NEW_LINE + "}");
            } catch (IOException e) {
                throw new ImplerException("Output exception " + e.getMessage());
            }
        } catch (IOException e) {
            throw new ImplerException("Failed create directories " + e.getMessage());
        }
    }

    // :NOTE: too long and complicated one-liner method
    private String methods(Class<?> token) {
        return mapAndCollect(token.getMethods(),
                // :NOTE: private methods
                s -> TAB + Modifier.toString(s.getModifiers() & ~Modifier.ABSTRACT & ~Modifier.TRANSIENT) + " "
                        + s.getReturnType().getCanonicalName() + " " + s.getName()
                        // :NOTE: private return type, parameters
                        // :NOTE: missing exceptions
                        + "(" + mapAndCollect(s.getParameters(), m -> m.getType().getCanonicalName()
                        + " " + m.getName(), ", ") + ") " + "{" + NEW_LINE + TAB + TAB + "return "
                        + (s.getReturnType() == boolean.class ? true : s.getReturnType() == void.class ? ""
                        : s.getReturnType().isPrimitive() ? 0 : null) + ";" + NEW_LINE + TAB + "}" + NEW_LINE, "");
    }

    private <R> String mapAndCollect(R[] array, Function<R, String> function, String s) {
        return Arrays.stream(array).map(function).collect(Collectors.joining(s));
    }

    // :NOTE:
//    private class A {}
//
//    public interface I {
//        private A foo(A a) {
//            return a;
//        }
//    }
}
