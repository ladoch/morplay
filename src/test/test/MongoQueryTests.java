package test;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.query.Query;
import com.google.common.collect.Iterables;
import net.onlite.morplay.mongo.MongoQuery;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

/**
 *
 */
@SuppressWarnings("unchecked")
public class MongoQueryTests {
    private static final int TEST_BATCH_SIZE = 10;
    private static final String TEST_CONDITION = "testCond";
    private static final String TEST_STRING_VALUE = "testVal";
    private static final String TEST_INDEX = "test";
    private static final int TEST_LIMIT = 10;
    private static final int TEST_OFFSET = 5;
    private static final String TEST_FIELD = "testField";
    private static final String TEST_ID = "id";
    public static final Key<String> TEST_KEY = new Key<>(String.class, TEST_ID);
    private static final List<String> TEST_LIST = Arrays.asList(TEST_STRING_VALUE, TEST_STRING_VALUE);
    private static final List<Key<String>> TEST_KEY_LIST = Arrays.asList(TEST_KEY, TEST_KEY);
    private static final Long TEST_COUNT = 10L;

    @Test
    public void delegationTest() {
        Query<String> impl = mock(Query.class);
        MongoQuery<String> query = new MongoQuery<String>(impl);

        assertThat(query.batchSize(TEST_BATCH_SIZE)).isSameAs(query);
        assertThat(query.disableCursorTimeout()).isSameAs(query);
        assertThat(query.disableValidation()).isSameAs(query);
        assertThat(query.disableSnapshotMode()).isSameAs(query);
        assertThat(query.enableCursorTimeout()).isSameAs(query);
        assertThat(query.enableSnapshotMode()).isSameAs(query);
        assertThat(query.filter(TEST_CONDITION, TEST_STRING_VALUE)).isSameAs(query);
        assertThat(query.hintIndex(TEST_INDEX)).isSameAs(query);
        assertThat(query.limit(TEST_LIMIT)).isSameAs(query);
        assertThat(query.offset(TEST_OFFSET)).isSameAs(query);
        assertThat(query.order(TEST_FIELD)).isSameAs(query);
        assertThat(query.queryNonPrimary()).isSameAs(query);
        assertThat(query.queryPrimaryOnly()).isSameAs(query);
        assertThat(query.retrievedFields(true, TEST_FIELD, TEST_FIELD)).isSameAs(query);
        assertThat(query.retrieveKnownFields()).isSameAs(query);
        assertThat(query.useReadPreference(null)).isSameAs(query);
        assertThat(query.where("")).isSameAs(query);

        verify(impl, times(1)).batchSize(TEST_BATCH_SIZE);
        verify(impl, times(1)).disableCursorTimeout();
        verify(impl, times(1)).disableValidation();
        verify(impl, times(1)).disableSnapshotMode();
        verify(impl, times(1)).enableCursorTimeout();
        verify(impl, times(1)).enableSnapshotMode();
        verify(impl, times(1)).filter(TEST_CONDITION, TEST_STRING_VALUE);
        verify(impl, times(1)).hintIndex(TEST_INDEX);
        verify(impl, times(1)).limit(TEST_LIMIT);
        verify(impl, times(1)).offset(TEST_OFFSET);
        verify(impl, times(1)).order(TEST_FIELD);
        verify(impl, times(1)).queryNonPrimary();
        verify(impl, times(1)).queryPrimaryOnly();
        verify(impl, times(1)).retrievedFields(true, TEST_FIELD, TEST_FIELD);
        verify(impl, times(1)).retrieveKnownFields();
        verify(impl, times(1)).useReadPreference(null);
        verify(impl, times(1)).where("");
    }

    @Test
    public void getTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Query<String> impl = mock(Query.class);
                when(impl.get()).thenReturn(TEST_STRING_VALUE);

                MongoQuery<String> query = new MongoQuery<>(impl);

                assertThat(query.get().get()).isEqualTo(TEST_STRING_VALUE);
                verify(impl, times(1)).get();
            }
        });
    }

    @Test
    public void getKeyTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Query<String> impl = mock(Query.class);
                when(impl.getKey()).thenReturn(TEST_KEY);

                MongoQuery<String> query = new MongoQuery<>(impl);

                assertThat(query.getKey().get()).isSameAs(TEST_KEY);
                verify(impl, times(1)).getKey();
            }
        });
    }

    @Test
    public void asListTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Query<String> impl = mock(Query.class);
                when(impl.asList()).thenReturn(TEST_LIST);

                MongoQuery<String> query = new MongoQuery<>(impl);

                assertThat(query.asList().get()).isSameAs(TEST_LIST);
                verify(impl, times(1)).asList();
            }
        });
    }

    @Test
    public void asKeyListTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Query<String> impl = mock(Query.class);
                when(impl.asKeyList()).thenReturn(TEST_KEY_LIST);

                MongoQuery<String> query = new MongoQuery<>(impl);

                assertThat(query.asKeyList().get()).isSameAs(TEST_KEY_LIST);
                verify(impl, times(1)).asKeyList();
            }
        });
    }

    @Test
    public void countAllTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Query<String> impl = mock(Query.class);
                when(impl.countAll()).thenReturn(TEST_COUNT);

                MongoQuery<String> query = new MongoQuery<>(impl);

                assertThat(query.countAll().get()).isEqualTo(TEST_COUNT);
                verify(impl, times(1)).countAll();
            }
        });
    }

    @Test
    public void fetchTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Query<String> impl = mock(Query.class);
                when(impl.fetch()).thenReturn(TEST_LIST);

                MongoQuery<String> query = new MongoQuery<>(impl);

                assertThat(Iterables.elementsEqual(query.fetch(), TEST_LIST)).isTrue();
                verify(impl, times(1)).fetch();
            }
        });
    }

    @Test
    public void fetchEmptyEntitiesTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Query<String> impl = mock(Query.class);
                when(impl.fetchEmptyEntities()).thenReturn(TEST_LIST);

                MongoQuery<String> query = new MongoQuery<>(impl);

                assertThat(Iterables.elementsEqual(query.fetchEmptyEntities(), TEST_LIST)).isTrue();
                verify(impl, times(1)).fetchEmptyEntities();
            }
        });
    }

    @Test
    public void fetchKeysTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Query<String> impl = mock(Query.class);
                when(impl.fetchKeys()).thenReturn(TEST_KEY_LIST);

                MongoQuery<String> query = new MongoQuery<>(impl);

                assertThat(Iterables.elementsEqual(query.fetchKeys(), TEST_KEY_LIST)).isTrue();
                verify(impl, times(1)).fetchKeys();
            }
        });
    }

    @Test
    public void tailTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Query<String> impl = mock(Query.class);
                when(impl.tail()).thenReturn(TEST_LIST.iterator());

                MongoQuery<String> query = new MongoQuery<>(impl);

                assertThat(Iterables.elementsEqual(query.tail(), TEST_LIST)).isTrue();
                verify(impl, times(1)).tail();
            }
        });
    }

}
