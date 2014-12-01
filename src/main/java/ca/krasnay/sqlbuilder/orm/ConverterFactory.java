package ca.krasnay.sqlbuilder.orm;

/**
 * Factory for returning a converter given a Java field type.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface ConverterFactory {

    public Converter<?> getConverter(Class<?> fieldClass);

}
