package ca.krasnay.sqlbuilder.orm;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class StringListFlattenerTest extends TestCase {

    public void testAll() {

        StringListFlattener slf = new StringListFlattener();

        List<String> list = slf.split(null);
        assertNotNull(list);
        assertEquals(0, list.size());

        list = new ArrayList<String>();
        assertEquivalent(list, "");

        list.add(null);

        try {
            assertEquals("", new StringListFlattener().join(list));
            assertTrue(false);
        } catch (IllegalArgumentException e) {
        }

        list.clear();
        list.add("foo");
        assertEquivalent(list, "foo");

        list.add("bar");
        assertEquivalent(list, "foo,bar");

        list.add("b\\a,z");
        assertEquivalent(list, "foo,bar,b\\\\a\\,z");

        list.add("quux");
        assertEquivalent(list, "foo,bar,b\\\\a\\,z,quux");

        list.clear();
        list.add("");
        list.add("");
        list.add("");

        assertEquivalent(list, ",,");

    }

    public void testConvertEmptyToNull() {

        StringListFlattener slf = new StringListFlattener().setConvertEmptyToNull(true);

        List<String> list = slf.split(null);
        assertNotNull(list);
        assertEquals(0, list.size());

        list = new ArrayList<String>();
        assertEquivalent(list, "");

        // Weird case, we can't handle a list with a single null,
        // just as the normal flattener can't handle a list with
        // a single, empty string
        list.add(null);
        assertEquals("", slf.join(list));

        list.add(null);
        assertEquals(",", slf.join(list));

        List<String> list2 = slf.split(",");
        assertEquals(2, list2.size());
        assertNull(list2.get(0));
        assertNull(list2.get(1));

    }

    private void assertEquivalent(List<String> list, String flattened) {
        StringListFlattener slf = new StringListFlattener();
        assertEquals(flattened, slf.join(list));
        assertEquals(list, slf.split(flattened));
    }
}
