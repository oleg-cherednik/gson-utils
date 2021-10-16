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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.olegcherednik.gson.utils.GsonUtilsBuilder.ZONE_MODIFIER_USE_ORIGINAL;
import static ru.olegcherednik.gson.utils.utils.PrettyPrintUtils.UNIX_LINE_SEPARATOR;
import static ru.olegcherednik.gson.utils.utils.PrettyPrintUtils.withUnixLineSeparator;

/**
 * @author Oleg Cherednik
 * @since 09.10.2021
 */
@Test
public class DateGsonUtilsTest {

    public void shouldRetrieveJsonWhenWriteDate() throws IOException {
        Map<String, Date> map = createData();
        String actual = GsonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"date\":\"2021-10-09T04:16:59.225Z\"}");
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteDateMapWithPrettyPrint() {
        Map<String, Date> map = createData();
        String actual = GsonUtils.prettyPrint().writeValue(map);
        assertThat(withUnixLineSeparator(actual)).isEqualTo('{' + UNIX_LINE_SEPARATOR +
                "  \"date\": \"2021-10-09T04:16:59.225Z\"" + UNIX_LINE_SEPARATOR +
                '}');
    }

    public void shouldRetrieveDeserializedDateMapWhenReadJsonAsMap() {
        String json = "{\"date\":\"2021-10-09T04:16:59.225Z\"}";
        Map<String, Date> expected = createData();
        Map<String, Date> actual = GsonUtils.readMap(json, String.class, Date.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldReadNullableValueWhenListContainsNull() {
        String json = "[null,\"2021-10-09T04:16:59.225Z\"]";
        List<Date> actual = GsonUtils.readList(json, Date.class);

        assertThat(actual).hasSize(2);
        assertThat(actual.get(0)).isNull();
        assertThat(actual.get(1)).isEqualTo(createDate());
    }

    public void shouldWriteNullWhenSerializeWithNullValue() {
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder().serializeNulls());
        String json = gson.writeValue(new Data());
        assertThat(json).isEqualTo("{\"notNullValue\":\"2021-10-09T04:16:59.225Z\",\"nullValue\":null}");
    }

    public void shouldIgnoreNullValueWhenSerializeWithIgnoreNullValue() {
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder());
        String json = gson.writeValue(new Data());
        assertThat(json).isEqualTo("{\"notNullValue\":\"2021-10-09T04:16:59.225Z\"}");
    }

    public void shouldRetrieveJsonWithCustomFormatWriteSerializeWithCustomFormatter() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd");
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(
                new GsonUtilsBuilder().dateTimeFormatter(ZONE_MODIFIER_USE_ORIGINAL, dateTimeFormatter));
        String json = gson.writeValue(new Data());
        assertThat(json).isEqualTo("{\"notNullValue\":\"04:16:59 2021-10-09\"}");
    }

    /*
                    "Asia/Singapore", ZonedDateTime.parse(str, ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("Asia/Singapore"))),
                "Australia/Sydney", ZonedDateTime.parse(str, ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("Australia/Sydney"))));
     */

    public void shouldRetrieveJsonWhenUseCustomTimeZone() {

    }

    private static Map<String, Date> createData() {
        return MapUtils.of("date", createDate());
    }

    private static Date createDate() {
        return new Date(1633753019225L);
    }

    @SuppressWarnings("unused")
    private static class Data {

        private final Date notNullValue = createDate();
        private final Date nullValue = null;

    }

}
