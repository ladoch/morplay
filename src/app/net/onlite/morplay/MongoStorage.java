package net.onlite.morplay;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

import java.util.HashMap;
import java.util.Map;

/**
 * Mongo storage abstraction
 */
public class MongoStorage {
    /**
     * Morphia instance
     */
    private final Morphia morphia = new Morphia();

    /**
     * Mongo instance
     */
    private final Mongo mongo;

    /**
     * Data stores
     */
    private final Map<String, Datastore> dbs = new HashMap<String, Datastore>();

    /**
     * Default db
     */
    private Datastore defaultDb;

    /**
     * Constructor.
     * Initialize mongo connection.
     * @param config Mongo configuration
     */
    public MongoStorage(MongoConfig config) {
        // Initialize connection to server
        mongo = new Mongo(config.getServerAddresses());
        mongo.setWriteConcern(config.getDefaultWriteConcern());

        // Open databases
        for (MongoConfig.DbConfig dbConfig : config.getDatabases()) {
            Datastore datastore;
            if (dbConfig.isSecure()) {
                datastore = morphia.createDatastore(mongo, dbConfig.getName(),
                        dbConfig.getLogin(), dbConfig.getPassword());
            } else {
                datastore = morphia.createDatastore(mongo, dbConfig.getName());
            }

            if (defaultDb == null) {
                defaultDb = datastore;
            }

            dbs.put(dbConfig.getName(), datastore);
        }
    }

    /**
     * @return Mongo driver instance
     */
    public Mongo mongo() {
        return mongo;
    }

    /**
     * @return Morphia instance
     */
    public Morphia morphia() {
        return morphia;
    }

    /**
     * @return Default data store
     */
    public Datastore ds() {
        return defaultDb;
    }

    /**
     * Get alternative data store by DB name
     * @param dbName DB name
     * @return Datastore
     */
    public Datastore ds(String dbName) {
        return dbs.get(dbName);
    }

    /**
     * Close all connections
     */
    public void close() {
        mongo.close();
    }
}
