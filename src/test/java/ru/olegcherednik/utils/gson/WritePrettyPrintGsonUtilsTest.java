/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ru.olegcherednik.utils.gson;

import org.testng.annotations.Test;
import ru.olegcherednik.utils.gson.data.Data;
import ru.olegcherednik.utils.gson.utils.ListUtils;
import ru.olegcherednik.utils.gson.utils.MapUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.olegcherednik.utils.gson.utils.PrettyPrintUtils.UNIX_LINE_SEPARATOR;
import static ru.olegcherednik.utils.gson.utils.PrettyPrintUtils.withUnixLineSeparator;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
@Test
public class WritePrettyPrintGsonUtilsTest {

    public void shouldRetrievePrettyPrintJsonWhenWriteObjectWithPrettyPrint() {
        Data data = new Data(555, "victory");
        String actual = GsonUtils.prettyPrint().writeValue(data);
        assertThat(actual).isNotNull();
        assertThat(withUnixLineSeparator(actual)).isEqualTo('{' + UNIX_LINE_SEPARATOR +
                "  \"intVal\": 555," + UNIX_LINE_SEPARATOR +
                "  \"strVal\": \"victory\"" + UNIX_LINE_SEPARATOR +
                '}');
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteMapObjectWithPrettyPrint() {
        Map<String, Data> data = MapUtils.of(
                "victory", new Data(555, "victory"),
                "omen", new Data(666, "omen"));
        String actual = GsonUtils.prettyPrint().writeValue(data);
        assertThat(withUnixLineSeparator(actual)).isEqualTo('{' + UNIX_LINE_SEPARATOR +
                "  \"victory\": {" + UNIX_LINE_SEPARATOR +
                "    \"intVal\": 555," + UNIX_LINE_SEPARATOR +
                "    \"strVal\": \"victory\"" + UNIX_LINE_SEPARATOR +
                "  }," + UNIX_LINE_SEPARATOR +
                "  \"omen\": {" + UNIX_LINE_SEPARATOR +
                "    \"intVal\": 666," + UNIX_LINE_SEPARATOR +
                "    \"strVal\": \"omen\"" + UNIX_LINE_SEPARATOR +
                "  }" + UNIX_LINE_SEPARATOR +
                '}');
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteListObjectWithPrettyPrint() {
        List<Data> data = ListUtils.of(new Data(555, "victory"), new Data(666, "omen"));
        String actual = GsonUtils.prettyPrint().writeValue(data);
        assertThat(actual).isNotNull();
        assertThat(withUnixLineSeparator(actual)).isEqualTo('[' + UNIX_LINE_SEPARATOR +
                "  {" + UNIX_LINE_SEPARATOR +
                "    \"intVal\": 555," + UNIX_LINE_SEPARATOR +
                "    \"strVal\": \"victory\"" + UNIX_LINE_SEPARATOR +
                "  }," + UNIX_LINE_SEPARATOR +
                "  {" + UNIX_LINE_SEPARATOR +
                "    \"intVal\": 666," + UNIX_LINE_SEPARATOR +
                "    \"strVal\": \"omen\"" + UNIX_LINE_SEPARATOR +
                "  }" + UNIX_LINE_SEPARATOR +
                ']');

    }

    public void shouldWritePrettyPrintJsonToStreamWhenWriteObjectWithPrettyPrintToStream() throws IOException {
        try (Writer out = new StringWriter()) {
            Data data = new Data(666, "omen");
            GsonUtils.prettyPrint().writeValue(data, out);
            assertThat(withUnixLineSeparator(out.toString())).isEqualTo('{' + UNIX_LINE_SEPARATOR +
                    "  \"intVal\": 666," + UNIX_LINE_SEPARATOR +
                    "  \"strVal\": \"omen\"" + UNIX_LINE_SEPARATOR +
                    '}');
        }
    }

    public void shouldWriteNullJsonWhenWriteNullWithPrettyPrintToStream() throws IOException {
        try (Writer out = new StringWriter()) {
            GsonUtils.prettyPrint().writeValue(null, out);
            assertThat(out.toString()).isEqualTo("null");
        }
    }

}
