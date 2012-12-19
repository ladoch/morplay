package test.utils;

import com.google.code.morphia.Datastore;
import net.onlite.morplay.AtomicOperation;
import net.onlite.morplay.Filter;
import net.onlite.morplay.MongoCollection;

/**
 * Test collection
 */
public class TestCollection extends MongoCollection<String> {

    public TestCollection(Class<String> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public AtomicOperation<String> atomic(Filter... filters) {
        return null;
    }

    @Override
    public AtomicOperation<String> atomicAll(Filter... filters) {
        return null;
    }
}
