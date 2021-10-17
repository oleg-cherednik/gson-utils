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

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

/**
 * @author Oleg Cherednik
 * @since 17.10.2021
 */
public class OffsetDateTimeTypeAdapter extends TypeAdapter<OffsetDateTime> {

    protected final UnaryOperator<ZoneId> zoneModifier;
    protected final DateTimeFormatter df;

    public OffsetDateTimeTypeAdapter(UnaryOperator<ZoneId> zoneModifier, DateTimeFormatter df) {
        this.zoneModifier = zoneModifier;
        this.df = df;
    }

    @Override
    public void write(JsonWriter out, OffsetDateTime value) throws IOException {
        if (value == null)
            out.nullValue();
        else {
            ZoneId zone = zoneModifier.apply(ZoneId.systemDefault());
            ZonedDateTime zonedDateTime = value.toZonedDateTime();
            out.value(df.format(zonedDateTime.withZoneSameInstant(zone)));
        }
    }

    @Override
    public OffsetDateTime read(JsonReader in) throws IOException {
        OffsetDateTime res = null;

        if (in.peek() == JsonToken.NULL)
            in.nextNull();
        else {
            ZoneId zone = zoneModifier.apply(ZoneId.systemDefault());
            DateTimeFormatter dateTimeFormatter = df.getZone() == null ? df.withZone(zone) : df;
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(in.nextString(), dateTimeFormatter);
            res = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toOffsetDateTime();
        }

        return res;
    }

}
