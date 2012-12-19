
package net.onlite.morplay.mongo;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;

import java.util.Arrays;
import java.util.List;

/**
 * Responsible for store atomic update operation command
 */
public class AtomicOperation<T> {
	/**
	 * Data store
	 */
	private final Datastore ds;

	/**
	 * Query
	 */
	private final Query<T> query;

	/**
	 * Update multiple
	 */
	private final boolean multiple;

	/**
	 * Update operations
	 */
	private UpdateOperations<T> updateOperations;

	/**
	 * Constructor
	 * @param ds Morphia MongoDB data store instance
	 * @param query Morphia query
	 * @param multiple Is multiple items should be updated
	 */
	public AtomicOperation(Datastore ds, Query<T> query, boolean multiple) {
		this.ds = ds;
		this.query = query;
		this.multiple = multiple;
		this.updateOperations = ds.createUpdateOperations(query.getEntityClass());
	}

	/**
	 * Append "set field" operation to atomic update
	 *
	 * @param fieldName Field name
	 * @param value	 Field value, should be same type as corresponding field in Account class
	 *
	 * @return Changed update instance
	 */
	public AtomicOperation<T> set(String fieldName, Object value) {
		updateOperations = updateOperations.set(fieldName, value);
		return this;
	}

	/**
	 * Append "unset field" operation to atomic update
	 *
	 * @param fieldName Field name
	 * @return Changed update instance
	 */
	public AtomicOperation<T> unset(String fieldName) {
		updateOperations = updateOperations.unset(fieldName);
		return this;
	}

	/**
	 * Append "increment field" operation to atomic update
	 *
	 * @param fieldName Field name
	 * @param value	 Increment value
	 *
	 * @return Changed update instance
	 */
	public AtomicOperation<T> inc(String fieldName, Number value) {
		updateOperations = updateOperations.inc(fieldName, value);
		return this;
	}

	/**
	 * Append "add to sub-array" operation to atomic update
	 *
     * @param fieldName Sub array field name
     * @param values	Values to add.
     *
     * @return Changed update instance
	 */
	public AtomicOperation<T> add(String fieldName, Object... values) {
        return add(fieldName, Arrays.asList(values));

	}

    /**
     * Append "add to sub-array" operation to atomic update
     *
     * @param fieldName Sub array field name
     * @param values	Values to add.
     *
     * @return Changed update instance
     */
    public AtomicOperation<T> add(String fieldName, List<?> values) {
        // Some optimization
        if (values.size() == 1) {
            updateOperations = updateOperations.add(fieldName, values.get(0), true);
        } else {
            updateOperations = updateOperations.addAll(fieldName, values, true);
        }

        return this;
    }

    /**
	 * Append "remove from sub-array" operation to atomic update
	 *
	 * @param fieldName Sub array field name
	 * @param value Value to remove
	 * @return Changed update instance
	 */
	public AtomicOperation<T> remove(String fieldName, Object value) {
		updateOperations = updateOperations.removeAll(fieldName, value);
		return this;
	}

    /**
	 * Append "pop from sub-array" operation to atomic update
	 *
	 * @param fieldName Sub array field name
	 * @return Changed update instance
	 */
	public AtomicOperation<T> pop(String fieldName) {
		updateOperations = updateOperations.removeFirst(fieldName);
		return this;
	}

	/**
	 * Execute atomic update
	 * @param upsert Create if not exist
	 */
	public void update(boolean upsert) {
		if (multiple) {
			ds.update(query, updateOperations, upsert);
		} else {
			ds.updateFirst(query, updateOperations, upsert);
		}
	}

	/**
	 * Execute atomic "find and modify" query
	 *
	 * @param isNew If true, then returned entity will include updates.
	 * @return Entity.
	 */
	public T findAndModify(boolean isNew) {
		if (multiple) {
			throw new RuntimeException("Not implemented");
		}

		return ds.findAndModify(query, updateOperations, !isNew);
	}

    public Query<T> query() {
        return query;
    }
}
