package ca.krasnay.sqlbuilder;

import junit.framework.TestCase;
import ca.krasnay.sqlbuilder.ParameterizedPreparedStatementCreator.SqlAndParams;

public class ParameterizedPreparedStatementCreatorTest extends TestCase {

    private void assertResult(ParameterizedPreparedStatementCreator ppsc, String psSql, Object... params) {

        SqlAndParams sap = ppsc.createSqlAndParams();

        assertEquals(psSql, sap.getSql());
        assertEquals(params.length, sap.getParams().size());
        for (int i = 0; i < params.length; i++) {
            assertEquals(params[i], sap.getParams().get(i));
        }
    }

    public void testAll() {

        ParameterizedPreparedStatementCreator ppsc;

        ppsc = new ParameterizedPreparedStatementCreator().setSql("");
        assertResult(ppsc, "");

        ppsc = new ParameterizedPreparedStatementCreator().setSql("select * from Employee");
        assertResult(ppsc, "select * from Employee");

        ppsc = new ParameterizedPreparedStatementCreator().setSql("select * from Employee where name = :name");
        try {
            ppsc.createSqlAndParams();
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown parameter 'name' at position 36", e.getMessage());
        }

        ppsc = new ParameterizedPreparedStatementCreator().setSql("select * from Employee where name = :name")
                .setParameter("name", "Joe");
        assertResult(ppsc, "select * from Employee where name = ?", "Joe");

        ppsc = new ParameterizedPreparedStatementCreator().setSql(
                "select * from Employee where name = :name and age > 37").setParameter("name", "Joe");
        assertResult(ppsc, "select * from Employee where name = ? and age > 37", "Joe");

        ppsc = new ParameterizedPreparedStatementCreator().setSql(
                "select * from Employee where name = :name and age > :age").setParameter("name", "Joe").setParameter(
                "age", 37);
        assertResult(ppsc, "select * from Employee where name = ? and age > ?", "Joe", 37);

    }
}
