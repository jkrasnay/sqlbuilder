package ca.krasnay.sqlbuilder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

import junit.framework.TestCase;

public class SelectCreatorTest extends TestCase {

    private static final Object getValue(Object o, String field) {
        try {
            Field f = o.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return f.get(o);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public void testWhereIn() {

        SelectCreator sc = new SelectCreator()
        .column("*")
        .from("Emp")
        .whereIn("name", Arrays.asList("Larry", "Curly", "Moe"));

        SelectBuilder builder = (SelectBuilder) getValue(sc, "builder");

        assertEquals("select * from Emp where name in (:param0, :param1, :param2)", builder.toString());

        ParameterizedPreparedStatementCreator ppsc = (ParameterizedPreparedStatementCreator) getValue(sc, "ppsc");

        Map<String, Object> map = ppsc.getParameterMap();

        assertEquals("Larry", map.get("param0"));
        assertEquals("Curly", map.get("param1"));
        assertEquals("Moe", map.get("param2"));

    }

}
