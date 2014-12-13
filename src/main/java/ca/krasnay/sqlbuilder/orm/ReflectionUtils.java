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

    /**
     * Returns the Field for a given parent class and a dot-separated path of
     * field names.
     *
     * @param clazz
     *            Parent class.
     * @param path
     *            Path to the desired field.
     */
    public static Field getDeclaredFieldWithPath(Class<?> clazz, String path) {

        int lastDot = path.lastIndexOf('.');

        if (lastDot > -1) {
            String parentPath = path.substring(0, lastDot);
            String fieldName = path.substring(lastDot + 1);
            Field parentField = getDeclaredFieldWithPath(clazz, parentPath);
            return getDeclaredFieldInHierarchy(parentField.getType(), fieldName);
        } else {
            return getDeclaredFieldInHierarchy(clazz, path);
        }
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
     * Returns the value of a field identified using a path from the parent.
     *
     * @param object
     *            Parent object.
     * @param path
     *            Path to identify the field. May contain one or more dots, e.g.
     *            "address.city".
     * @return The value of the field, or null if any of the path components are
     *         null.
     */
    public static Object getFieldValueWithPath(Object object, String path) {

        int lastDot = path.lastIndexOf('.');

        if (lastDot > -1) {

            String parentPath = path.substring(0, lastDot);
            String field = path.substring(lastDot + 1);

            Object parentObject = getFieldValueWithPath(object, parentPath);

            if (parentObject == null) {
                return null;
            } else {
                return getFieldValue(parentObject, field);
            }
        } else {
            return getFieldValue(object, path);
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

    /**
     * Returns the value of a field identified using a path from the parent.
     *
     *
     * @param object
     *            Parent object.
     * @param path
     *            Path to identify the field. May contain one or more dots, e.g.
     *            "address.city".
     * @param value
     *            Value to which to set the field.
     * @throws IllegalStateException
     *             if one of the intermediate objects on the path is null.
     */
    public static void setFieldValueWithPath(Object object, String path, Object value) throws IllegalStateException {

        int lastDot = path.lastIndexOf('.');

        if (lastDot > -1) {

            String parentPath = path.substring(0, lastDot);
            String field = path.substring(lastDot + 1);
            Object parentObject = getFieldValueWithPath(object, parentPath);

            if (parentObject == null) {
                throw new IllegalStateException(String.format("Null value for %s while accessing %s on object %s",
                        parentPath,
                        path,
                        object));
            }

            setFieldValue(parentObject, field, value);

        } else {
            setFieldValue(object, path, value);
        }

    }


}
