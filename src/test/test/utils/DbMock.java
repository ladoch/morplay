package test.utils;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.mongodb.DB;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Incapsulate mocked db classes
 */
public class DbMock<T> {
    private final UpdateOperations<T> updateOperations;
    private final Query<T> query;
    private final Datastore ds;
    private final DB db;

    @SuppressWarnings("unchecked")
    public DbMock(Class<T> entityClass) {
        updateOperations = (UpdateOperations<T>)mock(UpdateOperations.class);
        query = (Query<T>)mock(Query.class);
        ds = mock(Datastore.class);
        db = mock(DB.class);

        when(db.getName()).thenReturn("test");
        when(query.getEntityClass()).thenReturn(entityClass);
        when(ds.createUpdateOperations(entityClass)).thenReturn(updateOperations);
        when(ds.getDB()).thenReturn(db);
    }

    public UpdateOperations<T> getUpdateOperations() {
        return updateOperations;
    }

    public Query<T> getQuery() {
        return query;
    }

    public Datastore getDs() {
        return ds;
    }

    public Datastore getDs(String name) {
        Datastore datastore = mock(Datastore.class);
        DB database = mock(DB.class);

        when(database.getName()).thenReturn(name);
        when(datastore.getDB()).thenReturn(database);

        return datastore;
    }

}
