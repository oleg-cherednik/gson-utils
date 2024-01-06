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
import lombok.NoArgsConstructor;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.gson.DynamicToNumberStrategy;
import ru.olegcherednik.json.gson.GsonEngine;
import ru.olegcherednik.json.gson.datetime.JavaTimeModule;
import ru.olegcherednik.json.gson.factories.EnumIdTypeAdapterFactory;
import ru.olegcherednik.json.gson.factories.IteratorTypeAdapterFactory;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author Oleg Cherednik
 * @since 06.01.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class GsonFactory {

    public static GsonEngine createJsonEngine(JsonSettings settings) {
        GsonBuilder builder = createGsonBuilder(settings);
        return new GsonEngine(builder.create());
    }

    public static GsonEngine createPrettyPrintJsonEngine(JsonSettings settings) {
        GsonBuilder builder = createGsonBuilder(settings).setPrettyPrinting();
        return new GsonEngine(builder.create());
    }

    private static GsonBuilder createGsonBuilder(JsonSettings settings) {
        Objects.requireNonNull(settings);

        GsonBuilder builder = new GsonBuilder()
                .setObjectToNumberStrategy(DynamicToNumberStrategy.INSTANCE)
                .enableComplexMapKeySerialization()
                .registerTypeAdapterFactory(EnumIdTypeAdapterFactory.INSTANCE)
                .registerTypeAdapterFactory(IteratorTypeAdapterFactory.INSTANCE);

        if (settings.isSerializeNull())
            builder.serializeNulls();

        createJavaTimeModuleConsumer(settings).accept(builder);
        return builder;
    }

    private static JavaTimeModule createJavaTimeModuleConsumer(JsonSettings settings) {
        return JavaTimeModule.builder()
                             .date(settings.getDateFormatter())
                             .instant(settings.getInstantFormatter())
                             .localDate(settings.getLocalDateFormatter())
                             .localTime(settings.getLocalTimeFormatter())
                             .localDateTime(settings.getLocalDateTimeFormatter())
                             .offsetTime(settings.getOffsetTimeFormatter())
                             .offsetDateTime(withZoneId(settings.getOffsetDateTimeFormatter(), settings.getZoneId()))
                             .offsetDateTime(withZoneId(settings.getOffsetDateTimeFormatter(), settings.getZoneId()))
                             .zonedDateTime(withZoneId(settings.getZonedDateTimeFormatter(), settings.getZoneId()))
                             .build();
    }

    private static DateTimeFormatter withZoneId(DateTimeFormatter df, ZoneId zoneId) {
        return zoneId == null ? df : df.withZone(zoneId);
    }

}
