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

package ru.olegcherednik.json.gson.datetime;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

/**
 * @author Oleg Cherednik
 * @since 09.12.2023
 */
@RequiredArgsConstructor
public class OffsetTimeTypeAdapter extends TypeAdapter<OffsetTime> {

    private final DateTimeFormatter df;
    private final UnaryOperator<ZoneId> zoneModifier;

    @Override
    public void write(JsonWriter out, OffsetTime value) throws IOException {
        ZoneId zoneId = df.getZone() == null ? zoneModifier.apply(value.getOffset()) : df.getZone();
        ZoneOffset offset = zoneId.getRules().getOffset(Instant.now());
        value = value.withOffsetSameInstant(offset);
        out.value(df.withZone(zoneId).format(value));
    }

    @Override
    public OffsetTime read(JsonReader in) throws IOException {
        return OffsetTime.parse(in.nextString(), df);
    }

}
