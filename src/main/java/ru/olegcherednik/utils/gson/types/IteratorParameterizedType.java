package ru.olegcherednik.utils.gson.types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * @author Oleg Cherednik
 * @since 09.01.2021
 */
public class IteratorParameterizedType<V> implements ParameterizedType {

    private final Class<V> valueClass;

    public IteratorParameterizedType(Class<V> valueClass) {
        this.valueClass = valueClass;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[] { valueClass };
    }

    @Override
    public Type getRawType() {
        return Iterator.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
