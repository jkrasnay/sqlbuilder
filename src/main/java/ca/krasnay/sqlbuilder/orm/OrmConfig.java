package ca.krasnay.sqlbuilder.orm;

import javax.sql.DataSource;

import ca.krasnay.sqlbuilder.Dialect;
import ca.krasnay.sqlbuilder.Supplier;

/**
 * Configuration of the ORM system. Each mapping must be constructed with one of
 * these objects.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class OrmConfig {

    private DataSource dataSource;

    private Dialect dialect;

    private ConverterFactory converterFactory = new DefaultConverterFactory();

    public OrmConfig(DataSource dataSource, Dialect dialect) {
        super();
        this.dataSource = dataSource;
        this.dialect = dialect;
    }

    public ConverterFactory getConverterFactory() {
        return converterFactory;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public Supplier<Integer> getSequence(String sequenceName) {
        return dialect.getSequence(dataSource, sequenceName);
    }

    public OrmConfig setConverterFactory(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
        return this;
    }

}
