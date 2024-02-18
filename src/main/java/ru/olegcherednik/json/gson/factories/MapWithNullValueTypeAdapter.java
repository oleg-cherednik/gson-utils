/*
 * Copyright Oleg Cherednik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.olegcherednik.json.gson.factories;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map;

/**
 * @author Oleg Cherednik
 * @since 06.02.2024
 */
@RequiredArgsConstructor
public class MapWithNullValueTypeAdapter implements TypeAdapterFactory {

    private final boolean serializeNullValue;

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<? super T> rawType = typeToken.getRawType();

        if (!Map.class.isAssignableFrom(rawType))
            return null;
        if (!serializeNullValue)
            return null;

        TypeAdapter<Map<?, ?>> delegate = (TypeAdapter<Map<?, ?>>) gson.getDelegateAdapter(this, typeToken);
        return new Adapter<>(delegate, gson);
    }

    protected static class Adapter<T> extends TypeAdapter<T> {

        private final TypeAdapter<Map<?, ?>> delegate;
        private final Gson gson;

        public Adapter(TypeAdapter<Map<?, ?>> delegate, Gson gson) {
            this.delegate = delegate;
            this.gson = gson;
        }

        @Override
        public T read(JsonReader in) throws IOException {
            return (T) delegate.read(in);
        }

        @Override
        public void write(JsonWriter out, T map) throws IOException {
            if (map == null)
                out.nullValue();
            else {
                final boolean serializeNulls = out.getSerializeNulls();
                final boolean actualSerializeNulls = serializeNulls || gson.serializeNulls();
                out.beginObject();

                for (Map.Entry<?, ?> entry : ((Map<?, ?>) map).entrySet()) {
                    writeKey(out, entry.getKey(), actualSerializeNulls);
                    writeValue(out, entry.getValue(), actualSerializeNulls);
                }

                out.endObject();
                out.setSerializeNulls(serializeNulls);
            }
        }

        private void writeKey(JsonWriter out, Object key, boolean actualSerializeNulls) throws IOException {
            out.setSerializeNulls(actualSerializeNulls);

            if (key == null)
                out.nullValue();
            else {
                TypeToken<Object> typeToken = (TypeToken<Object>) TypeToken.get(key.getClass());
                TypeAdapter<Object> typeAdapter = gson.getAdapter(typeToken);
                out.name(typeAdapter.toJsonTree(key).getAsString());
            }
        }

        private void writeValue(JsonWriter out, Object value, boolean actualSerializeNulls) throws IOException {
            if (value == null) {
                out.setSerializeNulls(true);
                out.nullValue();
            } else {
                out.setSerializeNulls(actualSerializeNulls);
                TypeToken<Object> typeToken = (TypeToken<Object>) TypeToken.get(value.getClass());
                gson.getAdapter(typeToken).write(out, value);
            }

            out.setSerializeNulls(false);
        }

    }

}
