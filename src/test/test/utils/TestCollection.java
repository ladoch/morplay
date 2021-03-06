package test.utils;

import com.github.jmkgreen.morphia.Datastore;
import net.onlite.morplay.mongo.AtomicOperation;
import net.onlite.morplay.mongo.Filter;
import net.onlite.morplay.mongo.MongoCollection;

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
    public AtomicOperation<String> atomic(String entity) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AtomicOperation<String> atomicAll(Filter... filters) {
        return null;
    }
}
