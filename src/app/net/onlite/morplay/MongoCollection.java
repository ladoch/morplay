package net.onlite.morplay;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.query.Query;

/**
 * Responsible for operations on collection.
 */
public abstract class MongoCollection<T> {
    protected final Class<T> entityClass;
    protected final Datastore ds;

    public MongoCollection(Class<T> entityClass, Datastore ds) {
        this.entityClass = entityClass;
        this.ds = ds;
    }

    public Query<T> query(Filter... filters) {
        Query<T> query = ds.find(entityClass);

        for (Filter filter : filters) {
            query.filter(filter.getCriteria(), filter.getValue());
        }

        return query;
    }

    public Key<T> create(T entity) {
        return ds.save(entity);
    }

    public void remove(Filter... filters) {
        Query<T> query = ds.find(entityClass);

        for (Filter filter : filters) {
            query.filter(filter.getCriteria(), filter.getValue());
        }

        ds.delete(query);
    }

    public abstract AtomicOperation<T> atomic(Filter... filters);

    public abstract AtomicOperation<T> atomicAll(Filter... filters);

}
