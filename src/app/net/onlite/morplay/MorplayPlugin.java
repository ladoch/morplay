package net.onlite.morplay;

import play.Plugin;

/**
 * morplay plugin
 */
public class MorplayPlugin extends Plugin {
    /**
     * Storage instance
     */
    private static MongoStorage storage;

    /**
     * Collections cache
     */
    private static final MongoCollectionCache collectionsCache = new MongoCollectionCache();

    /**
     * Get storage instance
     * @return Mongo storage instance
     */
    public static MongoStorage getStorage() {
        return storage;
    }

    /**
     * @return Cache of collection wrappers
     */
    public static MongoCollectionCache collectionsCache() {
        return collectionsCache;
    }

    @Override
    public void onStart() {
        // Read configuration
        MongoConfig config = new MongoConfig();

        // Initialize storage
        storage = new MongoStorage(config);
    }

    @Override
    public void onStop() {
        if (storage != null) {
            storage.close();
        }

        collectionsCache.clear();
        storage = null;
    }
}
