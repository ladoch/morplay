package test;

import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import net.onlite.morplay.mongo.MongoConfig;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

/**
 * Mongo configuration tests
 */
public class MongoConfigTests {
    /**
     * Tests constants
     */

    private static final String[] TEST_SERVER_HOSTS       = {
        "192.168.1.1",
        "192.168.1.2"
    };
    private static final int[] TEST_SERVER_PORTS          = {
        27017,
        27018
    };
    private static final String[] TEST_DB_LOGINS          = {
        "login"
    };
    private static final String[] TEST_DB_PASSWORDS       = {
        "password"
    };
    private static final String[] TEST_DB_NAMES           = {
        "db1",
        "db2"
    };
    private static final String TEST_SEEDS                = String.format(
        "%s:%d;%s:%d",
        TEST_SERVER_HOSTS[0],
        TEST_SERVER_PORTS[0],
        TEST_SERVER_HOSTS[1],
        TEST_SERVER_PORTS[1]
    );
    private static final String TEST_WRITE_CONCERN        = "journaled";
    private static final String TEST_DATABASES            = String.format(
        "%s:%s@%s;%s",
        TEST_DB_LOGINS[0],
        TEST_DB_PASSWORDS[0],
        TEST_DB_NAMES[0],
        TEST_DB_NAMES[1]
    );

    @Test
    public void serverAddressesTest() {
        Map<String, String> morPlayConfig = new HashMap<>();
        morPlayConfig.put("seeds", TEST_SEEDS);
        Map<String, Object> config = new HashMap<>();
        config.put("morplay", morPlayConfig);

        running(fakeApplication(config), new Runnable() {
            @Override
            public void run() {
                MongoConfig mongoConfig = new MongoConfig();
                List<ServerAddress> serverAddresses = mongoConfig.getServerAddresses();

                for (int i = 0; i < serverAddresses.size(); i++) {
                    ServerAddress serverAddress = serverAddresses.get(i);

                    assertThat(serverAddress.getHost()).isEqualTo(TEST_SERVER_HOSTS[i]);
                    assertThat(serverAddress.getPort()).isEqualTo(TEST_SERVER_PORTS[i]);
                }
            }
        });
    }

    @Test
    public void writeConcernTest() {
        Map<String, String> morPlayConfig = new HashMap<>();
        morPlayConfig.put("write_concern", TEST_WRITE_CONCERN);
        Map<String, Object> config = new HashMap<>();
        config.put("morplay", morPlayConfig);

        running(fakeApplication(config), new Runnable() {
            @Override
            public void run() {
                MongoConfig mongoConfig = new MongoConfig();
                WriteConcern writeConcern = mongoConfig.getDefaultWriteConcern();

                assertThat(writeConcern).isNotNull();
                assertThat(writeConcern.toString()).isNotNull();
                assertThat(writeConcern.toString()).isEqualTo(WriteConcern.JOURNALED.toString());
            }
        });
    }

    @Test
    public void databasesTest() {
        Map<String, String> morPlayConfig = new HashMap<>();
        morPlayConfig.put("databases", TEST_DATABASES);
        Map<String, Object> config = new HashMap<>();
        config.put("morplay", morPlayConfig);

        running(fakeApplication(config), new Runnable() {
            @Override
            public void run() {
                MongoConfig mongoConfig = new MongoConfig();
                List<MongoConfig.DbConfig> databases = mongoConfig.getDatabases();

                for (int i = 0; i < databases.size(); i++) {
                    MongoConfig.DbConfig dbConfig = databases.get(i);

                    assertThat(dbConfig.getName()).isEqualTo(TEST_DB_NAMES[i]);
                    if (dbConfig.isSecure()) {
                        assertThat(dbConfig.getLogin()).isEqualTo(TEST_DB_LOGINS[i]);
                        assertThat(dbConfig.getPassword()).isEqualTo(TEST_DB_PASSWORDS[i].toCharArray());
                    }
                }
            }
        });
    }
}
