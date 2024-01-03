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

package ru.olegcherednik.json.impl;

import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.olegcherednik.json.api.JsonEngineFactory;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.gson.DynamicToNumberStrategy;
import ru.olegcherednik.json.gson.GsonEngine;
import ru.olegcherednik.json.gson.adapters.AutoCloseableIteratorTypeAdapter;
import ru.olegcherednik.json.gson.adapters.EnumIdTypeAdapterFactory;
import ru.olegcherednik.json.gson.datetime.DateTypeAdapter;
import ru.olegcherednik.json.gson.datetime.InstantTypeAdapter;
import ru.olegcherednik.json.gson.datetime.LocalDateTimeTypeAdapter;
import ru.olegcherednik.json.gson.datetime.LocalDateTypeAdapter;
import ru.olegcherednik.json.gson.datetime.LocalTimeTypeAdapter;
import ru.olegcherednik.json.gson.datetime.OffsetDateTimeTypeAdapter;
import ru.olegcherednik.json.gson.datetime.OffsetTimeTypeAdapter;
import ru.olegcherednik.json.gson.datetime.ZonedDateTimeTypeAdapter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * @author Oleg Cherednik
 * @since 19.11.2023
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StaticJsonEngineFactory implements JsonEngineFactory {

    private static final StaticJsonEngineFactory INSTANCE = new StaticJsonEngineFactory();

    @SuppressWarnings("unused")
    public static StaticJsonEngineFactory getInstance() {
        return INSTANCE;
    }

    // ---------- JsonEngineFactory ----------

    @Override
    public GsonEngine createJsonEngine(JsonSettings settings) {
        GsonBuilder builder = createGsonBuilder(settings);
        return new GsonEngine(builder.create());
    }

    @Override
    public GsonEngine createPrettyPrintJsonEngine(JsonSettings settings) {
        GsonBuilder builder = createGsonBuilder(settings).setPrettyPrinting();
        return new GsonEngine(builder.create());
    }

    // ---------- supplier ----------

    private static GsonBuilder createGsonBuilder(JsonSettings settings) {
        Objects.requireNonNull(settings);

        GsonBuilder builder = new GsonBuilder().setObjectToNumberStrategy(DynamicToNumberStrategy.INSTANCE);

        if (settings.isSerializeNull())
            builder.serializeNulls();

        UnaryOperator<ZoneId> zoneModifier = settings.getZoneId() == null ? zoneId -> zoneId
                                                                          : zoneId -> settings.getZoneId();

        InstantTypeAdapter instant = new InstantTypeAdapter(settings.getInstantFormatter(), zoneModifier);
        LocalDateTypeAdapter localDate = new LocalDateTypeAdapter(settings.getLocalDateFormatter());
        LocalTimeTypeAdapter localTime = new LocalTimeTypeAdapter(settings.getLocalTimeFormatter());
        LocalDateTimeTypeAdapter localDateTime = new LocalDateTimeTypeAdapter(settings.getLocalDateTimeFormatter());
        OffsetTimeTypeAdapter offsetTime = new OffsetTimeTypeAdapter(settings.getOffsetTimeFormatter(),
                                                                     zoneModifier);
        OffsetDateTimeTypeAdapter offsetDateTime = new OffsetDateTimeTypeAdapter(settings.getOffsetDateTimeFormatter(),
                                                                                 zoneModifier);
        ZonedDateTimeTypeAdapter zonedDateTime = new ZonedDateTimeTypeAdapter(settings.getZonedDateTimeFormatter(),
                                                                              zoneModifier);
        DateTypeAdapter date = new DateTypeAdapter(settings.getDateFormatter());

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
        return builder;
    }


}
