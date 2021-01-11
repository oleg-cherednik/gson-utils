package ru.olegcherednik.utils.gson.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ru.olegcherednik.utils.gson.GsonUtilsException;
import ru.olegcherednik.utils.reflection.Invoke;
import ru.olegcherednik.utils.reflection.MethodUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Oleg Cherednik
 * @since 10.01.2021
 */
public class IteratorTypeAdapter<V> extends TypeAdapter<Iterator<V>> {

    private final TypeAdapter<V> elementTypeAdapter;

    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            try {
                if (!Iterator.class.isAssignableFrom(typeToken.getRawType()))
                    return null;

                Type elementType = getIteratorElementType(typeToken.getType(), typeToken.getRawType());
                TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
                elementTypeAdapter = Invoke.invokeConstructor("com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper",
                        new Class[] { Gson.class, TypeAdapter.class, Type.class }, new Object[] { gson, elementTypeAdapter, elementType });
                //noinspection unchecked,rawtypes
                return new IteratorTypeAdapter(elementTypeAdapter);
            } catch(Exception e) {
                throw new GsonUtilsException(e);
            }
        }

        private Type getIteratorElementType(Type context, Class<?> contextRawType) throws Exception {
            Type type = MethodUtils.invokeStaticMethod($Gson$Types.class, "getSupertype",
                    new Class[] { Type.class, Class.class, Class.class }, new Object[] { context, contextRawType, Iterator.class });

            if (type instanceof WildcardType)
                type = ((WildcardType)type).getUpperBounds()[0];
            if (type instanceof ParameterizedType)
                return ((ParameterizedType)type).getActualTypeArguments()[0];

            return Object.class;
        }
    };

    public IteratorTypeAdapter(TypeAdapter<V> elementTypeAdapter) throws Exception {
        this.elementTypeAdapter = elementTypeAdapter;
    }

    @Override
    public void write(JsonWriter out, Iterator<V> it) throws IOException {
        if (it == null) {
            out.nullValue();
            return;
        }

        out.beginArray();

        while (it.hasNext()) {
            elementTypeAdapter.write(out, it.next());
        }

        out.endArray();
    }

    @Override
    public Iterator<V> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        in.beginArray();

        return new Iterator<V>() {

            @Override
            public boolean hasNext() {
                try {
                    return in.hasNext();
                } catch(IOException e) {
                    throw new GsonUtilsException(e);
                }
            }

            @Override
            public V next() {
                try {
                    if (!hasNext())
                        throw new NoSuchElementException();
                    return elementTypeAdapter.read(in);
                } catch(IOException e) {
                    throw new GsonUtilsException(e);
                }
            }
        };
    }

}
