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
