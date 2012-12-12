package test;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import net.onlite.morplay.AtomicOperation;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Atomic operation tests
 */
public class AtomicOperationTests {
    @Test
    @SuppressWarnings("unchecked")
    public void setTest() {
        UpdateOperations<String> updateOperations = (UpdateOperations<String>)mock(UpdateOperations.class);
        Query<String> query = (Query<String>)mock(Query.class);
        Datastore ds = mock(Datastore.class);

        when(query.getEntityClass()).thenReturn(String.class);
        when(ds.createUpdateOperations(String.class)).thenReturn(updateOperations);

        AtomicOperation<String> testOp = new AtomicOperation<String>(ds, query, false);
        testOp.set("testField", "testEntity");        
                
        verify(ds, only()).createUpdateOperations(String.class);
        verify(updateOperations, only()).set("testField", "testEntity");
    }

    @Test
    public void unsetTest() {

    }

    @Test
    public void incTest() {

    }

    @Test
    public void addTest() {

    }

    @Test
    public void removeTest() {

    }

    @Test
    public void popTest() {

    }

    @Test
    public void updateTest() {

    }

}
