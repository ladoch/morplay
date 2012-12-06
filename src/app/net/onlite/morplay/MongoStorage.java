package net.onlite.morplay;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Mongo storage abstraction
 */
public class MongoStorage {
    /**
     * Default database key
     */
    public static final String DEFAULT_DB = "_defaultdb";

    /**
     * Morphia instance
     */
    private final Morphia morphia = new Morphia();

    /**
     * Mongo instance
     */
    private Mongo mongo;

    /**
     * Data stores
     */
    private final Map<String, Datastore> dbs = new HashMap<String, Datastore>();

    /**
     * Constructor.
     * Initialize mongo connection.
     * @param config Mongo configuration
     */
    public MongoStorage(MongoConfig config) {
        // Initialize connection to server
        if (config.isMultiple()) {
            mongo = new Mongo(config.getServerAddresses());
        } else {
            mongo = new Mongo(config.getServerAddress());
        }

        mongo.setWriteConcern(config.getDefaultWriteConcern());

        // Open default db
        Datastore datastore = ds(config.getDbName(), config.getDbLogin(), config.getDbPassword());
        dbs.put(DEFAULT_DB, datastore);
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
        return ds(DEFAULT_DB);
    }

    /**
     * Get alternative data store by DB name
     * @param dbName DB name
     * @return Datastore
     */
    public Datastore ds(String dbName) {
        return ds(dbName, null, null);
    }

    /**
     * Get alternative data store by DB name
     * @param dbName DB name
     * @param dbLogin Login
     * @param dbPassword Password
     * @return Datastore
     */
    public Datastore ds(String dbName, String dbLogin, String dbPassword) {
        if (dbs.containsKey(dbName)) {
            return dbs.get(dbName);
        }

        Datastore ds;
        if (StringUtils.isEmpty(dbLogin) || StringUtils.isEmpty(dbPassword)) {
            ds = morphia.createDatastore(mongo, dbName);
        } else {
            ds = morphia.createDatastore(mongo, dbName, dbLogin, dbPassword.toCharArray());
        }

        return dbs.put(dbName, ds);
    }

    /**
     * Close all connections
     */
    public void close() {
        mongo.close();
    }
}
