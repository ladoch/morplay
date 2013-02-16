package net.onlite.morplay.async;

import akka.japi.Function;
import akka.japi.Procedure;
import play.libs.Akka;
import play.libs.F;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Extend Iterable with asynchronous functional style features.
 */
public class AsyncIterable<T> implements Iterable<T> {
    /**
     * Synchronous implementation
     */
    private Iterable<T> iterable;

    /**
     * @param iterable Synchronous implementation
     */
    public AsyncIterable(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    /**
     * Map items asynchronously
     * @param mapFunc Map item function
     * @param <V> Result item type
     * @return Promise reference
     */
    public <V> F.Promise<Iterable<V>> map(final Function<T, V> mapFunc) {
        return Akka.future(new Callable<Iterable<F.Promise<? extends V>>>() {
            @Override
            public Iterable<F.Promise<? extends V>> call() throws Exception {
                List<F.Promise<? extends V>> result = new LinkedList<>();
                for (final T item : iterable) {
                    result.add(Akka.future(new Callable<V>() {
                        @Override
                        public V call() throws Exception {
                            return mapFunc.apply(item);
                        }
                    }));
                }

                return result;
            }
        }).flatMap(new F.Function<Iterable<F.Promise<? extends V>>, F.Promise<List<V>>>() {
            @Override
            public F.Promise<List<V>> apply(Iterable<F.Promise<? extends V>> promises) throws Throwable {
                return F.Promise.sequence(promises);
            }
        }).map(new F.Function<List<V>, Iterable<V>>() {
            @Override
            public Iterable<V> apply(List<V> vs) throws Throwable {
                return vs;
            }
        });
    }

    /**
     * Map reduce items asynchronously
     * @param mapFunc Map item function
     * @param reduceFunc Reduce function
     * @param <R> Result item type
     * @return Promise reference
     */
    public <R, V> F.Promise<R> mapReduce(final Function<T, V> mapFunc,
                                         final Function<Iterable<V>, R> reduceFunc) {

        return map(mapFunc).map(new F.Function<Iterable<V>, R>() {
            @Override
            public R apply(Iterable<V> vs) throws Throwable {
                return reduceFunc.apply(vs);
            }
        });

    }

    /**
     * Apply procedure for each iem
     * @param procedure Procedure to apply
     * @return Runnable to execute
     */
    public Runnable forEach(final Procedure<T> procedure, final Duration timeout) {
        return new Runnable() {
            @Override
            public void run() {
                List<F.Promise<? extends Void>> result = new LinkedList<>();
                for (final T item : iterable) {
                    result.add(Akka.future(new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            procedure.apply(item);
                            return null;
                        }
                    }));
                }

                try {
                    Await.ready(F.Promise.sequence(result).getWrappedPromise(), timeout);
                } catch (Exception e) {
                    // TODO: logs
                }
            }
        };
    }

    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }
}
