package net.onlite.morplay.mongo;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.query.Query;
import com.mongodb.ReadPreference;
import net.onlite.morplay.async.AsyncIterable;
import org.bson.types.CodeWScope;
import play.libs.Akka;
import play.libs.F;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Responsible for querying from collection. Uses morphia query implementation internally.
 */
public class MongoQuery<T> {
    /**
     * Morphia query implementation
     */
    private Query<T> impl;

    /**
     * @param impl Morphia query implementation instance
     */
    public MongoQuery(Query<T> impl) {
        this.impl = impl;
    }

    public Query<T> getImpl() {
        return impl;
    }

    public MongoQuery<T> filter(String condition, Object value) {
        impl.filter(condition, value);
        return this;
    }

    public MongoQuery<T> where(String js) {
        impl.where(js);
        return this;
    }

    public MongoQuery<T> where(CodeWScope js) {
        impl.where(js);
        return this;
    }

    public MongoQuery<T> order(String field) {
        impl.order(field);
        return this;
    }

    public MongoQuery<T> limit(int value) {
        impl.limit(value);
        return this;
    }

    public MongoQuery<T> batchSize(int size) {
        impl.batchSize(size);
        return this;
    }

    public MongoQuery<T> offset(int value) {
        impl.offset(value);
        return this;
    }

    public MongoQuery<T> enableValidation() {
        impl.enableValidation();
        return this;
    }

    public MongoQuery<T> disableValidation() {
        impl.disableValidation();
        return this;
    }

    public MongoQuery<T> hintIndex(String indexName) {
        impl.hintIndex(indexName);
        return this;
    }

    public MongoQuery<T> retrievedFields(boolean include, String... fields) {
        impl.retrievedFields(include, fields);
        return this;
    }

    public MongoQuery<T> retrieveKnownFields() {
        impl.retrieveKnownFields();
        return this;
    }

    public MongoQuery<T> enableSnapshotMode() {
        impl.enableSnapshotMode();
        return this;
    }

    public MongoQuery<T> disableSnapshotMode() {
        impl.disableSnapshotMode();
        return this;
    }

    public MongoQuery<T> queryNonPrimary() {
        impl.queryNonPrimary();
        return this;
    }

    public MongoQuery<T> queryPrimaryOnly() {
        impl.queryPrimaryOnly();
        return this;
    }

    public MongoQuery<T> useReadPreference(ReadPreference readPreference) {
        impl.useReadPreference(readPreference);
        return this;
    }

    public MongoQuery<T> disableCursorTimeout() {
        impl.disableCursorTimeout();
        return this;
    }

    public MongoQuery<T> enableCursorTimeout() {
        impl.enableCursorTimeout();
        return this;
    }

    public Class<T> getEntityClass() {
        return impl.getEntityClass();
    }

    public F.Promise<T> get() {
        return Akka.future(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return impl.get();
            }
        });
    }

    public F.Promise<Key<T>> getKey() {
        return Akka.future(new Callable<Key<T>>() {
            @Override
            public Key<T> call() throws Exception {
                return impl.getKey();
            }
        });
    }

    public F.Promise<List<T>> asList() {
        return Akka.future(new Callable<List<T>>() {
            @Override
            public List<T> call() throws Exception {
                return impl.asList();
            }
        });
    }

    public F.Promise<List<Key<T>>> asKeyList() {
        return Akka.future(new Callable<List<Key<T>>>() {
            @Override
            public List<Key<T>> call() throws Exception {
                return impl.asKeyList();
            }
        });
    }

    public AsyncIterable<T> fetch() {
        return new AsyncIterable<>(impl.fetch());
    }

    public AsyncIterable<T> fetchEmptyEntities() {
        return new AsyncIterable<>(impl.fetchEmptyEntities());
    }

    public AsyncIterable<Key<T>> fetchKeys() {
        return new AsyncIterable<>(impl.fetchKeys());
    }

    public F.Promise<Long> countAll() {
        return Akka.future(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return impl.countAll();
            }
        });
    }

    public AsyncIterable<T> tail() {
        return new AsyncIterable<>(new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return impl.tail();
            }
        });
    }

    public AsyncIterable<T> tail(final boolean awaitData) {
        return new AsyncIterable<>(new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return impl.tail(awaitData);
            }
        });
    }
}
