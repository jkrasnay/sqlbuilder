package ca.krasnay.sqlbuilder;

import junit.framework.TestCase;

public class UpdateBuilderTest extends TestCase {

    public void testAll() {

        UpdateBuilder ub;

        ub = new UpdateBuilder("Employee");
        assertEquals("update Employee", ub.toString());

        ub.set("name = 'Bobo'");
        assertEquals("update Employee set name = 'Bobo'", ub.toString());

        ub.set("age = 37");
        assertEquals("update Employee set name = 'Bobo', age = 37", ub.toString());

        ub.where("name = 'Arnold'");
        assertEquals("update Employee set name = 'Bobo', age = 37 where name = 'Arnold'", ub.toString());

        ub.where("age = 17");
        assertEquals("update Employee set name = 'Bobo', age = 37 where name = 'Arnold' and age = 17", ub.toString());

    }
}
