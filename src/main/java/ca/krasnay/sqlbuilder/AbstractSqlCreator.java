package ca.krasnay.sqlbuilder;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 * Abstract base class of SQL creator classes.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public abstract class AbstractSqlCreator implements PreparedStatementCreator, Serializable  {

    private int paramIndex;

    private ParameterizedPreparedStatementCreator ppsc = new ParameterizedPreparedStatementCreator();

    public AbstractSqlCreator() {

    }

    /**
     * Copy constructor. Used by cloneable creators.
     *
     * @param other
     *            AbstractSqlCreator being cloned.
     */
    public AbstractSqlCreator(AbstractSqlCreator other) {
        this.paramIndex = other.paramIndex;
        this.ppsc = other.ppsc.clone();
    }

    /**
     * Allocate and return a new parameter that is unique within this
     * SelectCreator. The parameter is of the form "paramN", where N is an
     * integer that is incremented each time this method is called.
     */
    public String allocateParameter() {
        return "param" + paramIndex++;
    }

    public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
        return ppsc.setSql(getBuilder().toString()).createPreparedStatement(conn);
    }

    /**
     * Returns the builder for this creator.
     */
    protected abstract AbstractSqlBuilder getBuilder();

    /**
     * Returns the prepared statement creator for this creator.
     */
    protected ParameterizedPreparedStatementCreator getPreparedStatementCreator() {
        return ppsc;
    }

    /**
     * Sets a parameter for the creator.
     */
    public AbstractSqlCreator setParameter(String name, Object value) {
        ppsc.setParameter(name, value);
        return this;
    }

    @Override
    public String toString() {
        return ppsc.setSql(getBuilder().toString()).toString();
    }

}
