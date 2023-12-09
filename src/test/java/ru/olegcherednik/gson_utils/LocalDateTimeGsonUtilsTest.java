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
package ru.olegcherednik.gson_utils;

import org.testng.annotations.Test;
import ru.olegcherednik.json.gson.utils.MapUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 08.01.2021
 */
@Test
public class LocalDateTimeGsonUtilsTest {

    private final LocalDateTime localDateTime = LocalDateTime.now();
    private final String str = ISO_ZONED_DATE_TIME.format(localDateTime.atZone(ZoneId.systemDefault())
                                                                       .withZoneSameInstant(ZoneOffset.UTC));
    private final Map<String, LocalDateTime> expected = MapUtils.of("local", localDateTime);
    private final Data data = new Data(localDateTime);

    public void shouldRetrieveJsonWhenWriteLocalDateTime() throws IOException {
//        String actual = GsonUtils.writeValue(expected);
//        assertThat(actual).isNotNull();
//        assertThat(actual).isEqualTo("{\"local\":\"" + str + "\"}");
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteLocalDateTimeMapWithPrettyPrint() {
//        String actual = GsonUtils.prettyPrint().writeValue(expected);
//        assertThat(withUnixLineSeparator(actual)).isEqualTo('{' + UNIX_LINE_SEPARATOR +
//                "  \"local\": \"" + str + '"' + UNIX_LINE_SEPARATOR +
//                '}');
    }

    public void shouldRetrieveDeserializedZonedLocalDateTimeMapWhenReadJsonAsMap() {
//        String json = "{\"local\":\"" + str + "\"}";
//        Map<String, LocalDateTime> actual = GsonUtils.readMap(json, String.class, LocalDateTime.class);
//        assertThat(actual).isNotNull();
//        assertThat(actual).isEqualTo(expected);
    }

    public void shouldReadNullableValueWhenListContainsNull() {
//        String json = "[null,\"" + str + "\"]";
//        List<LocalDateTime> actual = GsonUtils.readList(json, LocalDateTime.class);
//
//        assertThat(actual).hasSize(2);
//        assertThat(actual.get(0)).isNull();
//        assertThat(actual.get(1)).isEqualTo(localDateTime);
    }

    public void shouldWriteNullWhenSerializeWithNullValue() {
//        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder().serializeNulls());
//        String json = gson.writeValue(data);
//        assertThat(json).isEqualTo("{\"notNullValue\":\"" + str + "\",\"nullValue\":null}");
    }

    public void shouldIgnoreNullValueWhenSerializeWithIgnoreNullValue() {
//        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder());
//        String json = gson.writeValue(data);
//        assertThat(json).isEqualTo("{\"notNullValue\":\"" + str + "\"}");
    }

//    public void shouldRetrieveJsonWithCustomFormatWriteSerializeWithCustomFormatter() throws IOException {
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd");
//        String str = dateTimeFormatter.format(localDateTime.atZone(ZoneId.systemDefault()));
//        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder()
//                .localDateTimeFormatter(ZONE_MODIFIER_USE_ORIGINAL, dateTimeFormatter));
//        String json = gson.writeValue(data);
//        assertThat(json).isEqualTo("{\"notNullValue\":\"" + str + "\"}");
//    }

    @SuppressWarnings({ "unused", "FieldCanBeLocal" })
    private static class Data {

        private final LocalDateTime notNullValue;
        private final LocalDateTime nullValue = null;

        public Data(LocalDateTime notNullValue) {
            this.notNullValue = notNullValue;
        }

    }

}
