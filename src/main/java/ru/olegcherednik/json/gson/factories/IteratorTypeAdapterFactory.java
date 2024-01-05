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
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.olegcherednik.json.api.iterator.AutoCloseableIterator;
import ru.olegcherednik.json.gson.adapters.IteratorTypeAdapter;
import ru.olegcherednik.json.gson.types.JsonReaderAutoCloseableIterator;
import ru.olegcherednik.json.gson.types.JsonReaderIterator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.function.BiFunction;

/**
 * @author Oleg Cherednik
 * @since 04.01.2024
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IteratorTypeAdapterFactory implements TypeAdapterFactory {

    public static final IteratorTypeAdapterFactory INSTANCE = new IteratorTypeAdapterFactory();

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        BiFunction<JsonReader, TypeAdapter<Object>, Iterator<?>> createId = getCreateIt(typeToken.getRawType());

        if (createId == null)
            return null;

        Type elementType = getElementType(typeToken.getType());
        TypeAdapter<Object> elementTypeAdapter = (TypeAdapter<Object>) gson.getAdapter(TypeToken.get(elementType));
        return new IteratorTypeAdapter<>(elementTypeAdapter, createId);
    }

    protected BiFunction<JsonReader, TypeAdapter<Object>, Iterator<?>> getCreateIt(Class<?> rawType) {
        if (AutoCloseableIterator.class.isAssignableFrom(rawType))
            return JsonReaderAutoCloseableIterator::new;
        if (Iterator.class.isAssignableFrom(rawType))
            return JsonReaderIterator::new;
        return null;
    }

    protected static Type getElementType(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();

            if (actualTypeArguments.length > 0)
                return actualTypeArguments[0];
        }

        return Object.class;
    }

}
