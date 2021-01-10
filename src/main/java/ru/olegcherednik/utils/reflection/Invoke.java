package ru.olegcherednik.utils.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * @author Oleg Cherednik
 * @since 09.01.2021
 */
@SuppressWarnings("MethodCanBeVariableArityMethod")
public final class Invoke {

    public static <T> T invokeConstructor(String className, Class<?>[] types, Object[] values) throws Exception {
        return invokeConstructor((Class<T>)Class.forName(className), types, values);
    }

    public static <T> T invokeConstructor(Class<T> cls, Class<?>[] types, Object[] values) throws Exception {
        return invokeFunction(cls.getDeclaredConstructor(types), constructor -> constructor.newInstance(values));
    }

    public static <T extends AccessibleObject & Member> void invokeConsumer(T accessibleObject, Consumer<T> task) throws Exception {
        invokeFunction(accessibleObject, (Function<T, Void>)func -> {
            task.accept(accessibleObject);
            return null;
        });
    }

    public static <T extends AccessibleObject & Member, R> R invokeFunction(T accessibleObject, Function<T, R> task) throws Exception {
        boolean accessible = accessibleObject.isAccessible();

        try {
            accessibleObject.setAccessible(true);

            if (accessibleObject instanceof Field)
                return invokeWithModifiers((Field)accessibleObject, (Function<Field, R>)task);
            return task.apply(accessibleObject);
        } finally {
            accessibleObject.setAccessible(accessible);
        }
    }

    private static <R> R invokeWithModifiers(Field accessibleObject, Function<Field, R> task) throws Exception {
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        boolean accessible = modifiersField.isAccessible();
        int modifiers = accessibleObject.getModifiers();

        try {
            modifiersField.setAccessible(true);
            modifiersField.setInt(accessibleObject, modifiers & ~Modifier.FINAL);
            return task.apply(accessibleObject);
        } finally {
            modifiersField.setInt(accessibleObject, modifiers);
            modifiersField.setAccessible(accessible);
        }
    }

    public interface Function<T, R> {

        R apply(T t) throws Exception;
    }

    public interface Consumer<T> {

        void accept(T t) throws Exception;
    }

    private Invoke() {
    }

}
