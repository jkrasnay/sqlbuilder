package ca.krasnay.sqlbuilder.orm;

import java.sql.Timestamp;
import java.util.Locale;

public class DefaultConverterFactory implements ConverterFactory {

    @Override
    public Converter<?> getConverter(Class<?> fieldClass) {
        if (fieldClass == String.class) {
            return StringConverter.getInstance();
        } else if (fieldClass == java.util.Date.class || fieldClass == Timestamp.class) {
            return TimestampConverter.getInstance();
        } else if (Enum.class.isAssignableFrom(fieldClass)) {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            Converter<?> converter = EnumStringConverter.create((Class<Enum>) fieldClass);
            return converter;
        } else if (fieldClass == Locale.class) {
            return LocaleConverter.getInstance();
        } else if (fieldClass == Class.class) {
            return ClassConverter.getInstance();
        } else {
            // TODO limit this to known-good types, e.g. primitives and their object equivalents
            // to prevent the driver trying to serialize objects
            return DefaultConverter.getInstance();
        }
    }

}
