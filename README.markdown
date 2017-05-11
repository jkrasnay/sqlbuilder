# SQL Builder Utilities

This package contains a number of utility classes to simplify working
with SQL.

## Maven

Add the following dependency to your POM:

```xml
<dependency>
  <groupId>ca.krasnay</groupId>
  <artifactId>sqlbuilder</artifactId>
  <version>1.2</version>
</dependency>
```

SQL Builder depends on slf4j and Spring JDBC, so be sure to add the
desired versions of these to your `<dependencyManagement>` section.

## Gradle

Add the following dependency to your build.gradle:

```gradle
compile 'ca.krasnay:sqlbuilder:1.2'
```

## Builders

Builders simplify the creation of SQL strings. They know a little bit
about SQL syntax, and make the creation of dynamic SQL a little nicer in
Java. Like Java's `StringBuilder`, they use chainable calls. Here's an
example of using `SelectBuilder`.

```java
new SelectBuilder()
    .column("name")
    .column("age")
    .from("Employee")
    .where("dept = 'engineering'")
    .where("salary > 100000")
    .toString();
```

This produces the SQL string `select name, age from Employee where dept
= 'engineering' and salary > 100000`. Note how `SelectBuilder` knows to
join the columns with a comma and to join the where clauses with `and`.

For more info, see
<http://john.krasnay.ca/2010/02/15/building-sql-in-java.html>

## ParameterizedPreparedStatementCreator

Spring has a powerful abstraction known as `JdbcTemplate` that makes
working with JDBC bearable. `JdbcTemplate` takes care of the proper
allocation and disposal of JDBC connections from a `DataSource`. It
never returns a `Connection`; instead, connection objects are passed to
callbacks provided by the caller. Once such callback, the
`PreparedStatementCreator`, is used to create a prepared statement given
a connection.

In a typical `PreparedStatementCreator`, one creates SQL with
substitutable parameters indicated by question marks, then sets the
parameter values by index. Keeping track of these indexes can be
challenging when working with dynamic SQL. To simplify this,
`ParamerizedPreparedStatementCreator` uses named parameters. Here's an
example:

```java
PreparedStatementCreator psc =
    new ParameterizedPreparedStatementCreator()
        .setSql("update Employee set name = :name where id = :id")
        .setParameter("name", "Bob")
        .setParameter("id", 42);

new JdbcTemplate(dataSource).update(psc);
```

## Creators

Each builder class has a corresponding `Creator` class that combines a
builder and a `ParameterizedPreparedStatmentCreator`.

```java
PreparedStatementCreator psc =
    new UpdateCreator("Employee")
        .setValue("name", "Bob")
        .whereEquals("id", 42);

new JdbcTemplate(dataSource).update(psc);
```

Creators don't add much functionality themselves, but they make working
with builders and `ParameterizedPreparedStatementCreator`s a little
easier (plus you don't have to keep typing that ridiculously large class
name!).
