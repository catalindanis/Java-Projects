import java.time.*;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

import java.util.*;
import java.time.Instant;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ValueProvider provider = new ValueProvider();
        int sum = 0;
        for(int i=0;i<provider.getValues().size();i++)
            if(provider.getValues().get(i).isPresent())
                sum += provider.getValues().get(i).get();
        System.out.println(sum);
    }
}

/* Please, do not modify the code below */
class AddressBook {
    private static Map<String, String> namesToAddresses = new HashMap<>();

    static {
        namesToAddresses.put("Pansy Barrows", "63 Shub Farm Drive, Cumberland, RI 02864");
        namesToAddresses.put("Kevin Bolyard", "9526 Front Court, Hartsville, SC 29550");
        namesToAddresses.put("Earl Riley", "9197 Helen Street, West Bloomfield, MI 48322");
        namesToAddresses.put("Christina Doss", "7 Lincoln St., Matawan, NJ 07747");
    }

    static Optional<String> getAddressByName(String name) {
        return Optional.ofNullable(namesToAddresses.get(name));
    }
}

class ValueProvider {
    private List<Optional<Integer>> opts; // cache to provide reproducing method invocation

    public List<Optional<Integer>> getValues() {
        if (opts != null) {
            return opts;
        }

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int number = scanner.nextInt();
        opts = java.util.stream.IntStream
                .rangeClosed(1, number)
                .mapToObj(n -> {
                    String val = scanner.next();
                    return "null".equals(val) ?
                            Optional.<Integer>empty() :
                            Optional.of(Integer.valueOf(val));
                })
                .collect(java.util.stream.Collectors.toList());

        return opts;
    }
}





class Calculator {

    public int maxOf(int a, int b) {
        if (a >= b) {
            return a;
        } else {
            return b;
        }
    }
}

class Operator {

    public static <T, U> Function<T, U> ternaryOperator(
            Predicate<? super T> condition,
            Function<? super T, ? extends U> ifTrue,
            Function<? super T, ? extends U> ifFalse) {

        return t -> condition.test(t) ? ifTrue.apply(t) : ifFalse.apply(t);
    }
}

class Time {

    private int hours;
    private int minutes;
    private int seconds;

    public Time(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        if(hours < 10)
            result.append("0"+String.valueOf(hours)+":");
        else result.append(String.valueOf(hours)+":");

        if(minutes < 10)
            result.append("0"+String.valueOf(minutes)+":");
        else result.append(String.valueOf(minutes)+":");

        if(seconds < 10)
            result.append("0"+String.valueOf(seconds));
        else result.append(String.valueOf(seconds));

        return result.toString();
    }
}

class Person {

    protected String name;
    protected int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String toString(){
        return String.format("Person{name=%s,age=%d}",name,age);
    }
}

class Patient extends Person {

    protected int height;

    public Patient(String name, int age, int height) {
        super(name, age);
        this.height = height;
    }

    public String toString(){
        return String.format("Patient{name=%s,age=%d,height=%d}",name,age,height);
    }
}

