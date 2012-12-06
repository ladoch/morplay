package net.onlite.morplay;

import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import play.Configuration;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

/**
 * Mongo configuration wrapper
 */
public class MongoConfig {
    /**
     * Configuration parameters prefix
     */
    private static final String CONFIG_PREFIX = "morplay";

    /**
     * Seeds config parameter name
     */
    private static final String CONFIG_SEEDS = "seeds";

    /**
     * Host config parameter name
     */
    private static final String CONFIG_HOST = "host";

    /**
     * Port config parameter name
     */
    private static final String CONFIG_PORT = "port";

    /**
     * Write concern config parameter name
     */
    private static final String CONFIG_WRITECONCERN = "write_concern";

    /**
     * Play configuration for plugin
     */
    private final Configuration pluginConfig = Configuration.root().getConfig(CONFIG_PREFIX);



    /**
     * Is multiple db addresses used
     * @return True or false.
     */
    public boolean isMultiple() {
        // Get seeds string from configuration
        String seedsString = pluginConfig.getString(CONFIG_SEEDS, "");

        // If seeds configured then multiple addresses used
        return !seedsString.isEmpty();
    }

    /**
     * Get server addresses
     * @return List of mongo server addresses
     */
    public List<ServerAddress> getServerAddresses() {
        // Get seeds string from configuration
        String seedsString = pluginConfig.getString(CONFIG_SEEDS, "");

        // Split string into urls
        String[] seeds = seedsString.split("[;,\\s]+");

        List<ServerAddress> addresses = new LinkedList<ServerAddress>();
        for (String seed : seeds) {
            // Split seed string into url parts <host>[:<port>]
            String[] seedParts = seed.split(":");
            if (seedParts.length == 0) {
                continue;
            }

            // Get host and port from url
            String host = seedParts[0];
            int port = seedParts.length > 1 ? Integer.parseInt(seedParts[1]) : ServerAddress.defaultPort();

            try {
                addresses.add(new ServerAddress(host, port));
            } catch (UnknownHostException e) {
                throw pluginConfig.reportError(CONFIG_SEEDS, "Invalid host", e);
            }
        }

        return addresses;
    }

    /**
     * Get server address
     * @return Mongo server address
     */
    public ServerAddress getServerAddress() {
        String host = pluginConfig.getString(CONFIG_HOST, ServerAddress.defaultHost());
        int port = pluginConfig.getInt(CONFIG_PORT, ServerAddress.defaultPort());

        try {
            return new ServerAddress(host, port);
        } catch (UnknownHostException e) {
            throw pluginConfig.reportError(CONFIG_HOST, "Invalid host", e);
        }
    }

    /**
     * Get default write concern for mongo operations
     * @return Write concern
     */
    public WriteConcern getDefaultWriteConcern() {
        String writeConcernString = pluginConfig.getString(CONFIG_WRITECONCERN, WriteConcern.NONE.toString());
        return WriteConcern.valueOf(writeConcernString);
    }

    public String getDbName() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public String getDbLogin() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }


    public String getDbPassword() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
