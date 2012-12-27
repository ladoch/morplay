package test;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.query.Query;
import net.onlite.morplay.mongo.Filter;
import org.junit.Test;
import test.utils.TestCollection;

import static org.mockito.Mockito.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

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
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Query<String> query = (Query<String>)mock(Query.class);
                Datastore ds = mock(Datastore.class);
                when(ds.find(String.class)).thenReturn(query);

                TestCollection collection = new TestCollection(String.class, ds);
                collection.findById(TEST_ID).get();

                verify(ds, times(1)).getByKey(eq(String.class), eq(new Key<>(String.class, TEST_ID)));
            }
        });
    }

    @Test
    public void createTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Datastore ds = mock(Datastore.class);
                TestCollection collection = new TestCollection(String.class, ds);

                collection.create(TEST_ENTITY_STRING).get();
                verify(ds, times(1)).save(TEST_ENTITY_STRING);
            }
        });
    }

    @Test
    public void removeTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Query<String> query = (Query<String>)mock(Query.class);
                Datastore ds = mock(Datastore.class);
                when(ds.find(String.class)).thenReturn(query);

                TestCollection collection = new TestCollection(String.class, ds);

                collection.remove(TEST_FILTERS).get();

                verify(ds, times(1)).find(String.class);
                for (Filter filter : TEST_FILTERS) {
                    verify(query, times(1)).filter(filter.getCriteria(), filter.getValue());
                }
                verify(ds, times(1)).delete(query);
            }
        });
    }


    @Test
    public void removeByIdTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Query<String> query = (Query<String>)mock(Query.class);
                Datastore ds = mock(Datastore.class);
                when(ds.find(String.class)).thenReturn(query);
                when(query.filter("_id", TEST_ID)).thenReturn(query);

                TestCollection collection = new TestCollection(String.class, ds);
                collection.removeById(TEST_ID).get();

                verify(ds, times(1)).find(String.class);
                verify(query, times(1)).filter("_id", TEST_ID);
                verify(ds, times(1)).delete(query);
            }
        });
    }

}
