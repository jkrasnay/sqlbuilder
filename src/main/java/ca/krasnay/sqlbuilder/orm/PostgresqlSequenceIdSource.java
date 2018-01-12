package ca.krasnay.sqlbuilder.orm;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class PostgresqlSequenceIdSource implements IdSource {

    private JdbcTemplate jdbcTemplate;

    private String sequenceName;

    public PostgresqlSequenceIdSource(DataSource dataSource, String sequenceName) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.sequenceName = sequenceName;
    }

    @Override
    public Object nextId() {
        return jdbcTemplate.queryForObject("select nextval(?)", Integer.class, sequenceName);
    }

}
