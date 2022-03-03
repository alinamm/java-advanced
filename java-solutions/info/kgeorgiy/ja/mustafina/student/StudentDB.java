package info.kgeorgiy.ja.mustafina.student;

import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.GroupName;
import info.kgeorgiy.java.advanced.student.StudentQuery;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentDB implements StudentQuery {

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return getInfo(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return getInfo(students, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return getInfo(students, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return getInfo(students, (s) -> s.getFirstName() + " " + s.getLastName());
    }

    public static <R> List<R> getInfo(List<Student> students, Function<Student, R> function) {
        return streamAndMap(students, function).toList();
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return streamAndMap(students, Student::getFirstName).sorted().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static <R> Stream<R> streamAndMap(List<Student> students, Function<Student, R> function) {
        return students.stream().map(function);
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream().max(Student::compareTo).map(Student::getFirstName).orElse("");
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sortStudentsByInfo(students, Student::compareTo);
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return orderStudentsByName(students);
    }

    public static List<Student> orderStudentsByName(Collection<Student> students) {
        return sortStudentsByInfo(students, Comparator.comparing(Student::getLastName, Comparator.reverseOrder()).
                thenComparing(Student::getFirstName, Comparator.reverseOrder()).thenComparing(Student::getId));
    }

    public static <T> List<T> sortStudentsByInfo(Collection<T> students, Comparator<T> comparator) {
        return students.stream().sorted(comparator).toList();
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return findStudentsByInfo(students, (s) -> s.getFirstName().equals(name));
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return findStudentsByInfo(students, (s) -> s.getLastName().equals(name));
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return findStudentsByInfo(students, (s) -> s.getGroup().equals(group));
    }

    public static List<Student> findStudentsByInfo(Collection<Student> students, Predicate<Student> predicate) {
        return orderStudentsByName(streamAndFilter(students, predicate).toList());
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return streamAndFilter(students, (s) -> s.getGroup().equals(group))
                .collect(Collectors.toMap(Student::getLastName, Student::getFirstName, BinaryOperator.minBy(Comparator.naturalOrder())));
    }

    public static Stream<Student> streamAndFilter(Collection<Student> students, Predicate<Student> predicate) {
        return students.stream().filter(predicate);
    }
}
