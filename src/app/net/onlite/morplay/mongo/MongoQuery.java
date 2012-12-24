package net.onlite.morplay.mongo;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.query.Query;
import com.mongodb.ReadPreference;
import org.bson.types.CodeWScope;

import java.util.Iterator;
import java.util.List;

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

    public T get() {
        return impl.get();
    }

    public Key<T> getKey() {
        return impl.getKey();
    }

    public List<T> asList() {
        return impl.asList();
    }

    public List<Key<T>> asKeyList() {
        return impl.asKeyList();
    }

    public Iterable<T> fetch() {
        return impl.fetch();
    }

    public Iterable<T> fetchEmptyEntities() {
        return impl.fetchEmptyEntities();
    }

    public Iterable<Key<T>> fetchKeys() {
        return impl.fetchKeys();
    }

    public long countAll() {
        return impl.countAll();
    }

    public Iterator<T> tail() {
        return impl.tail();
    }

    public Iterator<T> tail(boolean awaitData) {
        return impl.tail(awaitData);
    }

    public Iterator<T> iterator() {
        return impl.iterator();
    }
}
