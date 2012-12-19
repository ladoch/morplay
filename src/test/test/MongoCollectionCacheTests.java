package test;

import com.google.code.morphia.Datastore;
import net.onlite.morplay.MongoCollection;
import net.onlite.morplay.MongoCollectionCache;
import org.junit.Test;
import test.utils.DbMock;
import test.utils.TestCollection;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for MongoCollection cache
 */
public class MongoCollectionCacheTests {
    /**
     * Test objects
     */

    private final DbMock<String> dbMock = new DbMock<>(String.class);
    private final MongoCollection<String> testCollection = mock(TestCollection.class);

    @Test
    public void addGetTest() {
        MongoCollectionCache cache = new MongoCollectionCache();
        cache.add(String.class, dbMock.getDs(), testCollection);

        MongoCollection<String> obtained = cache.get(String.class, dbMock.getDs(), TestCollection.class);
        assertThat(obtained).isNotNull();
        assertThat(obtained).isSameAs(testCollection);

        MongoCollection<String> obtained1 = cache.get(String.class, dbMock.getDs("another"), TestCollection.class);
        assertThat(obtained1).isNull();
    }

    @Test
    public void addContainsTest() {
        MongoCollectionCache cache = new MongoCollectionCache();

        cache.add(String.class, dbMock.getDs(), testCollection);

        boolean contains = cache.contains(String.class, dbMock.getDs());
        assertThat(contains).isTrue();

        boolean contains1 = cache.contains(String.class, dbMock.getDs("another"));
        assertThat(contains1).isFalse();

    }

    @Test
    public void clearTest() {
        MongoCollectionCache cache = new MongoCollectionCache();

        cache.add(String.class, dbMock.getDs(), testCollection);
        cache.clear();

        // Test that collection is clear
        boolean contains = cache.contains(String.class, dbMock.getDs());
        assertThat(contains).isFalse();
    }

}
