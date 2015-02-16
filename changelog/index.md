---
title: Changelog
---

# Changelog

## Version 2.0

Version 2.0 is a major release with a number of breaking changes, as
follows.

**1. The `Mapping.insert` method no longer automatically assigns a primary
key.** This simplifies a number of use-cases:

- where the primary key is a UUID that is assigned when the entity
  object is constructed
- where the mapping is used in a library whose key allocation strategy
  may not align with the rest of the application.

This change has a number of knock-on effects:

- classes `IdSource`, `IdSourceFactory`, and
  `PostgresqlSequenceIdSource` have been deleted
- the `OrmConfig.idSourceFactory` property has been removed. Further,
  the remaining `dataSource` and `dialect` fields are now supplied to
  the constructor
- the new `Dialect.getSequence` method returns a `Supplier<Integer>`
  object that returns sequence values for the particular dialect
- the new `OrmConfig.getSequence` method simplifies calling
  `Dialect.getSequence`
- the new `PostgresqlSequence` and `UniqueStringGenerator` classes
  provide ways to generate keys from an PostgreSQL sequence and a random
  bit stream, respectively

I recommend changing your DAO classes to compensate, as follows:

{% highlight java linenos %}
public class WidgetDaoImpl implements WidgetDao {

    public Mapping<Widget> mapping;
    public Supplier<Integer> sequence;

    public WidgetDaoImpl(OrmConfig ormConfig) {

        mapping = new Mapping<Widget>(ormConfig, Widget.class)
        .setIdColumn("id")
        .setVersionColumn("version")
        .addFields();

        sequence = ormConfig.getSequence("widget_id_seq");
    }

    public void insert(Widget w) {
        w.setId(sequence.get());
        mapping.insert(w);
    }
}
{% endhighlight %}



## Version 1.2

- ORM: Implement StringMapConverter
- ORM: Ignore static fields in Mapping.addFields
- ORM: Support embedded objects

## Version 1.1

- Implement orm package

