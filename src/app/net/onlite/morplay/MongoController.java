package net.onlite.morplay;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;

/**
 * Base controller with helper methods for using morphia
 */
public class MongoController {
    /**
     * Get collection from default database
     * @param entityClass Entity class instance
     * @param <T> Entity type
     * @return Collection wrapper
     */
    protected static <T> MongoCollection<T> collection(Class<T> entityClass) {
        return collection(entityClass, null);
    }

    /**
     * Get collection from default database
     * @param entityClass Entity class instance
     * @param dbName Database name
     * @param <T> Entity type
     * @return Collection wrapper
     */
    protected static <T> MongoCollection<T> collection(Class<T> entityClass, String dbName) {
        class TAtomicOperation extends AtomicOperation<T> {
            public TAtomicOperation(Datastore ds, Query<T> query, boolean multiple) {
                super(ds, query, multiple);
            }
        }

        class TMongoCollection extends MongoCollection<T> {
            public TMongoCollection(Class<T> entityClass, Datastore ds) {
                super(entityClass, ds);
            }

            @Override
            public AtomicOperation<T> atomic(Filter... filters) {
                return new TAtomicOperation(ds, query(filters), false);
            }

            @Override
            public AtomicOperation<T> atomicAll(Filter... filters) {
                return new TAtomicOperation(ds, query(filters), true);
            }
        }

        MongoCollectionCache cache = MorplayPlugin.collectionsCache();        
        MongoStorage storage = MorplayPlugin.getStorage();

        Datastore ds = dbName == null ? storage.ds() : storage.ds(dbName);
        if (cache.contains(entityClass, ds)) {
            return MorplayPlugin.collectionsCache().get(entityClass, ds, TMongoCollection.class);
        }

        return cache.add(entityClass, ds, new TMongoCollection(entityClass, ds));
    }
}
