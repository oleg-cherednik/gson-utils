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
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.olegcherednik.gson.utils.utils.PrettyPrintUtils.UNIX_LINE_SEPARATOR;
import static ru.olegcherednik.gson.utils.utils.PrettyPrintUtils.withUnixLineSeparator;

/**
 * @author Oleg Cherednik
 * @since 09.10.2021
 */
@Test
public class DateGsonUtilsTest {

    private final Date date = new Date();
    private final String str = ISO_ZONED_DATE_TIME.format(ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC));
    private final Map<String, Date> expected = MapUtils.of("date", date);
    private final Data data = new Data(date);

    public void shouldRetrieveJsonWhenWriteDate() throws IOException {
        String actual = GsonUtils.writeValue(expected);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"date\":\"" + str + "\"}");
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteDateMapWithPrettyPrint() {
        String actual = GsonUtils.prettyPrint().writeValue(expected);
        assertThat(withUnixLineSeparator(actual)).isEqualTo('{' + UNIX_LINE_SEPARATOR +
                "  \"date\": \"" + str + '"' + UNIX_LINE_SEPARATOR +
                '}');
    }

    public void shouldRetrieveDeserializedDateMapWhenReadJsonAsMap() {
        String json = "{\"date\":\"" + str + "\"}";
        Map<String, Date> actual = GsonUtils.readMap(json, String.class, Date.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldReadNullableValueWhenListContainsNull() {
        String json = "[null,\"" + str + "\"]";
        List<Date> actual = GsonUtils.readList(json, Date.class);

        assertThat(actual).hasSize(2);
        assertThat(actual.get(0)).isNull();
        assertThat(actual.get(1)).isEqualTo(date);
    }

    public void shouldWriteNullWhenSerializeWithNullValue() {
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder().serializeNulls());
        String json = gson.writeValue(data);
        assertThat(json).isEqualTo("{\"notNullValue\":\"" + str + "\",\"nullValue\":null}");
    }

    public void shouldIgnoreNullValueWhenSerializeWithIgnoreNullValue() {
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder());
        String json = gson.writeValue(data);
        assertThat(json).isEqualTo("{\"notNullValue\":\"" + str + "\"}");
    }

//    public void shouldRetrieveJsonWithCustomFormatWriteSerializeWithCustomFormatter() throws IOException {
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd");
//        String str = df.format(ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
//        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(
//                new GsonUtilsBuilder().dateFormatter(ZONE_MODIFIER_USE_ORIGINAL, df));
//        String json = gson.writeValue(data);
//        assertThat(json).isEqualTo("{\"notNullValue\":\"" + str + "\"}");
//    }

    @SuppressWarnings({ "FieldCanBeLocal", "AssignmentOrReturnOfFieldWithMutableType", "unused" })
    private static class Data {

        private final Date notNullValue;
        private final Date nullValue = null;

        public Data(Date notNullValue) {
            this.notNullValue = notNullValue;
        }

    }

}
