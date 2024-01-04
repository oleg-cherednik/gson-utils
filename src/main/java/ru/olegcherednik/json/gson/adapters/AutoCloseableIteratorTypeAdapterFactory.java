package ru.olegcherednik.json.gson.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.olegcherednik.json.api.iterator.AutoCloseableIterator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Oleg Cherednik
 * @since 04.01.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AutoCloseableIteratorTypeAdapterFactory implements TypeAdapterFactory {

    public static final AutoCloseableIteratorTypeAdapterFactory INSTANCE =
            new AutoCloseableIteratorTypeAdapterFactory();

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (!AutoCloseableIterator.class.isAssignableFrom(typeToken.getRawType()))
            return null;

        ParameterizedType parameterizedType = (ParameterizedType) typeToken.getType();
        Type elementType = parameterizedType.getActualTypeArguments()[0];
        TypeAdapter<?> typeAdapter = gson.getAdapter(TypeToken.get(elementType));
        //noinspection unchecked,rawtypes
        return new AutoCloseableIteratorTypeAdapter(typeAdapter);
    }

}
