package ca.krasnay.sqlbuilder.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ReflectionUtils {

    /**
     * Returns an array of all declared fields in the given class and all
     * super-classes.
     */
    public static Field[] getDeclaredFieldsInHierarchy(Class<?> clazz) {

        if (clazz.isPrimitive()) {
            throw new IllegalArgumentException("Primitive types not supported.");
        }

        List<Field> result = new ArrayList<Field>();

        while (true) {

            if (clazz == Object.class) {
                break;
            }

            result.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        return result.toArray(new Field[result.size()]);
    }

    public static Field getDeclaredFieldInHierarchy(Class<?> clazz, String fieldName) {
        while (true) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (SecurityException e) {
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                if (clazz == Object.class) {
                    throw new RuntimeException(e);
                } else {
                    clazz = clazz.getSuperclass();
                }
            }
        }
    }

    /**
     * Convenience method for getting the value of a private object field,
     * without the stress of checked exceptions in the reflection API.
     *
     * @param object
     *            Object containing the field.
     * @param fieldName
     *            Name of the field whose value to return.
     */
    public static Object getFieldValue(Object object, String fieldName) {
        try {
            return getDeclaredFieldInHierarchy(object.getClass(), fieldName).get(object);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convenience method for setting the value of a private object field,
     * without the stress of checked exceptions in the reflection API.
     *
     * @param object
     *            Object containing the field.
     * @param fieldName
     *            Name of the field to set.
     * @param value
     *            Value to which to set the field.
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        try {
            getDeclaredFieldInHierarchy(object.getClass(), fieldName).set(object, value);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
