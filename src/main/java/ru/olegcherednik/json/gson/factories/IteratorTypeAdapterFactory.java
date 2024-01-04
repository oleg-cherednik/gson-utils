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

package ru.olegcherednik.json.gson.factories;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.ObjectTypeAdapter;
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
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Oleg Cherednik
 * @since 04.01.2024
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IteratorTypeAdapterFactory implements TypeAdapterFactory {

    public static final IteratorTypeAdapterFactory INSTANCE = new IteratorTypeAdapterFactory();

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (AutoCloseableIterator.class.isAssignableFrom(typeToken.getRawType()))
            return AutoCloseableIteratorTypeAdapterFactory.INSTANCE.create(gson, typeToken);
        if (Iterator.class.isAssignableFrom(typeToken.getRawType()))
            return createForIterator(gson, typeToken);
        return null;
    }

    protected <T> TypeAdapter<T> createForIterator(Gson gson, TypeToken<T> typeToken) {
        return new AdapterBar(gson.getAdapter(Object.class));
    }

    @RequiredArgsConstructor
    public static class AdapterBar<V> extends TypeAdapter<Iterator<V>> {

        protected final TypeAdapter<V> elementTypeAdapter;

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
            return new JsonReaderIterator1<>(in, elementTypeAdapter);
        }

    }

    @RequiredArgsConstructor
    public static class JsonReaderIterator1<V> implements Iterator<V> {

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

    }

}
