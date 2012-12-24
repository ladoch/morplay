package test.utils;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.query.Query;
import com.github.jmkgreen.morphia.query.UpdateOperations;
import com.mongodb.DB;
import net.onlite.morplay.mongo.MongoQuery;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Incapsulate mocked db classes
 */
public class DbMock<T> {
    private final UpdateOperations<T> updateOperations;
    private final MongoQuery<T> query;
    private final Datastore ds;

    @SuppressWarnings("unchecked")
    public DbMock(Class<T> entityClass) {
        updateOperations = (UpdateOperations<T>)mock(UpdateOperations.class);
        query = (MongoQuery<T>)mock(MongoQuery.class);
        ds = mock(Datastore.class);

        DB db = mock(DB.class);

        when(db.getName()).thenReturn("test");
        when(query.getEntityClass()).thenReturn(entityClass);
        when(query.getImpl()).thenReturn((Query<T>)mock(Query.class));
        when(ds.createUpdateOperations(entityClass)).thenReturn(updateOperations);
        when(ds.getDB()).thenReturn(db);
    }

    public UpdateOperations<T> getUpdateOperations() {
        return updateOperations;
    }

    public MongoQuery<T> getQuery() {
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
