package ru.olegcherednik.utils.gson.types;

import com.google.gson.internal.$Gson$Types;
import ru.olegcherednik.utils.reflection.MethodUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Iterator;

/**
 * @author Oleg Cherednik
 * @since 10.01.2021
 */
public final class TypeUtils {

    public static Type getIteratorElementType(Type context, Class<?> contextRawType) throws Exception {
        Type iteratorType = MethodUtils.invokeStaticMethod($Gson$Types.class, "getSupertype",
                new Class[] { Type.class, Class.class, Class.class }, new Object[] { context, contextRawType, Iterator.class });

        if (iteratorType instanceof WildcardType)
            iteratorType = ((WildcardType)iteratorType).getUpperBounds()[0];
        if (iteratorType instanceof ParameterizedType)
            return ((ParameterizedType)iteratorType).getActualTypeArguments()[0];

        return Object.class;
    }

    private TypeUtils() {
    }

}
