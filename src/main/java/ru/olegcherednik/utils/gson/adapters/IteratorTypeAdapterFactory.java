package ru.olegcherednik.utils.gson.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ru.olegcherednik.utils.gson.GsonUtilsException;
import ru.olegcherednik.utils.gson.types.TypeUtils;
import ru.olegcherednik.utils.reflection.Invoke;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * @author Oleg Cherednik
 * @since 09.01.2021
 */
public class IteratorTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<? super T> rawType = typeToken.getRawType();

        if (!Iterator.class.isAssignableFrom(rawType))
            return null;

        try {
            Type elementType = TypeUtils.getIteratorElementType(typeToken.getType(), rawType);
            TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
            return new Adapter(gson, elementType, elementTypeAdapter);
        } catch(Exception e) {
            throw new GsonUtilsException(e);
        }
    }

    private static final class Adapter<T> extends TypeAdapter<Iterator<T>> {

        private final TypeAdapter<T> elementTypeAdapter;

        public Adapter(Gson context, Type elementType, TypeAdapter<T> elementTypeAdapter) throws Exception {
            this.elementTypeAdapter = Invoke.invokeConstructor("com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper",
                    new Class[] { Gson.class, TypeAdapter.class, Type.class }, new Object[] { context, elementTypeAdapter, elementType });
        }

        @Override
        public void write(JsonWriter out, Iterator<T> it) throws IOException {
//            if (collection == null) {
//                out.nullValue();
//                return;
//            }
//
//            out.beginArray();
//            for (E element : collection) {
//                elementTypeAdapter.write(out, element);
//            }
//            out.endArray();

//            if (map == null) {
//                out.nullValue();
//                return;
//            }
//
//            if (!complexMapKeySerialization) {
//                out.beginObject();
//                for (Map.Entry<K, V> entry : map.entrySet()) {
//                    out.name(String.valueOf(entry.getKey()));
//                    valueTypeAdapter.write(out, entry.getValue());
//                }
//                out.endObject();
//                return;
//            }
//
//            boolean hasComplexKeys = false;
//            List<JsonElement> keys = new ArrayList<JsonElement>(map.size());
//
//            List<V> values = new ArrayList<V>(map.size());
//            for (Map.Entry<K, V> entry : map.entrySet()) {
//                JsonElement keyElement = keyTypeAdapter.toJsonTree(entry.getKey());
//                keys.add(keyElement);
//                values.add(entry.getValue());
//                hasComplexKeys |= keyElement.isJsonArray() || keyElement.isJsonObject();
//            }
//
//            if (hasComplexKeys) {
//                out.beginArray();
//                for (int i = 0, size = keys.size(); i < size; i++) {
//                    out.beginArray(); // entry array
//                    Streams.write(keys.get(i), out);
//                    valueTypeAdapter.write(out, values.get(i));
//                    out.endArray();
//                }
//                out.endArray();
//            } else {
//                out.beginObject();
//                for (int i = 0, size = keys.size(); i < size; i++) {
//                    JsonElement keyElement = keys.get(i);
//                    out.name(keyToString(keyElement));
//                    valueTypeAdapter.write(out, values.get(i));
//                }
//                out.endObject();
//            }
        }

        @Override
        public Iterator<T> read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            in.beginArray();

            return new Iterator<T>() {

                @Override
                public boolean hasNext() {
                    try {
                        return in.hasNext();
                    } catch(IOException ignore) {
                        return false;
                    }
                }

                @Override
                public T next() {
                    try {
                        return elementTypeAdapter.read(in);
                    } catch(IOException e) {
                        throw new GsonUtilsException(e);
                    }
                }
            };
        }

    }
}
