package ca.krasnay.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

public class ExistsPredicateImpl implements ExistsPredicate {

    private List<Predicate> predicates = new ArrayList<Predicate>();

    private SelectBuilder selectBuilder;

    public ExistsPredicateImpl(String table) {
        this.selectBuilder = new SelectBuilder()
        .column("1")
        .from(table);
    }

    public ExistsPredicate and(Predicate predicate) {
        return where(predicate);
    }

    public ExistsPredicate and(String predicate) {
        return where(predicate);
    }

    public void init(AbstractSqlCreator creator) {
        for (Predicate predicate : predicates) {
            predicate.init(creator);
            selectBuilder.where(predicate.toSql());
        }
    }

    public ExistsPredicate join(String join) {
        selectBuilder.join(join);
        return this;
    }

    public String toSql() {
        return "exists (" + selectBuilder + ")";
    }

    public ExistsPredicate where(Predicate predicate) {
        predicates.add(predicate);
        return this;
    }

    public ExistsPredicate where(String predicate) {
        selectBuilder.where(predicate);
        return this;
    }

}
