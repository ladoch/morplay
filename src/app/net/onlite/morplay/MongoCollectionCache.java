package net.onlite.morplay;

import com.google.code.morphia.Datastore;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for cache collection objects
 */
public class MongoCollectionCache {
    /**
     * Cache
     */
    private final Map<String, MongoCollection> cache = new HashMap<>();

    /**
     * Check whether cache contains requested collection or not
     * @param entityClass Entity class
     * @param ds Datastore instance
     * @param <T> Entity type
     * @return True or false.
     */
    public <T> boolean contains(Class<T> entityClass, Datastore ds) {
        return cache.containsKey(key(entityClass, ds.getDB().getName()));
    }

    /**
     * Add collection to cache
     * @param entityClass Entity class
     * @param ds Datastore instance
     * @param collection Collection wrapper instance
     * @param <T> Entity type.
     * @return Cached collection
     */
    public <T> MongoCollection<T> add(Class<T> entityClass, Datastore ds, MongoCollection<T> collection) {
        cache.put(key(entityClass, ds.getDB().getName()), collection);
        return collection;
    }

    /**
     * Get collection from cache
     * @param entityClass Entity class
     * @param ds Datastore instance.
     * @param resultClass Collection class
     * @param <T> Entity type
     * @return Collection instance or null if not cached.
     */
    public <T> MongoCollection<T> get(Class<T> entityClass,
                                      Datastore ds,
                                      Class<? extends MongoCollection<T>> resultClass) {

        if (!contains(entityClass, ds)) {
            return null;
        }

        return resultClass.cast(cache.get(key(entityClass, ds.getDB().getName())));
    }

    /**
     * Clear cache
     */
    public void clear() {
        cache.clear();
    }
    
    private static <T> String key(Class<T> entityClass, String dbName) {
        return entityClass.getSimpleName() + dbName;
    }
}
