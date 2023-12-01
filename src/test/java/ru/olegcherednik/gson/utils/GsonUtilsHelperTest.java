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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import ru.olegcherednik.gson.utils.utils.MapUtils;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.olegcherednik.gson.utils.utils.PrettyPrintUtils.UNIX_LINE_SEPARATOR;
import static ru.olegcherednik.gson.utils.utils.PrettyPrintUtils.withUnixLineSeparator;

/**
 * @author Oleg Cherednik
 * @since 11.01.2021
 */
@Test
public class GsonUtilsHelperTest {

    @AfterMethod
    public void clear() {
        GsonUtilsHelper.setGsonBuilder(null);
    }

//    public void shouldUseNewBuilderWhenSetNotNullBuilderToGsonHelper() {
//        Map<String, ZonedDateTime> map = createData();
//        assertThat(GsonUtils.writeValue(map)).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\"}");
//        assertThat(withUnixLineSeparator(GsonUtils.prettyPrint().writeValue(map))).isEqualTo('{' + UNIX_LINE_SEPARATOR +
//                "  \"UTC\": \"2017-07-23T13:57:14.225Z\"" + UNIX_LINE_SEPARATOR + '}');
//
//        GsonUtilsHelper.setGsonBuilder(new GsonUtilsBuilder().zonedDateTimeFormatter(zone ->
//                ZoneId.of("Asia/Singapore"), ISO_ZONED_DATE_TIME));
//        assertThat(GsonUtils.writeValue(map)).isEqualTo("{\"UTC\":\"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\"}");
//        assertThat(withUnixLineSeparator(GsonUtils.prettyPrint().writeValue(map))).isEqualTo('{' + UNIX_LINE_SEPARATOR +
//                "  \"UTC\": \"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\"" + UNIX_LINE_SEPARATOR + '}');
//    }

    public void shouldNotRebuildMapperWhenSetSameBuilder() {
        Gson expectedGson = GsonUtilsHelper.gson();
        Gson expectedPrettyPrintGson = GsonUtilsHelper.prettyPrintGson();

        GsonUtilsHelper.setGsonBuilder(GsonUtilsHelper.DEFAULT_BUILDER);
        assertThat(GsonUtilsHelper.gson()).isSameAs(expectedGson);
        assertThat(GsonUtilsHelper.prettyPrintGson()).isSameAs(expectedPrettyPrintGson);
    }

    public void shouldCreateDecorator() {
        assertThat(GsonUtilsHelper.createGsonDecorator()).isNotNull();
        assertThat(GsonUtilsHelper.createPrettyPrintGsonDecorator()).isNotNull();
    }

    private static Map<String, ZonedDateTime> createData() {
        String str = "2017-07-23T13:57:14.225";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return MapUtils.of("UTC", ZonedDateTime.parse(str, df.withZone(ZoneOffset.UTC)));
    }


}
