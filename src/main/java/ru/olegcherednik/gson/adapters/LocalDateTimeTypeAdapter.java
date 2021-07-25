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

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 08.01.2021
 */
public class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {

    protected final DateTimeFormatter df;

    public LocalDateTimeTypeAdapter(DateTimeFormatter df) {
        this.df = df;
    }

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null)
            out.nullValue();
        else
            out.value(df.format(value));
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        LocalDateTime res = null;

        if (in.peek() == JsonToken.NULL)
            in.nextNull();
        else
            res = LocalDateTime.parse(in.nextString(), df);

        return res;
    }

}
