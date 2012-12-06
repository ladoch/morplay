
package net.onlite.morplay;

/**
 * Query filter
 */
public class Filter {
	private final String criteria;
	private final Object value;

	/**
	 * Create filter
	 * @param criteria  The criteria is a composite of the field name and the operator ("field >", or "field in").
	 *                  Operators: =,!=, <>,>,<,>=,<=,in,nin,elem,exists,all,size
	 * @param value Criteria value
	 */
	public Filter(String criteria, Object value) {
		this.criteria = criteria;
		this.value = value;
	}

	/**
	 * Get criteria
	 * @return Criteria
	 */
	public String getCriteria() {
		return criteria;
	}

	/**
	 * Get criteria value
	 * @return Value
	 */
	public Object getValue() {
		return value;
	}
}
