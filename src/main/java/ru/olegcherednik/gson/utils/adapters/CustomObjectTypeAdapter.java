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
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ru.olegcherednik.gson.utils.GsonUtilsException;
import ru.olegcherednik.utils.reflection.FieldUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.NoSuchElementException;

import static com.google.gson.stream.JsonToken.NUMBER;

/**
 * @author Oleg Cherednik
 * @since 09.01.2021
 */
public class CustomObjectTypeAdapter extends TypeAdapter<Object> {

    protected final Gson gson;
    protected final TypeAdapter<Object> delegate;

    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            TypeAdapter<Object> typeAdapter = (TypeAdapter<Object>)getTypeAdapterFactory().<T>create(gson, type);
            return typeAdapter == null ? null : (TypeAdapter<T>)new CustomObjectTypeAdapter(gson, typeAdapter);
        }

        private TypeAdapterFactory getTypeAdapterFactory() {
            try {
                return FieldUtils.getStaticFieldValue(ObjectTypeAdapter.class, "FACTORY");
            } catch (NoSuchElementException e) {
                try {
                    return FieldUtils.getStaticFieldValue(ObjectTypeAdapter.class, "DOUBLE_FACTORY");
                } catch (Exception ee) {
                    throw new GsonUtilsException(ee);
                }
            } catch (Exception e) {
                throw new GsonUtilsException(e);
            }
        }
    };

    public CustomObjectTypeAdapter(Gson gson, TypeAdapter<Object> delegate) {
        this.gson = gson;
        this.delegate = delegate;
    }

    @Override
    public void write(JsonWriter out, Object value) throws IOException {
        delegate.write(out, value);
    }

    @Override
    public Object read(JsonReader in) throws IOException {
        JsonToken token = in.peek();

        if (token != NUMBER)
            return delegate.read(in);

        String str = in.nextString();
        double val = Double.parseDouble(str);

        if (Double.compare((int)val, val) == 0)
            return (int)val;
        if (Double.compare((long)val, val) == 0)
            return (long)val;

        try {
            return new BigInteger(str);
        } catch (NumberFormatException ignore) {
            // ignore
        }

        return val;
    }

}
