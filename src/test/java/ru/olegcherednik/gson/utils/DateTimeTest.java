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
package ru.olegcherednik.gson.utils;

import com.google.gson.Gson;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.olegcherednik.gson.utils.GsonUtilsBuilder.ZONE_MODIFIER_USE_ORIGINAL;

/**
 * @author Oleg Cherednik
 * @since 16.10.2021
 */
@Test
public class DateTimeTest {

    private final Date date = new Date();
    private final Data data = new Data(date);

    public void shouldReverseConvertUsingDefaultSettings() {
        String json = GsonUtils.writeValue(data);
        String expected = ISO_ZONED_DATE_TIME.format(ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC));
        checkJson(json, expected);
        checkData(GsonUtils.readValue(json, Data.class));
    }

    public void shouldUseGivenZoneWhenConvertAllDateFormat() {
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder()
                .zonedModifier(ZONE_MODIFIER_USE_ORIGINAL));
        String json = gson.writeValue(data);
        String expected = ISO_OFFSET_DATE_TIME.format(ZonedDateTime.ofInstant(date.toInstant(),
                ZoneOffset.systemDefault()));
        checkJson(json, expected);
        checkData(gson.readValue(json, Data.class));
    }

    public void shouldUseGiveFormatWhenConvertAllDateFormat() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss.SSS yyyy-MM-dd");
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder().dateTimeFormatter(df));
        String json = gson.writeValue(data);
        String expected = df.format(ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC));
        checkJson(json, expected);
        checkData(gson.readValue(json, Data.class));
    }

    public void shouldUseGivenZOneAndFormatWhenConvertAllDateFormat() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss.SSS yyyy-MM-dd");
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder()
                .dateTimeFormatter(ZONE_MODIFIER_USE_ORIGINAL, df));
        String json = gson.writeValue(data);
        String expected = df.format(ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.systemDefault()));
        checkJson(json, expected);
        checkData(gson.readValue(json, Data.class));
    }

    private static void checkJson(String json, String expected) {
        Map<String, Object> actual = toMap(json);
        assertThat(actual.get("date")).isInstanceOf(String.class).isEqualTo(expected);
        assertThat(actual.get("instant")).isInstanceOf(String.class).isEqualTo(expected);
        assertThat(actual.get("localDateTime")).isInstanceOf(String.class).isEqualTo(expected);
        assertThat(actual.get("zonedDateTime")).isInstanceOf(String.class).isEqualTo(expected);
        assertThat(actual.get("offsetDateTime")).isInstanceOf(String.class).isEqualTo(expected);
    }

    private void checkData(Data actual) {
        assertThat(actual.date).isEqualTo(data.date);
        assertThat(actual.instant).isEqualTo(data.instant);
        assertThat(actual.localDateTime).isEqualTo(data.localDateTime);
        assertThat(actual.zonedDateTime).isEqualTo(data.zonedDateTime);
        assertThat(actual.offsetDateTime).isEqualTo(data.offsetDateTime);
    }

    private static Map<String, Object> toMap(String json) {
        return (Map<String, Object>)new Gson().fromJson(json, Map.class);
    }

    @SuppressWarnings({ "unused", "AssignmentOrReturnOfFieldWithMutableType", "FieldCanBeLocal" })
    private static class Data {

        private final Date date;
        private final Instant instant;
        private final LocalDateTime localDateTime;
        private final ZonedDateTime zonedDateTime;
        private final OffsetDateTime offsetDateTime;
//        private final LocalDate localDate = LocalDate.now();
//        private final LocalTime localTime = LocalTime.now();

        public Data(Date date) {
            this.date = date;
            instant = date.toInstant();
            localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
            offsetDateTime = OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
        }

    }

}
