package test;

import akka.japi.Function;
import akka.japi.Procedure;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import net.onlite.morplay.async.AsyncIterable;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

/**
 * Tests AsyncIterable class
 */
public class AsyncIterableTests {
    @Test
    public void mapTest() {
        final Iterable<Integer> source = Arrays.asList(3, 5, 12, 7, 9);
        final Iterable<String> expected = Iterables.transform(source, new com.google.common.base.Function<Integer, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Integer integer) {
                return String.valueOf(integer);
            }
        });

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                AsyncIterable<Integer> test = new AsyncIterable<>(source);

                Iterable<String> result = test.map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return String.valueOf(integer);
                    }
                }).get();

                assertThat(Iterables.elementsEqual(result, expected)).isEqualTo(true);
            }
        });
    }

    @Test
    public void mapReduceTest() {
        final Iterable<Integer> source = Arrays.asList(3, 5, 12, 7, 9);
        final String expected = StringUtils.join(Iterables.transform(source, new com.google.common.base.Function<Integer, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Integer integer) {
                return String.valueOf(integer);
            }
        }).iterator(), ",");

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                AsyncIterable<Integer> test = new AsyncIterable<>(source);

                String result = test.mapReduce(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return String.valueOf(integer);
                    }
                }, new Function<Iterable<String>, String>() {
                    @Override
                    public String apply(Iterable<String> strings) throws Exception {
                        return StringUtils.join(strings.iterator(), ",");
                    }
                }).get();

                assertThat(result).isEqualTo(expected);
            }
        });

    }

    @Test
    public void forEachTest() {
        final Iterable<Integer> source = Arrays.asList(3, 5, 12, 7, 9);
        final Set<Integer> testSet = Sets.newHashSet(source);

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                AsyncIterable<Integer> test = new AsyncIterable<>(source);

                Runnable operation = test.forEach(new Procedure<Integer>() {
                    @Override
                    public void apply(Integer integer) throws Exception {
                        testSet.remove(integer);
                    }
                });

                operation.run();

                assertThat(testSet).isEmpty();
            }
        });
    }
}
