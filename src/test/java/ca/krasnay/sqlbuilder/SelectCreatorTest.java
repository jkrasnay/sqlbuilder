package ca.krasnay.sqlbuilder;

import junit.framework.TestCase;

public class SelectCreatorTest extends TestCase {

    public void testAllocateParameter() {

        SelectCreator sc = new SelectCreator();
        assertEquals("param0", sc.allocateParameter());
        assertEquals("param1", sc.allocateParameter());
        assertEquals("param2", sc.allocateParameter());

    }
}
