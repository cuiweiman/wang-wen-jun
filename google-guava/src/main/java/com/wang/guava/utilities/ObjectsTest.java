package com.wang.guava.utilities;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import java.util.Calendar;

/**
 * <p>
 * Objects & MoreObjects & ComparisonChain
 * 构造类中的 toString()、hashCode()、equals()、compareTo()方法
 * </p>
 *
 * @description: 可用可不用的 工具类； 别用了，真不方便。
 * @date: 2020/7/23 22:52
 * @author: wei·man cui
 */
public class ObjectsTest {
    public static void main(String[] args) {
        Guava guava = new Guava("Google", "29.0-jre", Calendar.getInstance());
        System.out.println(guava);
        System.out.println(guava.hashCode());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Guava guava2 = new Guava("Google", "29.0-jre", calendar);
        System.out.println(guava.compareTo(guava2));

    }

    static class Guava implements Comparable<Guava> {
        private final String menuFactory;
        private final String version;
        private final Calendar releaseDate;

        public Guava(String menuFactory, String version, Calendar releaseDate) {
            this.menuFactory = menuFactory;
            this.version = version;
            this.releaseDate = releaseDate;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .omitNullValues()
                    .add("menuFactory", this.menuFactory)
                    .add("version", this.version)
                    .add("releaseDate", this.releaseDate)
                    .toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Guava guava = (Guava) o;
            return Objects.equal(this.menuFactory, guava.menuFactory)
                    && Objects.equal(this.version, guava.version)
                    && Objects.equal(this.releaseDate, guava.releaseDate);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(menuFactory, version, releaseDate);
        }

        @Override
        public int compareTo(Guava o) {
            return ComparisonChain.start()
                    .compare(this.menuFactory, o.menuFactory)
                    .compare(this.version, o.version)
                    .compare(this.releaseDate, o.releaseDate)
                    .result();
        }
    }
}
