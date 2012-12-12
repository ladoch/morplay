package test;

import net.onlite.morplay.Filter;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Filter class test
 */
public class FilterTests {
    @Test
    public void base() {
        Filter filter = new Filter("test criteria", "test value");
        assertThat(filter.getCriteria()).isEqualTo("test criteria");
        assertThat(filter.getValue()).isEqualTo("test value");
    }
}
