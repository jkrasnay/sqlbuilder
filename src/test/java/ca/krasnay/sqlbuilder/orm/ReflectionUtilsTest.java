package ca.krasnay.sqlbuilder.orm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import java.lang.reflect.Field;

import junit.framework.TestCase;

public class ReflectionUtilsTest extends TestCase {

    private static class Address {
        private String street;
    }

    private static class Person {
        private String name;
        private Address address;
    }

    public void testGetDeclaredFieldWithPath() {

        Field field = ReflectionUtils.getDeclaredFieldWithPath(Person.class, "name");

        assertThat(field.getName(), equalTo("name"));
        assertEquals(String.class, field.getType());
        assertEquals(Person.class, field.getDeclaringClass());

        field = ReflectionUtils.getDeclaredFieldWithPath(Person.class, "address.street");

        assertThat(field.getName(), equalTo("street"));
        assertEquals(String.class, field.getType());
        assertEquals(Address.class, field.getDeclaringClass());

    }

    public void testGetFieldWithPath() {

        Person person = new Person();
        person.name = "Joe";

        assertThat((String) ReflectionUtils.getFieldValueWithPath(person, "name"), equalTo("Joe"));
        assertThat((String) ReflectionUtils.getFieldValueWithPath(person, "address.street"), nullValue());

        person.address = new Address();
        assertThat((String) ReflectionUtils.getFieldValueWithPath(person, "address.street"), nullValue());

        person.address.street = "123 Main";
        assertThat((String) ReflectionUtils.getFieldValueWithPath(person, "address.street"), equalTo("123 Main"));

//        ReflectionUtils.getFieldValueWithPath(person, ".");

    }

    public void testSetFieldWithPath() {

        Person person = new Person();

        ReflectionUtils.setFieldValueWithPath(person, "name", "Joe");
        assertThat(person.name, equalTo("Joe"));

        try {
            ReflectionUtils.setFieldValueWithPath(person, "address.street", "123 Main");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
        }

        person.address = new Address();

        ReflectionUtils.setFieldValueWithPath(person, "address.street", "123 Main");

        assertThat(person.address.street, equalTo("123 Main"));
    }

}
