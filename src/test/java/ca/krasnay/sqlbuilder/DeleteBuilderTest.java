package ca.krasnay.sqlbuilder;

import junit.framework.TestCase;

public class DeleteBuilderTest extends TestCase {

    public void testAll() {
        assertEquals("delete from Foo", new DeleteBuilder("Foo").toString());
        assertEquals("delete from Foo where id = 1", new DeleteBuilder("Foo").where("id = 1").toString());
        assertEquals("delete from Foo where id = 1 and colour = 'red'", new DeleteBuilder("Foo").where("id = 1").where("colour = 'red'").toString());
    }
}
