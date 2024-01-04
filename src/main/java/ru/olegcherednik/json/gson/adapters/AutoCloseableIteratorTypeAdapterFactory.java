/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package ru.olegcherednik.json.gson.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.olegcherednik.json.api.JsonException;
import ru.olegcherednik.json.api.iterator.AutoCloseableIterator;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.NoSuchElementException;

/**
 * @author Oleg Cherednik
 * @since 04.01.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AutoCloseableIteratorTypeAdapterFactory implements TypeAdapterFactory {

    public static final AutoCloseableIteratorTypeAdapterFactory INSTANCE =
            new AutoCloseableIteratorTypeAdapterFactory();

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (!AutoCloseableIterator.class.isAssignableFrom(typeToken.getRawType()))
            return null;

        Type elementType = ((ParameterizedType) typeToken.getType()).getActualTypeArguments()[0];
        TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
        //noinspection unchecked,rawtypes
        return new Adapter(elementTypeAdapter);
    }

    @RequiredArgsConstructor
    public static class Adapter<V> extends TypeAdapter<AutoCloseableIterator<V>> {

        protected final TypeAdapter<V> elementTypeAdapter;

        @Override
        public void write(JsonWriter out, AutoCloseableIterator<V> it) throws IOException {
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
        public AutoCloseableIterator<V> read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            in.beginArray();
            return new JsonReaderIterator<>(in, elementTypeAdapter);
        }

    }

    @RequiredArgsConstructor
    public static class JsonReaderIterator<V> implements AutoCloseableIterator<V> {

        protected final JsonReader in;
        protected final TypeAdapter<V> typeAdapter;

        @Override
        public boolean hasNext() {
            try {
                return in.hasNext();
            } catch (IOException e) {
                throw new JsonException(e);
            }
        }

        @Override
        public V next() {
            try {
                if (!hasNext())
                    throw new NoSuchElementException();
                return typeAdapter.read(in);
            } catch (IOException e) {
                throw new JsonException(e);
            }
        }

        @Override
        public void close() throws Exception {
            in.close();
        }
    }

}
