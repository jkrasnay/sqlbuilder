package ca.krasnay.sqlbuilder.orm;

import javax.sql.DataSource;

/**
 * Configuration of the ORM system. Each mapping must be constructed with one of
 * these objects.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class OrmConfig {

    private DataSource dataSource;

    private IdSourceFactory idSourceFactory;

    private ConverterFactory converterFactory = new DefaultConverterFactory();

    public OrmConfig() {
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public IdSourceFactory getIdSourceFactory() {
        return idSourceFactory;
    }

    public OrmConfig setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public OrmConfig setIdSource(final IdSource idSource) {
        this.idSourceFactory = new IdSourceFactory() {
            @Override
            public IdSource getIdSource(DataSource dataSource, Mapping<?> mapping) {
                return idSource;
            }
        };
        return this;
    }

    public OrmConfig setIdSourceFactory(IdSourceFactory idSourceFactory) {
        this.idSourceFactory = idSourceFactory;
        return this;
    }

    public ConverterFactory getConverterFactory() {
        return converterFactory;
    }

    public OrmConfig setConverterFactory(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
        return this;
    }

}
