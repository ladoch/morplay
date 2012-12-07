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
     * Database configuration
     */
    public static class DbConfig {
        /**
         * Name
         */
        private final String name;

        /**
         * Login
         */
        private final String login;

        /**
         * Password
         */
        private final char[] password;

        /**
         * @param name Database name
         * @param login Login
         * @param password Password
         */
        public DbConfig(String name, String login, char[] password) {
            this.name = name;
            this.login = login;
            this.password = password;
        }

        public boolean isSecure() {
            return name != null && password != null;
        }

        public String getName() {
            return name;
        }

        public String getLogin() {
            return login;
        }

        public char[] getPassword() {
            return password;
        }
    }

    /**
     * Configuration parameters prefix
     */
    private static final String CONFIG_PREFIX = "morplay";

    /**
     * Seeds config parameter name
     */
    private static final String CONFIG_SEEDS = "seeds";

    /**
     * Databases config parameter name
     */
    private static final String CONFIG_DATABASES = "databases";

    /**
     * Write concern config parameter name
     */
    private static final String CONFIG_WRITECONCERN = "write_concern";

    /**
     * Play configuration for plugin
     */
    private final Configuration pluginConfig = Configuration.root().getConfig(CONFIG_PREFIX);

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
     * Get default write concern for mongo operations
     * @return Write concern
     */
    public WriteConcern getDefaultWriteConcern() {
        String writeConcernString = pluginConfig.getString(CONFIG_WRITECONCERN, WriteConcern.NONE.toString());
        return WriteConcern.valueOf(writeConcernString);
    }

    /**
     * Get configured databases
     * @return Databases configurations
     */
    public List<DbConfig> getDatabases() {
        // Get databases string from configuration
        String dbsString = pluginConfig.getString(CONFIG_DATABASES, "");

        // Split string into databases strings
        String[] databases = dbsString.split("[;,\\s]+");
        if (databases.length == 0) {
            throw pluginConfig.reportError(CONFIG_DATABASES, "Empty databases list..", new RuntimeException());
        }

        List<DbConfig> result = new LinkedList<DbConfig>();
        for (String database : databases) {
            // Split database string into parts [<login>:<password>@]<name>
            String[] dbParts = database.split("@");

            DbConfig dbConf;
            if (dbParts.length == 2) { // DB with authorization
                // Extract auth info
                String[] auth = dbParts[0].split(":");
                if (auth.length != 2) {
                    throw pluginConfig.reportError(CONFIG_DATABASES, "Invalid db auth parameters.", new RuntimeException());
                }

                String name = dbParts[1];
                String login = auth[0];
                char[] password = auth[1].toCharArray();

                dbConf = new DbConfig(name, login, password);
            } else if (dbParts.length == 1) { // DB without authorization
                dbConf = new DbConfig(dbParts[0], null, null);
            } else {
                throw pluginConfig.reportError(CONFIG_DATABASES, "Invalid db configuration.", new RuntimeException());
            }

            result.add(dbConf);
        }

        return result;
    }
}
