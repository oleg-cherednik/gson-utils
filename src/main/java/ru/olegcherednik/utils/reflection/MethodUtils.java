package ru.olegcherednik.utils.reflection;

import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Oleg Cherednik
 * @since 10.01.2021
 */
@SuppressWarnings("MethodCanBeVariableArityMethod")
public final class MethodUtils {

    public static <T> T invokeStaticMethod(Class<?> cls, String name, Class<?>[] types, Object[] values) throws Exception {
        return Invoke.invokeFunction(getMethod(cls, name, types), method -> (T)method.invoke(null, values));
    }

    private static Method getMethod(Class<?> cls, String name, Class<?>... types) throws NoSuchMethodException {
        Method method = null;

        while (method == null && cls != null) {
            try {
                method = cls.getDeclaredMethod(name, types);
            } catch(NoSuchMethodException ignored) {
                cls = cls.getSuperclass();
            }
        }

        return Optional.ofNullable(method).orElseThrow(NoSuchElementException::new);
    }

    private MethodUtils() {
    }

}
