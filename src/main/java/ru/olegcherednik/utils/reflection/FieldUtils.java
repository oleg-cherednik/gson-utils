package ru.olegcherednik.utils.reflection;

import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Oleg Cherednik
 * @since 09.01.2021
 */
public final class FieldUtils {

    public static <T> T getFieldValue(Object obj, String name) throws Exception {
        return Invoke.invokeFunction(getField(obj.getClass(), name), field -> (T)field.get(obj));
    }

    public static void setFieldValue(Object obj, String name, Object value) throws Exception {
        Invoke.invokeConsumer(getField(obj.getClass(), name), field -> setFieldValueImpl(field, obj, value));
    }

    private static Field getField(Class<?> cls, String name) throws NoSuchFieldException {
        Field field = null;

        while (field == null && cls != null) {
            try {
                field = cls.getDeclaredField(name);
            } catch(NoSuchFieldException ignored) {
                cls = cls.getSuperclass();
            }
        }

        return Optional.ofNullable(field).orElseThrow(NoSuchElementException::new);
    }

    public static void setFieldValueImpl(Field field, Object obj, Object value) throws IllegalAccessException {
        if (TypeUtils.isInteger(field.getType()))
            field.setInt(obj, (Integer)value);
        else if (TypeUtils.isBoolean(field.getType()))
            field.setBoolean(obj, (Boolean)value);
        else if (TypeUtils.isByte(field.getType()))
            field.setByte(obj, (Byte)value);
        else if (TypeUtils.isChar(field.getType()))
            field.setChar(obj, (Character)value);
        else if (TypeUtils.isDouble(field.getType()))
            field.setDouble(obj, (Double)value);
        else if (TypeUtils.isLong(field.getType()))
            field.setLong(obj, (Long)value);
        else if (TypeUtils.isShort(field.getType()))
            field.setShort(obj, (Short)value);
        else if (TypeUtils.isFloat(field.getType()))
            field.setFloat(obj, (Float)value);
        else
            field.set(obj, value);
    }

    private FieldUtils() {
    }

}
