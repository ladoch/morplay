package test;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import net.onlite.morplay.Filter;
import org.junit.Test;
import test.utils.TestCollection;

import static org.mockito.Mockito.*;

/**
 * Tests for MongoCollection class
 */
@SuppressWarnings("unchecked")
public class MongoCollectionTests {
    /**
     * Tests constants
     */

    public static final Filter[] TEST_FILTERS = new Filter[]{
            new Filter("testCriteria", "testValue"),
            new Filter("testCriteria1", "testValue1"),
    };
    private static final String TEST_ENTITY_STRING = "testEntityString";

    @Test
    public void queryTest() {
        Query<String> query = (Query<String>)mock(Query.class);
        Datastore ds = mock(Datastore.class);
        when(ds.find(String.class)).thenReturn(query);

        TestCollection collection = new TestCollection(String.class, ds);

        collection.query(TEST_FILTERS);

        verify(ds, times(1)).find(String.class);
        for (Filter filter : TEST_FILTERS) {
            verify(query, times(1)).filter(filter.getCriteria(), filter.getValue());
        }
    }

    @Test
    public void createTest() {
        Datastore ds = mock(Datastore.class);
        TestCollection collection = new TestCollection(String.class, ds);

        collection.create(TEST_ENTITY_STRING);
        verify(ds, times(1)).save(TEST_ENTITY_STRING);
    }

    @Test
    public void removeTest() {
        Query<String> query = (Query<String>)mock(Query.class);
        Datastore ds = mock(Datastore.class);
        when(ds.find(String.class)).thenReturn(query);

        TestCollection collection = new TestCollection(String.class, ds);

        collection.remove(TEST_FILTERS);

        verify(ds, times(1)).find(String.class);
        for (Filter filter : TEST_FILTERS) {
            verify(query, times(1)).filter(filter.getCriteria(), filter.getValue());
        }
        verify(ds, times(1)).delete(query);
    }

}
