package ca.krasnay.sqlbuilder;

import static ca.krasnay.sqlbuilder.Predicates.eq;
import static ca.krasnay.sqlbuilder.Predicates.exists;
import static ca.krasnay.sqlbuilder.Predicates.in;
import static ca.krasnay.sqlbuilder.Predicates.not;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class PredicateTest extends TestCase {

    public void testEq() {

        SelectCreator sc = new SelectCreator()
        .column("*")
        .from("Emp")
        .where(eq("name", "Bob"));

        assertEquals("select * from Emp where name = :param0", sc.getBuilder().toString());
        assertEquals("Bob", sc.getPreparedStatementCreator().getParameterMap().get("param0"));

    }

    public void testExists() {

        SelectCreator sc = new SelectCreator()
        .column("*")
        .from("Emp e")
        .where(exists("SickDay sd").where("sd.emp_id = e.id").and(eq("sd.dow", "Monday")));

        assertEquals("select * from Emp e where exists (select 1 from SickDay sd where sd.emp_id = e.id and sd.dow = :param0)", sc.getBuilder().toString());
        assertEquals("Monday", sc.getPreparedStatementCreator().getParameterMap().get("param0"));

    }

    public void testInArray() {

        SelectCreator sc = new SelectCreator()
        .column("*")
        .from("Emp")
        .where(in("name", "Larry", "Curly", "Moe"));

        assertEquals("select * from Emp where name in (:param0, :param1, :param2)", sc.getBuilder().toString());

        ParameterizedPreparedStatementCreator ppsc = sc.getPreparedStatementCreator();

        Map<String, Object> map = ppsc.getParameterMap();

        assertEquals("Larry", map.get("param0"));
        assertEquals("Curly", map.get("param1"));
        assertEquals("Moe", map.get("param2"));

    }

    public void testInList() {


        List<String> names = new ArrayList<String>();
        names.add("Larry");
        names.add("Curly");
        names.add("Moe");

        SelectCreator sc = new SelectCreator()
        .column("*")
        .from("Emp")
        .where(in("name", names));

        assertEquals("select * from Emp where name in (:param0, :param1, :param2)", sc.getBuilder().toString());

        ParameterizedPreparedStatementCreator ppsc = sc.getPreparedStatementCreator();

        Map<String, Object> map = ppsc.getParameterMap();

        assertEquals("Larry", map.get("param0"));
        assertEquals("Curly", map.get("param1"));
        assertEquals("Moe", map.get("param2"));

    }

    public void testNot() {

        SelectCreator sc = new SelectCreator()
        .column("*")
        .from("Emp")
        .where(not(eq("name", "Bob")));

        assertEquals("select * from Emp where not (name = :param0)", sc.getBuilder().toString());
        assertEquals("Bob", sc.getPreparedStatementCreator().getParameterMap().get("param0"));

    }
}
