package ca.krasnay.sqlbuilder;

import junit.framework.TestCase;

public class InsertBuilderTest extends TestCase {

    public void testAll() {

        InsertBuilder builder;

        builder = new InsertBuilder("Employee");
        assertEquals("insert into Employee () values ()", builder.toString());

        builder.set("id", "1");
        assertEquals("insert into Employee (id) values (1)", builder.toString());

        builder.set("name", "'Bobo'");
        assertEquals("insert into Employee (id, name) values (1, 'Bobo')", builder.toString());

    }
}
