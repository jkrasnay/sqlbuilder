package ca.krasnay.sqlbuilder;

import junit.framework.TestCase;

public class SelectBuilderTest extends TestCase {

    public void testBasics() {

        //
        // Simple tables
        //

        SelectBuilder sb = new SelectBuilder("Employee");
        assertEquals("select * from Employee", sb.toString());

        sb = new SelectBuilder("Employee e");
        assertEquals("select * from Employee e", sb.toString());

        sb = new SelectBuilder("Employee e").column("name");
        assertEquals("select name from Employee e", sb.toString());

        sb = new SelectBuilder("Employee e").column("name").column("age");
        assertEquals("select name, age from Employee e", sb.toString());

        sb = new SelectBuilder("Employee e").column("name as n").column("age");
        assertEquals("select name as n, age from Employee e", sb.toString());

        //
        // Where clauses
        //

        sb = new SelectBuilder("Employee e").where("name like 'Bob%'");
        assertEquals("select * from Employee e where name like 'Bob%'", sb.toString());

        sb = new SelectBuilder("Employee e").where("name like 'Bob%'").where("age > 37");
        assertEquals("select * from Employee e where name like 'Bob%' and age > 37", sb.toString());

        //
        // Join clauses
        //

        sb = new SelectBuilder("Employee e").join("Department d on e.dept_id = d.id");
        assertEquals("select * from Employee e join Department d on e.dept_id = d.id", sb.toString());

        sb = new SelectBuilder("Employee e").join("Department d on e.dept_id = d.id").where("name like 'Bob%'");
        assertEquals("select * from Employee e join Department d on e.dept_id = d.id where name like 'Bob%'", sb
                .toString());

        //
        // Order by clauses
        //

        sb = new SelectBuilder("Employee e").orderBy("name");
        assertEquals("select * from Employee e order by name", sb.toString());

        sb = new SelectBuilder("Employee e").orderBy("name desc").orderBy("age");
        assertEquals("select * from Employee e order by name desc, age", sb.toString());

        sb = new SelectBuilder("Employee").where("name like 'Bob%'").orderBy("age");
        assertEquals("select * from Employee where name like 'Bob%' order by age", sb.toString());

        //
        // For Update
        //

        sb = new SelectBuilder("Employee").where("id = 42").forUpdate();
        assertEquals("select * from Employee where id = 42 for update", sb.toString());

    }

    public void testLimits() {

        SelectBuilder sb = new SelectBuilder()
                .from("test_table")
                .column("a")
                .column("b")
                .limit(10);

        assertEquals("select a, b from test_table limit 10", sb.toString());

        sb = sb.limit(10, 4);

        assertEquals("select a, b from test_table limit 10 4", sb.toString());
    }

    public void testUnions() {

        SelectBuilder sb = new SelectBuilder()
        .column("a")
        .column("b")
        .from("Foo")
        .where("a > 10")
        .orderBy("1");

        sb.union(new SelectBuilder()
        .column("c")
        .column("d")
        .from("Bar"));

        assertEquals("select a, b from Foo where a > 10 union select c, d from Bar order by 1", sb.toString());

    }
}
