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

import com.google.gson.GsonBuilder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import ru.olegcherednik.json.gson.adapters.AutoCloseableIteratorTypeAdapter;
import ru.olegcherednik.json.gson.adapters.EnumIdTypeAdapterFactory;
import ru.olegcherednik.json.gson.datetime.adapter.DateTypeAdapter;
import ru.olegcherednik.json.gson.datetime.adapter.InstantTypeAdapter;
import ru.olegcherednik.json.gson.datetime.adapter.LocalDateTimeTypeAdapter;
import ru.olegcherednik.json.gson.datetime.adapter.LocalDateTypeAdapter;
import ru.olegcherednik.json.gson.datetime.adapter.LocalTimeTypeAdapter;
import ru.olegcherednik.json.gson.datetime.adapter.OffsetDateTimeTypeAdapter;
import ru.olegcherednik.json.gson.datetime.adapter.OffsetTimeTypeAdapter;
import ru.olegcherednik.json.gson.datetime.adapter.ZonedDateTimeTypeAdapter;

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Consumer;

/**
 * @author Oleg Cherednik
 * @since 03.01.2024
 */
@Builder
@RequiredArgsConstructor
public class JavaTimeModule implements Consumer<GsonBuilder> {

    protected final ZoneId zoneId;
    protected final DateTimeFormatter instant;
    protected final DateTimeFormatter localDate;
    protected final DateTimeFormatter localTime;
    protected final DateTimeFormatter localDateTime;
    protected final DateTimeFormatter offsetTime;
    protected final DateTimeFormatter offsetDateTime;
    protected final DateTimeFormatter zonedDateTime;
    protected final DateFormat date;

    @Override
    public void accept(GsonBuilder builder) {
        InstantTypeAdapter instant = new InstantTypeAdapter(this.instant);
        LocalDateTypeAdapter localDate = new LocalDateTypeAdapter(this.localDate);
        LocalTimeTypeAdapter localTime = new LocalTimeTypeAdapter(this.localTime);
        LocalDateTimeTypeAdapter localDateTime = new LocalDateTimeTypeAdapter(this.localDateTime);
        OffsetTimeTypeAdapter offsetTime = new OffsetTimeTypeAdapter(withZoneId(this.offsetTime));
        OffsetDateTimeTypeAdapter offsetDateTime = new OffsetDateTimeTypeAdapter(withZoneId(this.offsetDateTime));
        ZonedDateTimeTypeAdapter zonedDateTime = new ZonedDateTimeTypeAdapter(withZoneId(this.zonedDateTime));
        DateTypeAdapter date = new DateTypeAdapter(this.date);

        Consumer<GsonBuilder> customizer = ((Consumer<GsonBuilder>) GsonBuilder::enableComplexMapKeySerialization)
                .andThen(b -> b.registerTypeAdapterFactory(AutoCloseableIteratorTypeAdapter.INSTANCE))
                .andThen(b -> b.registerTypeAdapterFactory(EnumIdTypeAdapterFactory.INSTANCE))
                .andThen(b -> b.registerTypeAdapter(Instant.class, instant.nullSafe()))
                .andThen(b -> b.registerTypeAdapter(LocalTime.class, localTime.nullSafe()))
                .andThen(b -> b.registerTypeAdapter(LocalDate.class, localDate.nullSafe()))
                .andThen(b -> b.registerTypeAdapter(LocalDateTime.class, localDateTime.nullSafe()))
                .andThen(b -> b.registerTypeAdapter(OffsetTime.class, offsetTime.nullSafe()))
                .andThen(b -> b.registerTypeAdapter(OffsetDateTime.class, offsetDateTime.nullSafe()))
                .andThen(b -> b.registerTypeAdapter(ZonedDateTime.class, zonedDateTime.nullSafe()))
                .andThen(b -> b.registerTypeAdapter(Date.class, date.nullSafe()));

        customizer.accept(builder);
    }

    protected DateTimeFormatter withZoneId(DateTimeFormatter df) {
        return zoneId == null ? df : df.withZone(zoneId);
    }

}
