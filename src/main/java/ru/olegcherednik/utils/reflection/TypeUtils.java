package ru.olegcherednik.utils.reflection;

/**
 * @author Oleg Cherednik
 * @since 09.01.2021
 */
public final class TypeUtils {

    public static boolean isBoolean(Class<?> type) {
        return type.isAssignableFrom(boolean.class) || isBooleanWrapper(type);
    }

    public static boolean isBooleanWrapper(Class<?> type) {
        return type.isAssignableFrom(Boolean.class);
    }

    public static boolean isInteger(Class<?> type) {
        return type.isAssignableFrom(int.class) || isIntegerWrapper(type);
    }

    public static boolean isIntegerWrapper(Class<?> type) {
        return type.isAssignableFrom(Integer.class);
    }

    public static boolean isByte(Class<?> type) {
        return type.isAssignableFrom(byte.class) || isByteWrapper(type);
    }

    public static boolean isByteWrapper(Class<?> type) {
        return type.isAssignableFrom(Byte.class);
    }

    public static boolean isShort(Class<?> type) {
        return type.isAssignableFrom(short.class) || isShortWrapper(type);
    }

    public static boolean isShortWrapper(Class<?> type) {
        return type.isAssignableFrom(Short.class);
    }

    public static boolean isLong(Class<?> type) {
        return type.isAssignableFrom(long.class) || isLongWrapper(type);
    }

    public static boolean isLongWrapper(Class<?> type) {
        return type.isAssignableFrom(Long.class);
    }

    public static boolean isChar(Class<?> type) {
        return type.isAssignableFrom(char.class) || isCharWrapper(type);
    }

    public static boolean isCharWrapper(Class<?> type) {
        return type.isAssignableFrom(Character.class);
    }

    public static boolean isFloat(Class<?> type) {
        return type.isAssignableFrom(float.class) || isFloatWrapper(type);
    }

    public static boolean isFloatWrapper(Class<?> type) {
        return type.isAssignableFrom(Float.class);
    }

    public static boolean isDouble(Class<?> type) {
        return type.isAssignableFrom(double.class) || isDoubleWrapper(type);
    }

    public static boolean isDoubleWrapper(Class<?> type) {
        return type.isAssignableFrom(Double.class);
    }

    private TypeUtils() {
    }

}
