package ca.krasnay.sqlbuilder.orm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import junit.framework.TestCase;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import ca.krasnay.sqlbuilder.PostgresqlDialect;

public class MappingTest extends TestCase {

    public static class Employee {
        private int id;
        private int version;
        private String name;
    }

    public void testAll() throws Exception {

        Class.forName("org.h2.Driver");
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");

        JdbcTemplate t = new JdbcTemplate(ds);

        t.update("create table Employee (id int primary key, version int not null, name varchar(255))");
        t.update("create sequence myseq start with 1");

        OrmConfig ormConfig = new OrmConfig(ds, new PostgresqlDialect());

        Mapping<Employee> mapping = new Mapping<Employee>(ormConfig, Employee.class, "Employee")
        .setIdColumn(new Column("id"))
        .setVersionColumn("version")
        .addColumn("name");

        Employee emp;

        try {
            emp = mapping.findById(42);
            fail("Expected exception");
        } catch (RowNotFoundException e) {
        }

        emp = new Employee();
        emp.name = "Bobo";

        assertThat(emp.id, is(0));
        assertThat(emp.name, is("Bobo"));
        assertThat(emp.version, is(0));


        // Insert

        try {
            mapping.insert(emp);
            fail("Expected error due to unset primary key");
        } catch (Exception e1) {
        }

        emp.id = 1;

        mapping.insert(emp);

        assertThat(emp.id, is(1));
        assertThat(emp.name, is("Bobo"));
        assertThat(emp.version, is(0));

        emp = mapping.findById(1);

        assertThat(emp.id, is(1));
        assertThat(emp.name, is("Bobo"));
        assertThat(emp.version, is(0));


        // Update

        emp.name = "Bezu";
        mapping.update(emp);

        assertThat(emp.id, is(1));
        assertThat(emp.name, is("Bezu"));
        assertThat(emp.version, is(1));

        emp.name = "Boffo";
        emp.version = 0;

        try {
            mapping.update(emp);
            fail("Expected exception");
        } catch (OptimisticLockException e) {
        }


        // Delete

        try {
            mapping.deleteById(2);
            fail("Expected exception");
        } catch (RowNotFoundException e) {
        }

        mapping.deleteById(1);

        try {
            emp = mapping.findById(1);
            fail("Expected exception");
        } catch (RowNotFoundException e) {
        }

    }

}
