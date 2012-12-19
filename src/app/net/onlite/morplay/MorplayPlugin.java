package net.onlite.morplay;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.common.collect.Iterables;
import play.Play;
import play.Plugin;
import play.libs.Classpath;

/**
 * Responsible for integration with Playframework
 */
public class MorplayPlugin extends Plugin {
    /**
     * Mongo connection
     */
    private static MongoConnection connection;

    /**
     * Get mongo connection instance
     * @return Mongo storage instance
     */
    public static MongoConnection connection() {
        return connection;
    }

    /**
     * Get default data store
     * @return Default data store instance
     */
    public static MongoStore store() {
        return connection.store();
    }

    /**
     * Get data store by name
     * @return Data store instance
     */
    public static MongoStore store(String name) {
        return connection.store(name);
    }


    @Override
    public void onStart() {
        // Read configuration
        MongoConfig config = new MongoConfig();

        // Initialize mongo connection
        connection = new MongoConnection(config);

        // Map classes
        Iterable<String> entities = Classpath.getTypesAnnotatedWith(Play.application(), "models", Entity.class);
        Iterable<String> embedded = Classpath.getTypesAnnotatedWith(Play.application(), "models", Embedded.class);

        for(String className : Iterables.concat(entities, embedded)) {
            try {
                Morphia morphia = connection.morphia();
                morphia.map(Class.forName(className, true, Play.application().classloader()));
            } catch (ClassNotFoundException e) {
                // TODO: log
            }
        }
    }

    @Override
    public void onStop() {
        if (connection != null) {
            connection.close();
        }

        connection = null;
    }
}
