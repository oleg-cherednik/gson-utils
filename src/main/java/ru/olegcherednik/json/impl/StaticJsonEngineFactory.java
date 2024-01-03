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
import ru.olegcherednik.json.gson.datetime.JavaTimeModule;

import java.util.Objects;

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

    private static GsonBuilder createGsonBuilder(JsonSettings settings) {
        Objects.requireNonNull(settings);

        GsonBuilder builder = new GsonBuilder().setObjectToNumberStrategy(DynamicToNumberStrategy.INSTANCE);

        if (settings.isSerializeNull())
            builder.serializeNulls();

        createJavaTimeModule(settings).accept(builder);
        return builder;
    }

    private static JavaTimeModule createJavaTimeModule(JsonSettings settings) {
        return JavaTimeModule.builder()
                             .zoneId(settings.getZoneId())
                             .date(settings.getDateFormatter())
                             .instant(settings.getInstantFormatter())
                             .localDate(settings.getLocalDateFormatter())
                             .localTime(settings.getLocalTimeFormatter())
                             .localDateTime(settings.getLocalDateTimeFormatter())
                             .offsetTime(settings.getOffsetTimeFormatter())
                             .offsetDateTime(settings.getOffsetDateTimeFormatter())
                             .offsetDateTime(settings.getOffsetDateTimeFormatter())
                             .zonedDateTime(settings.getZonedDateTimeFormatter())
                             .build();
    }

}
