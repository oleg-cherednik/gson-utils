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
package ru.olegcherednik.gson.utils.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ru.olegcherednik.gson.utils.EnumId;
import ru.olegcherednik.gson.utils.GsonUtilsException;
import ru.olegcherednik.gson.utils.JsonCreator;
import ru.olegcherednik.utils.reflection.MethodUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Oleg Cherednik
 * @since 18.10.2021
 */
public class EnumIdTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>)type.getRawType();

        if (!EnumId.class.isAssignableFrom(rawType))
            return null;

        Function<String, T> read = createReadFunc(rawType);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                String id = value == null ? null : ((EnumId)value).getId();

                if (id == null)
                    out.nullValue();
                else
                    out.value(id);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                String id = null;

                if (in.peek() == JsonToken.NULL)
                    in.nextNull();
                else
                    id = in.nextString();

                return read.apply(id);
            }
        };

    }

    private static <T> Function<String, T> createReadFunc(Class<T> rawType) {
        List<Method> methods = getJsonCreateMethods(rawType);

        if (methods.size() > 1) {
            return id -> {
                throw new GsonUtilsException("Multiple methods with '" + JsonCreator.class.getSimpleName()
                        + "' annotation was found in '" + rawType.getSimpleName() + "' class");
            };
        }

        if (methods.size() == 1)
            return createFunc(methods.get(0));

        Method method = getParseIdMethod(rawType);

        if (method == null) {
            return id -> {
                throw new GsonUtilsException("Factory method for EnumIs '"
                        + rawType.getSimpleName() + "' was not found");
            };
        }

        return createFunc(method);
    }

    private static <T> Function<String, T> createFunc(Method method) {
        return id -> {
            try {
                return MethodUtils.invokeStaticMethod(method, id);
            } catch (Exception e) {
                throw new GsonUtilsException(e.getCause());
            }
        };
    }

    private static List<Method> getJsonCreateMethods(final Class<?> rawType) {
        List<Method> res = new ArrayList<>();
        Class<?> type = rawType;

        while (type != Object.class) {
            for (Method method : type.getDeclaredMethods())
                if (isValidFactoryMethod(method))
                    res.add(method);

            type = type.getSuperclass();
        }

        return res;
    }

    private static Method getParseIdMethod(Class<?> rawType) {
        while (rawType != Object.class) {
            try {
                return rawType.getDeclaredMethod("parseId", String.class);
            } catch (NoSuchMethodException ignore) {
                // ignore
            }

            rawType = rawType.getSuperclass();
        }

        return null;
    }

    private static boolean isValidFactoryMethod(Method method) {
        return Modifier.isStatic(method.getModifiers())
                && method.isAnnotationPresent(JsonCreator.class)
                && method.getParameterCount() == 1
                && method.getParameterTypes()[0] == String.class;
    }

}
