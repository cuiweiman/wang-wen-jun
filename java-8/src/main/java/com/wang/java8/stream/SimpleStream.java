package com.wang.java8.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @description: stream demo
 * @author: weiman cui
 * @date: 2020/6/22 9:40
 */
public class SimpleStream {

    public static Stream<String> createStreamFromFile() {
        Path path = Paths.get("E:\\IdeaProjects\\java8\\src\\main\\java\\com\\wang\\java8\\stream\\SimpleStream.java");
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(System.out::println);
            return stream;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<Integer> createStreamFromIterator() {
        Stream<Integer> iterate = Stream.iterate(0, n -> n + 2).limit(100);
        return iterate;
    }

    public static Stream<Double> createStreamFromGenerate() {
        Stream<Double> iterate = Stream.generate(Math::random).limit(100);
        return iterate;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Obj {
        private int id;
        private String name;
    }

    public static class ObjSupplier implements Supplier<Obj> {
        private int index = 0;
        private Random random = new Random(System.currentTimeMillis());

        @Override
        public Obj get() {
            index = random.nextInt(100);
            return new Obj(index, "Name->" + index);
        }
    }

    public static Stream<Obj> createObjStreamFromGenerate() {
        return Stream.generate(new ObjSupplier()).limit(100);
    }


    public static void main(String[] args) {
        // System.out.println(createStreamFromFile());
        // createStreamFromFile();
        // createStreamFromIterator().forEach(System.out::println);
        // createStreamFromGenerate().forEach(System.out::println);
        createObjStreamFromGenerate().forEach(System.out::println);
    }


}
