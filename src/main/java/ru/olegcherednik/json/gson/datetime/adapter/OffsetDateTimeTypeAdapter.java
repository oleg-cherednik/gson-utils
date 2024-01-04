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

package ru.olegcherednik.json.gson.datetime.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 17.10.2021
 */
@RequiredArgsConstructor
public class OffsetDateTimeTypeAdapter extends TypeAdapter<OffsetDateTime> {

    protected final DateTimeFormatter df;

    @Override
    public void write(JsonWriter out, OffsetDateTime value) throws IOException {
        if (df.getZone() != null) {
            ZoneOffset zoneOffset = df.getZone().getRules().getOffset(Instant.now());
            value = value.withOffsetSameInstant(zoneOffset);
        }

        out.value(df.format(value));
    }

    @Override
    public OffsetDateTime read(JsonReader in) throws IOException {
        return OffsetDateTime.parse(in.nextString(), df);
    }

}