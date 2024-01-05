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

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Iterator;
import java.util.function.BiFunction;

/**
 * @param <T> Type of the value object
 * @author Oleg Cherednik
 * @since 05.01.2024
 */
@RequiredArgsConstructor
public class IteratorTypeAdapter<T> extends TypeAdapter<T> {

    protected final TypeAdapter<Object> elementTypeAdapter;
    protected final BiFunction<JsonReader, TypeAdapter<Object>, Iterator<?>> createIt;

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginArray();
        Iterator<?> it = (Iterator<?>) value;

        while (it.hasNext()) {
            elementTypeAdapter.write(out, it.next());
        }

        out.endArray();
    }

    @Override
    public T read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        in.beginArray();
        return (T) createIt.apply(in, elementTypeAdapter);
    }

}
