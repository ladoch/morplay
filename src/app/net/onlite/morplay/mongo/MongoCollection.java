package net.onlite.morplay.mongo;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.query.Query;
import com.mongodb.WriteResult;
import play.libs.Akka;
import play.libs.F;

import java.util.concurrent.Callable;

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
    public F.Promise<T> findById(final Object id) {
        return Akka.future(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return ds.getByKey(entityClass, new Key<>(entityClass, id));
            }
        });
    }

    /**
     * Create document
     * @param entity Entity
     * @return Key
     */
    public F.Promise<Key<T>> create(final T entity) {
        return Akka.future(new Callable<Key<T>>() {
            @Override
            public Key<T> call() throws Exception {
                return ds.save(entity);
            }
        });
    }

    /**
     * Remove documents
     * @param filters Query filters
     */
    public F.Promise<WriteResult> remove(final Filter... filters) {
        return Akka.future(new Callable<WriteResult>() {
            @Override
            public WriteResult call() throws Exception {
                Query<T> query = ds.find(entityClass);

                for (Filter filter : filters) {
                    query.filter(filter.getCriteria(), filter.getValue());
                }

                return ds.delete(query);
            }
        });
    }

    /**
     * Remove document by id
     * @param id Document id
     */
    public F.Promise<WriteResult> removeById(final Object id) {
        return Akka.future(new Callable<WriteResult>() {
            @Override
            public WriteResult call() throws Exception {
                Query<T> query = ds.find(entityClass).filter("_id", id);
                return ds.delete(query);
            }
        });
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
