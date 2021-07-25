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
package ru.olegcherednik.gson.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ru.olegcherednik.gson.GsonUtilsException;
import ru.olegcherednik.utils.reflection.ConstructorUtils;
import ru.olegcherednik.utils.reflection.MethodUtils;

import java.io.IOException;
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

    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            try {
                if (!Iterator.class.isAssignableFrom(typeToken.getRawType()))
                    return null;

                Type elementType = getIteratorElementType(typeToken.getType(), typeToken.getRawType());
                TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
                elementTypeAdapter = ConstructorUtils.invokeConstructor("com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper",
                        Gson.class, gson,
                        TypeAdapter.class, elementTypeAdapter,
                        Type.class, elementType);
                //noinspection unchecked,rawtypes
                return new IteratorTypeAdapter(elementTypeAdapter);
            } catch (Exception e) {
                throw new GsonUtilsException(e);
            }
        }

        private Type getIteratorElementType(Type context, Class<?> contextRawType) throws Exception {
            Type type = MethodUtils.invokeStaticMethod($Gson$Types.class, "getSupertype",
                    Type.class, context,
                    Class.class, contextRawType,
                    Class.class, Iterator.class);
            return ((ParameterizedType)type).getActualTypeArguments()[0];
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
                    throw new GsonUtilsException(e);
                }
            }

            @Override
            public V next() {
                try {
                    if (!hasNext())
                        throw new NoSuchElementException();
                    return elementTypeAdapter.read(in);
                } catch (IOException e) {
                    throw new GsonUtilsException(e);
                }
            }
        };
    }

}
