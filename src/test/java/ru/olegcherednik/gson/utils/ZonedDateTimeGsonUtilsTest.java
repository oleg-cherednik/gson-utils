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

import org.testng.annotations.Test;
import ru.olegcherednik.gson.utils.utils.MapUtils;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.olegcherednik.gson.utils.GsonUtilsBuilder.ZONE_MODIFIER_USE_ORIGINAL;

/**
 * @author Oleg Cherednik
 * @since 08.01.2021
 */
@Test
public class ZonedDateTimeGsonUtilsTest {

    public void shouldRetrieveJsonUTCZoneWhenWriteZonedDateTimeDefaultSettings() {
        Map<String, ZonedDateTime> map = createData();
        String actual = GsonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\"," +
                "\"Asia/Singapore\":\"2017-07-23T05:57:14.225Z\"," +
                "\"Australia/Sydney\":\"2017-07-23T03:57:14.225Z\"}");
    }

    public void shouldRetrieveJsonSingaporeZoneWhenWriteZonedDateTimeSingaporeZone() throws IOException {
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(
                new GsonUtilsBuilder().zonedDateTimeFormatter(zone -> ZoneId.of("Asia/Singapore"), ISO_ZONED_DATE_TIME));

        Map<String, ZonedDateTime> map = createData();
        String actual = gson.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Australia/Sydney\":\"2017-07-23T11:57:14.225+08:00[Asia/Singapore]\"}");
    }

    public void shouldRetrieveJsonWithNoZoneChangeWhenWriteZonedDateTimeWithSameZone() throws IOException {
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(
                new GsonUtilsBuilder().zonedDateTimeFormatter(ZONE_MODIFIER_USE_ORIGINAL, ISO_ZONED_DATE_TIME));

        Map<String, ZonedDateTime> map = createData();
        String actual = gson.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\"," +
                "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Australia/Sydney\":\"2017-07-23T13:57:14.225+10:00[Australia/Sydney]\"}");
    }

    public void shouldRetrieveDeserializedZonedDateTimeMapWhenReadJsonAsMap() {
        String json = "{\"UTC\":\"2017-07-23T13:57:14.225Z\"," +
                "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Australia/Sydney\":\"2017-07-23T13:57:14.225+10:00[Australia/Sydney]\"}";
        Map<String, ZonedDateTime> expected = createData();
        Map<String, ZonedDateTime> actual = GsonUtils.readMap(json, String.class, ZonedDateTime.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public static Map<String, ZonedDateTime> createData() {
        String str = "2017-07-23T13:57:14.225";

        return MapUtils.of(
                "UTC", ZonedDateTime.parse(str, ISO_LOCAL_DATE_TIME.withZone(ZoneOffset.UTC)),
                "Asia/Singapore", ZonedDateTime.parse(str, ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("Asia/Singapore"))),
                "Australia/Sydney", ZonedDateTime.parse(str, ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("Australia/Sydney"))));
    }

    public void shouldReadNullableValueWhenListContainsNull() {
        String json = "[null,\"2017-07-23T13:57:14.225Z\"]";
        List<ZonedDateTime> actual = GsonUtils.readList(json, ZonedDateTime.class);

        assertThat(actual).hasSize(2);
        assertThat(actual.get(0)).isNull();
        assertThat(actual.get(1)).isEqualTo(ZonedDateTime.parse("2017-07-23T13:57:14.225", ISO_LOCAL_DATE_TIME.withZone(ZoneOffset.UTC)));
    }

    public void shouldWriteNullWhenSerializeWithNullValue() {
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder().serializeNulls());
        String json = gson.writeValue(new Data());
        assertThat(json).isEqualTo("{\"notNullValue\":\"2017-07-23T13:57:14.225Z\",\"nullValue\":null}");
    }

    public void shouldIgnoreNullValueWhenSerializeWithIgnoreNullValue() {
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder());
        String json = gson.writeValue(new Data());
        assertThat(json).isEqualTo("{\"notNullValue\":\"2017-07-23T13:57:14.225Z\"}");
    }

    public void shouldUseCustomDateTimeFormatterWhenWriteZonedDateTime() {
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder()
                .zonedDateTimeFormatter(ZONE_MODIFIER_USE_ORIGINAL, DateTimeFormatter.ofPattern("HH:mm:ss'T'dd.MM.yyyy")));
        String json = gson.writeValue(new Data());
        assertThat(json).isEqualTo("{\"notNullValue\":\"13:57:14T23.07.2017\"}");
    }

    @SuppressWarnings("unused")
    private static class Data {

        private final ZonedDateTime notNullValue = ZonedDateTime.parse("2017-07-23T13:57:14.225", ISO_LOCAL_DATE_TIME.withZone(ZoneOffset.UTC));
        private final ZonedDateTime nullValue = null;
    }

}
