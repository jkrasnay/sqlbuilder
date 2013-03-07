package ca.krasnay.sqlbuilder;

import java.io.Serializable;

import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 * Abstract base class of SQL creator classes.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public abstract class AbstractSqlCreator implements PreparedStatementCreator, Serializable  {

    private int paramIndex;

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
    }

    /**
     * Allocate and return a new parameter that is unique within this
     * SelectCreator. The parameter is of the form "paramN", where N is an
     * integer that is incremented each time this method is called.
     */
    public String allocateParameter() {
        return "param" + paramIndex++;
    }

}
