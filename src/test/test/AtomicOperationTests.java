package test;

import net.onlite.morplay.mongo.AtomicOperation;
import org.junit.Test;
import test.utils.DbMock;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Atomic operation tests
 */
public class AtomicOperationTests {

    /**
     * Test constants
     */

    public static final String TEST_FIELD_NAME          = "testFieldName";
    public static final String TEST_FIELD_STRING_VALUE  = "testFieldStringValue";
    public static final int TEST_FIELD_INT_VALUE        = 3;
    public static final boolean TEST_UPSERT_VALUE       = true;
    public static final List<String> TEST_ARRAY_VALUE   = Arrays.asList(
            TEST_FIELD_STRING_VALUE,
            TEST_FIELD_STRING_VALUE
    );

    @Test
    public void constructorTest() {
        DbMock<String> dbMock = new DbMock<>(String.class);

        // Create operation
        AtomicOperation<String> testOp = createAtomicOperation(dbMock);

        assertThat(testOp).isNotNull();
        assertThat((Object)testOp.query()).isSameAs(testOp.query());
        verify(dbMock.getDs(), times(1)).createUpdateOperations(String.class);
    }

    @Test
    public void setTest() {
        DbMock<String> dbMock = new DbMock<>(String.class);
        AtomicOperation<String> testOp = createAtomicOperation(dbMock);

        testOp.set(TEST_FIELD_NAME, TEST_FIELD_STRING_VALUE);
        verify(dbMock.getUpdateOperations(), times(1)).set(TEST_FIELD_NAME, TEST_FIELD_STRING_VALUE);
    }

    @Test
    public void unsetTest() {
        DbMock<String> dbMock = new DbMock<>(String.class);
        AtomicOperation<String> testOp = createAtomicOperation(dbMock);

        testOp.unset(TEST_FIELD_NAME);
        verify(dbMock.getUpdateOperations(), times(1)).unset(TEST_FIELD_NAME);

    }

    @Test
    public void incTest() {
        DbMock<String> dbMock = new DbMock<>(String.class);
        AtomicOperation<String> testOp = createAtomicOperation(dbMock);

        testOp.inc(TEST_FIELD_NAME, TEST_FIELD_INT_VALUE);
        verify(dbMock.getUpdateOperations(), times(1)).inc(TEST_FIELD_NAME, TEST_FIELD_INT_VALUE);
    }

    @Test
    public void addTest() {
        DbMock<String> dbMock = new DbMock<>(String.class);
        AtomicOperation<String> testOp = createAtomicOperation(dbMock);

        testOp.add(TEST_FIELD_NAME, TEST_FIELD_STRING_VALUE);
        verify(dbMock.getUpdateOperations(), times(1)).add(TEST_FIELD_NAME, TEST_FIELD_STRING_VALUE, true);

        testOp = createAtomicOperation(dbMock);
        testOp.add(TEST_FIELD_NAME, TEST_ARRAY_VALUE);
        verify(dbMock.getUpdateOperations(), times(1)).addAll(TEST_FIELD_NAME, TEST_ARRAY_VALUE, true);
    }

    @Test
    public void removeTest() {
        DbMock<String> dbMock = new DbMock<>(String.class);
        AtomicOperation<String> testOp = createAtomicOperation(dbMock);

        testOp.remove(TEST_FIELD_NAME, TEST_FIELD_STRING_VALUE);
        verify(dbMock.getUpdateOperations(), times(1)).removeAll(TEST_FIELD_NAME, TEST_FIELD_STRING_VALUE);
    }

    @Test
    public void popTest() {
        DbMock<String> dbMock = new DbMock<>(String.class);
        AtomicOperation<String> testOp = createAtomicOperation(dbMock);

        testOp.pop(TEST_FIELD_NAME);
        verify(dbMock.getUpdateOperations(), times(1)).removeFirst(TEST_FIELD_NAME);
    }

    @Test
    public void updateTest() {
        DbMock<String> dbMock = new DbMock<>(String.class);
        AtomicOperation<String> testOp = createAtomicOperation(dbMock);

        testOp.update(TEST_UPSERT_VALUE);
        verify(dbMock.getDs(), times(1)).updateFirst(dbMock.getQuery(),
                dbMock.getUpdateOperations(), TEST_UPSERT_VALUE);
    }

    @Test
    public void findAndModifyTest() {
        DbMock<String> dbMock = new DbMock<>(String.class);
        AtomicOperation<String> testOp = createAtomicOperation(dbMock);

        testOp.findAndModify(true);
        verify(dbMock.getDs(), times(1)).findAndModify(dbMock.getQuery(),
                dbMock.getUpdateOperations(), false);
    }

    private <T> AtomicOperation<T> createAtomicOperation(DbMock<T> dbMock) {
        return new AtomicOperation<>(dbMock.getDs(), dbMock.getQuery(), false);
    }
}
