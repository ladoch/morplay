package net.onlite.morplay.mongo;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.query.Query;

/**
 * Responsible for operations on collection.
 */
public abstract class MongoCollection<T> {
    protected final Class<T> entityClass;
    protected final Datastore ds;

    /**
     * @param entityClass Entity class
     * @param ds Morphia data store
     */
    public MongoCollection(Class<T> entityClass, Datastore ds) {
        this.entityClass = entityClass;
        this.ds = ds;
    }

    /**
     * Create find query
     * @param filters Query filters
     * @return Query wrapper
     */
    public MongoQuery<T> find(Filter... filters) {
        Query<T> query = ds.find(entityClass);

        for (Filter filter : filters) {
            query.filter(filter.getCriteria(), filter.getValue());
        }

        return new MongoQuery<>(query);
    }

    /**
     * Find document by id
     * @param id Document id
     * @return Entity instance or null
     */
    public T findById(Object id) {
        return ds.getByKey(entityClass, new Key<>(entityClass, id));
    }

    /**
     * Create document
     * @param entity Entity
     * @return Key
     */
    public Key<T> create(T entity) {
        return ds.save(entity);
    }

    /**
     * Remove documents
     * @param filters Query filters
     */
    public void remove(Filter... filters) {
        Query<T> query = ds.find(entityClass);

        for (Filter filter : filters) {
            query.filter(filter.getCriteria(), filter.getValue());
        }

        ds.delete(query);
    }

    /**
     * Remove document by id
     * @param id Document id
     */
    public void removeById(Object id) {
        Query<T> query = ds.find(entityClass).filter("_id", id);
        ds.delete(query);
    }

    /**
     * Create atomic update operation (single update)
     * @param filters Query filters
     * @return Atomic operation wrapper
     */
    public abstract AtomicOperation<T> atomic(Filter... filters);

    /**
     * Create atomic update operation
     * @param entity Entity to update
     * @return Atomic operation wrapper
     */
    public abstract AtomicOperation<T> atomic(T entity);

    /**
     * Create atomic update operation (multi update)
     * @param filters Query filters
     * @return Atomic operation wrapper
     */
    public abstract AtomicOperation<T> atomicAll(Filter... filters);

}
