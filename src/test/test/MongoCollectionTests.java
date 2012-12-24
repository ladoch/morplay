package test;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.query.Query;
import net.onlite.morplay.mongo.Filter;
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
    public static final String TEST_ID = "testId";

    @Test
    public void findTest() {
        Query<String> query = (Query<String>)mock(Query.class);
        Datastore ds = mock(Datastore.class);
        when(ds.find(String.class)).thenReturn(query);

        TestCollection collection = new TestCollection(String.class, ds);

        collection.find(TEST_FILTERS);

        verify(ds, times(1)).find(String.class);
        for (Filter filter : TEST_FILTERS) {
            verify(query, times(1)).filter(filter.getCriteria(), filter.getValue());
        }
    }

    @Test
    public void findByIdTest() {
        Query<String> query = (Query<String>)mock(Query.class);
        Datastore ds = mock(Datastore.class);
        when(ds.find(String.class)).thenReturn(query);

        TestCollection collection = new TestCollection(String.class, ds);
        collection.findById(TEST_ID);

        verify(ds, times(1)).getByKey(eq(String.class), eq(new Key<>(String.class, TEST_ID)));
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


    @Test
    public void removeByIdTest() {
        Query<String> query = (Query<String>)mock(Query.class);
        Datastore ds = mock(Datastore.class);
        when(ds.find(String.class)).thenReturn(query);
        when(query.filter("_id", TEST_ID)).thenReturn(query);

        TestCollection collection = new TestCollection(String.class, ds);
        collection.removeById(TEST_ID);

        verify(ds, times(1)).find(String.class);
        verify(query, times(1)).filter("_id", TEST_ID);
        verify(ds, times(1)).delete(query);
    }

}
