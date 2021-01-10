package ru.olegcherednik.utils.gson.types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;

/**
 * @author Oleg Cherednik
 * @since 09.01.2021
 */
public class MapParameterizedType<K, V> implements ParameterizedType {

    protected final Class<K> keyClass;
    protected final Class<V> valueClass;

    public MapParameterizedType(Class<K> keyClass, Class<V> valueClass) {
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[] { keyClass, valueClass };
    }

    @Override
    public Type getRawType() {
        return LinkedHashMap.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
