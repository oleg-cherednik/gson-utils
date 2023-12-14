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
import ru.olegcherednik.json.api.JsonException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Oleg Cherednik
 * @since 10.01.2021
 */
public class IteratorTypeAdapter<V> extends TypeAdapter<Iterator<V>> {

    private final TypeAdapter<V> elementTypeAdapter;

    public static final TypeAdapterFactory INSTANCE = new TypeAdapterFactory() {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            try {
                if (!Iterator.class.isAssignableFrom(typeToken.getRawType()))
                    return null;

                ParameterizedType parameterizedType = (ParameterizedType) typeToken.getType();
                Type elementType = parameterizedType.getActualTypeArguments()[0];
                TypeAdapter<?> typeAdapter = gson.getAdapter(TypeToken.get(elementType));
                //noinspection unchecked,rawtypes
                return new IteratorTypeAdapter(createTypeAdapter(gson, typeAdapter, elementType));
            } catch (Exception e) {
                throw new JsonException(e);
            }
        }

        private TypeAdapter<?> createTypeAdapter(Gson gson, TypeAdapter<?> typeAdapter, Type elementType)
                throws Exception {
            Class<?> cls = Class.forName("com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper");
            Constructor<?> con = cls.getDeclaredConstructor(Gson.class, TypeAdapter.class, Type.class);
            con.setAccessible(true);
            return (TypeAdapter<?>) con.newInstance(gson, typeAdapter, elementType);
        }
    };

    public IteratorTypeAdapter(TypeAdapter<V> elementTypeAdapter) {
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
                } catch (IOException e) {
                    throw new JsonException(e);
                }
            }

            @Override
            public V next() {
                try {
                    if (!hasNext())
                        throw new NoSuchElementException();
                    return elementTypeAdapter.read(in);
                } catch (IOException e) {
                    throw new JsonException(e);
                }
            }
        };
    }

}
