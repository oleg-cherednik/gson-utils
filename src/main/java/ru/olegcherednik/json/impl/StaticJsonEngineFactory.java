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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.olegcherednik.json.api.JsonEngine;
import ru.olegcherednik.json.api.JsonEngineFactory;
import ru.olegcherednik.json.api.JsonSettings;

/**
 * @author Oleg Cherednik
 * @since 19.11.2023
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public final class StaticJsonEngineFactory implements JsonEngineFactory {

    private static final StaticJsonEngineFactory INSTANCE = new StaticJsonEngineFactory();

    public static StaticJsonEngineFactory getInstance() {
        return INSTANCE;
    }

    public static String getMainClass() {
        return "com.google.gson.Gson";
    }

    @Override
    public JsonEngine createJsonEngine(JsonSettings settings) {
        return GsonFactory.createJsonEngine(settings);
    }

    @Override
    public JsonEngine createPrettyPrintJsonEngine(JsonSettings settings) {
        return GsonFactory.createPrettyPrintJsonEngine(settings);
    }
}
