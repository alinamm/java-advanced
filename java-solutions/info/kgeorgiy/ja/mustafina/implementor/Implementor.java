package info.kgeorgiy.ja.mustafina.implementor;

import java.io.*;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.Objects;
import java.util.function.Function;
import java.util.jar.*;
import java.util.stream.*;
import java.util.zip.ZipEntry;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * Class Implementor
 */
public class Implementor implements Impler, JarImpler {
    // :NOTE: javadoc
    private static final String TAB = "\t";
    private static final String NEW_LINE = System.lineSeparator();


    /**
     * Checks if arguments are correct and then calls {@link Implementor#implement} or {@link Implementor#implementJar}
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        try {
            Implementor implementor = new Implementor();
            if (args.length == 3 && Objects.equals(args[0], "/jar")) {
                implementor.implementJar(Class.forName(args[1]), Path.of(args[2]));
            }
            if (args.length == 1) {
                implementor.implement(Class.forName(args[0]), Path.of(""));
            }
        } catch (ImplerException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Can`t find class " + e.getMessage());
        }
    }

    /**
     * Produces code implementing interface specified by provided {@code token}.
     * <p>
     * Generated class classes name should be same as classes name of the type token with {@code Impl} suffix
     * added. Generated source code should be placed in the correct subdirectory of the specified
     * {@code root} directory and have correct file name. For example, the implementation of the
     * interface {@link java.util.List} should go to {@code $root/java/util/ListImpl.java}
     *
     * @param token type token to create implementation for.
     * @param root  root directory.
     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when implementation cannot be
     *                                                                 generated.
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        int modifiers = token.getModifiers();
        if (Modifier.isPrivate(modifiers) || Modifier.isFinal(modifiers) || !Modifier.isInterface(modifiers)) {
            throw new ImplerException("Invalid token");
        }
        try {
            Path path = Path.of(root + "/" + token.getPackageName().replace(".", "/"));
            Files.createDirectories(path);
            Path file = path.resolve(token.getSimpleName() + "Impl.java");
            String output = token.getPackageName().isEmpty() ? "" : ("package " + token.getPackageName() + ";" + NEW_LINE)
                    + " public class " + token.getSimpleName() + "Impl " + "implements " + token.getCanonicalName()
                    + "{" + NEW_LINE + methods(token) + NEW_LINE + "}";
            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(file)) {
                for (char ch : output.toCharArray()) {
                    bufferedWriter.write(ch < 128 ? String.valueOf(ch) : String.format("\\u%04x", (int) ch));
                }
            } catch (IOException e) {
                throw new ImplerException("Output exception " + e.getMessage());
            }
        } catch (IOException e) {
            throw new ImplerException("Failed create directories " + e.getMessage());
        }
    }

    /**
     * Produces <var>.jar</var> file implementing interface specified by provided <var>token</var>.
     * <p>
     * Generated class classes name should be same as classes name of the type token with <var>Impl</var> suffix
     * added.
     *
     * @param token   type token to create implementation for.
     * @param jarFile target <var>.jar</var> file.
     * @throws ImplerException when implementation cannot be generated.
     */
    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        Manifest manifest = new Manifest();
        implement(token, jarFile.getParent());
        compile(jarFile.getParent(), token);
        String path = token.getPackageName().replace(".", "/") +
                "/" + token.getSimpleName() + "Impl.class";
        try (JarOutputStream jarOutputStream = new JarOutputStream(Files.newOutputStream(jarFile), manifest)) {
            jarOutputStream.putNextEntry(new ZipEntry(path));
            Files.copy(Path.of(jarFile.getParent() + "/" + path), jarOutputStream);
        } catch (IOException e) {
            throw new ImplerException("Output exception occurred " + e.getMessage());
        }
    }

    /**
     * Compiles classes
     *
     * @param root    path to classes we have to compile
     * @param classes classes we have to compile
     */
    public static void compile(final Path root, final Class<?>... classes) {
        final List<String> files = new ArrayList<>();
        for (final Class<?> token : classes) {
            files.add((root.resolve((token.getPackageName() + "." + token.getSimpleName() + "Impl").
                    replace(".", File.separator) + ".java").toAbsolutePath()).toString());
        }
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        final String classpath;
        try {
            classpath = root + File.pathSeparator
                    + Path.of(JarImpler.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (final URISyntaxException e) {
            throw new AssertionError(e);
        }
        final String[] args = Stream.concat(files.stream(), Stream.of("-cp", classpath)).toArray(String[]::new);
        compiler.run(null, null, null, args);

    }

    /**
     * Generate methods code
     *
     * @param token type token from which we get methods
     * @return generated methods
     */
    private String methods(Class<?> token) {
        return mapAndCollect(token.getMethods(),
                s -> TAB + modifiers(s) + " " + s.getReturnType().getCanonicalName() + " " + s.getName()
                        + "(" + parameters(s) + ") " + exceptions(s) + "{" + NEW_LINE + TAB + TAB
                        + "return " + returnValue(s) + ";" + NEW_LINE + TAB + "}" + NEW_LINE, "");
    }

    /**
     * Check return value of method
     *
     * @param method method which return value is checked
     * @return return value of methods
     */
    private String returnValue(Method method) {
        return String.valueOf(method.getReturnType() == boolean.class ? true : method.getReturnType() == void.class ? ""
                : method.getReturnType().isPrimitive() ? 0 : null);
    }

    /**
     * Generate exceptions code if there are any
     *
     * @param method method which can throw exceptions
     * @return generated exceptions code or empty string if there aren't any
     */
    private String exceptions(Method method) {
        Class<?>[] exceptions = method.getExceptionTypes();
        return exceptions.length > 0 ? "throws " + mapAndCollect(exceptions, Class::getCanonicalName, ", ") : "";
    }

    /**
     * Generate code of method's parameters
     *
     * @param method method for which parameters code is generating
     * @return generated code for parameters
     */
    private String parameters(Method method) {
        return mapAndCollect(method.getParameters(), m -> m.getType().getCanonicalName()
                + " " + m.getName(), ", ");
    }

    /**
     * Generate code of method's modifiers and remove private, abstract and transient modifiers
     * @see Modifier#TRANSIENT
     * @see Modifier#ABSTRACT
     * @see Modifier#PRIVATE
     *
     * @param method method for which modifiers code is generating
     * @return generated code of modifiers
     */
    private String modifiers(Method method) {
        return Modifier.toString(method.getModifiers() & ~Modifier.PRIVATE & ~Modifier.ABSTRACT & ~Modifier.TRANSIENT);
    }

    /**
     * Streams array then applies function to each element and join elements with separator
     *
     * @param array    array to which operations will be applied
     * @param function function to apply to each element
     * @param s        the delimiter to be used between each element
     * @return elements of array after applying function joined with delimiter
     */
    private <R> String mapAndCollect(R[] array, Function<R, String> function, String s) {
        return Arrays.stream(array).map(function).collect(Collectors.joining(s));
    }
}
