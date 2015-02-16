---
title: SQL Builder
---

SQL Builder is a Java library for dealing with dynamic SQL. SQL Builder
offers three groups of functionality:

- **Builders** simplify the creation of strings of SQL
- **Creators** and **predicates** extend builders to simplify the
  creation and execution of prepared statements
- A simple **object-relational mapping (ORM)** library makes it easy to
  map Java classes to database tables

## Builders

Builders simplify the creation of SQL strings. They know a little bit
about SQL syntax, and make the creation of dynamic SQL a little nicer in
Java. Like Java's `StringBuilder`, they use chainable calls. Here's an
example of using `SelectBuilder`.

{% highlight java linenos %}
new SelectBuilder()
.column("name")
.column("age")
.from("Employee")
.where("dept = 'engineering'")
.where("salary > 100000")
.toString();
{% endhighlight %}

This produces the SQL string `select name, age from Employee where dept
= 'engineering' and salary > 100000`. Note how `SelectBuilder` knows to
join the columns with a comma and to join the where clauses with `and`.

In addition to `SelectBuilder`, SQL Builder provides builders for
insert, update, and delete statements.

_WARNING: builders DO NOT attempt to perform any SQL escaping. You have
to be careful to escape any externally-provided strings or your
application may be vulnerable to a SQL injection attack._

For example, suppose you implement the following:

{% highlight java linenos %}
String sql = new SelectBuilder()
.column("firstName")
.from("Employee")
.where("id = " + id);

// execute the SQL on a database connection
{% endhighlight %}

If the value for `id` came from a URL, an attacker could provide a
corrupted value like this: `0; drop table Employee;`. Depending on how
you execute the SQL, you could find yourself with no more employee
records.

Fortunately, SQL Builder provides a mechanism called a _creator_ that
neatly solves this problem.


## Creators

Each builder class has a corresponding `Creator` class that implements
the `PreparedStatmentCreator` interface. SQL Builder repackages versions
of the `PreparedStatementCreator` and `JdbcTemplate` from Spring JDBC to
make it simple to execute dynamically-created SQL statements when given
a data source:

{% highlight java linenos %}
PreparedStatementCreator psc =
    new UpdateCreator("Employee")
    .setValue("name", "Bob");

new JdbcTemplate(dataSource).update(psc);
{% endhighlight %}

One big advantage of creators over builders is that you can inject data
into the executed SQL in a way that is safe from SQL injection.
The unsafe example from the builders section can be re-implemented
safely using a `SelectCreator`:

{% highlight java linenos %}
SelectCreator sc = new SelectCreator()
.column("firstName")
.from("Employee")
.where("id = :id")
.setParameter("id", id);

return new JdbcTemplate(dataSource).query(sc);
{% endhighlight %}

In this example, the creator creates the prepared statement `select
firstName from Employee where id = ?`, and uses the JDBC driver to
safely escape the value of `id`.

One nice thing about creators is that you can call their methods in any
order you like, and they'll still produce the correct SQL. For example,
consider the common case where different queries can be performed based
on the presence of requested parameters.

{% highlight java linenos %}
SelectCreator sc = new SelectCreator()
.column("name")
.column("age")
.from("Employee e");

if (deptId != null) {
    sc.where(eq("e.deptId", deptId));
}
{% endhighlight %}

You can even conditionally join other tables:

{% highlight java linenos %}
if (deptName != null) {
    sc
    .column("d.name as deptName")
    .join("Dept d on e.deptId = d.id")
    .where(eq("d.name", deptName));
}
{% endhighlight %}



## Predicates

A _predicate_ is a small class that encapsulates adding an expression to
a creator's `where` clause and setting the appropriate parameters. SQL
Builder provides a number of predicates in the `Predicates` class.
The most commonly used predicate is the equality predicate, `eq`:

{% highlight java linenos %}
import static ca.krasnay.sqlbuilder.Predicates.eq;

new SelectCreator()
.column("firstName")
.from("Employee")
.where(eq("id", id));
{% endhighlight %}

Here, the two step process of adding a string to the `where` clause and
separately calling `setParameter` from the previous example is replaced
with a neat invocation of the `eq` predicate.

Below is another example where a predicate can save a tricky bit of
work, especially when the passed string of department IDs can vary in
length:

{% highlight java linenos %}
import static ca.krasnay.sqlbuilder.Predicates.in;

List<Integer> deptIds = //...

new SelectCreator()
.column("firstName")
.from("Employee")
.where(in("deptId", deptIds));
{% endhighlight %}

You can easily implement your own predicates. Here is the implementation
of the `Predicates.eq` method as an example:

{% highlight java linenos %}
public static Predicate eq(final String expr, final Object value) {
    return new Predicate() {
        private String param;
        public void init(AbstractSqlCreator creator) {
            param = creator.allocateParameter();
            creator.setParameter(param, value);
        }
        public String toSql() {
            return String.format("%s = :%s", expr, param);
        }
    };
}
{% endhighlight %}

## Object-Relational Mapping

SQL Builder implements a simple object-relational mapping tool. Unlike
some more complicated ORM tools such as Hibernate and JPA, SQL Builder's
ORM makes some simplifying assumptions:

- there is a one-to-one mapping between tables and entity classes
- there is generally a one-to-one mapping between columns and entity
  class fields
- tables have a single primary key column
- optimistic locking is implemented by a single numeric column

## Maven

Add the following dependency to your POM:

{% highlight xml linenos %}
<dependency>
  <groupId>ca.krasnay</groupId>
  <artifactId>sqlbuilder</artifactId>
  <version>1.2</version>
</dependency>
{% endhighlight %}

SQL Builder depends on slf4j and Spring JDBC, so be sure to add the
desired versions of these to your `<dependencyManagement>` section.

