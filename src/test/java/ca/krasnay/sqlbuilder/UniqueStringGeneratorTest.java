package ca.krasnay.sqlbuilder;

import junit.framework.TestCase;

public class UniqueStringGeneratorTest extends TestCase {

    public void testHappyPath() {
        UniqueStringGenerator gen = new UniqueStringGenerator(8);
        String s1 = gen.get();
        String s2 = gen.get();

        assertEquals(8, s1.length());
        assertEquals(8, s2.length());
        assertFalse(s1.equals(s2));
    }

    public void testInoffensive() {
        UniqueStringGenerator gen = new UniqueStringGenerator(8);
        assertFalse(gen.isOffensive("puppies"));
        assertFalse(gen.isOffensive("muffins"));
        assertTrue(gen.isOffensive("fvck"));
        assertTrue(gen.isOffensive("abcshitdef"));
        assertTrue(gen.isOffensive("abcsh1tdef"));
        assertTrue(gen.isOffensive("1a552"));
    }
}
