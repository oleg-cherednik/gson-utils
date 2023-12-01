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
package ru.olegcherednik.gson.utils.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.olegcherednik.gson.utils.utils.ListUtils;
import ru.olegcherednik.json.api.JsonException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Oleg Cherednik
 * @since 17.01.2021
 */
@Test
public class IteratorTypeAdapterTest {

    private TypeAdapter<String> elementTypeAdapter;
    private IteratorTypeAdapter<String> adapter;

    @BeforeMethod
    public void init() {
        elementTypeAdapter = (TypeAdapter<String>) mock(TypeAdapter.class);
        adapter = new IteratorTypeAdapter<>(elementTypeAdapter);
    }

    public void shouldRetrieveNullWhenReaderHasNullToken() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);

        Iterator<String> it = adapter.read(in);
        assertThat(it).isNull();
    }

    public void shouldThrowJsonExceptionWhenReadAndHasNextThrowsException() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.hasNext()).thenThrow(new IOException());

        assertThatThrownBy(() -> adapter.read(in).hasNext())
                .isExactlyInstanceOf(JsonException.class)
                .hasCauseInstanceOf(IOException.class);
    }

    public void shouldThrowJsonExceptionWhenReadAndNextThrowsException() throws IOException {
        JsonReader in = mock(JsonReader.class);

        when(in.hasNext()).thenReturn(true);
        when(elementTypeAdapter.read(same(in))).thenThrow(new IOException());

        assertThatThrownBy(() -> adapter.read(in).next())
                .isExactlyInstanceOf(JsonException.class)
                .hasCauseInstanceOf(IOException.class);
    }

    public void shouldWriteNullValueWhenIteratorNull() throws IOException {
        JsonWriter out = mock(JsonWriter.class);
        adapter.write(out, null);
        verify(out).nullValue();
    }

    public void shouldWriteNullJsonWhenWriteNullToStream() throws IOException {
        try (Writer writer = new StringWriter();
             JsonWriter out = new JsonWriter(writer)) {
            ArgumentCaptor<JsonWriter> outCaptor = ArgumentCaptor.forClass(JsonWriter.class);
            ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

            List<String> data = ListUtils.of("one", null, "two");
            adapter.write(out, data.iterator());

            verify(elementTypeAdapter, times(3)).write(outCaptor.capture(), valueCaptor.capture());

            List<JsonWriter> outKeys = outCaptor.getAllValues();
            List<String> valueKeys = valueCaptor.getAllValues();

            assertThat(outKeys).hasSize(3);
            assertThat(outKeys.get(0)).isSameAs(out);
            assertThat(outKeys.get(1)).isSameAs(out);
            assertThat(outKeys.get(2)).isSameAs(out);

            assertThat(valueKeys).hasSize(3);
            assertThat(valueKeys.get(0)).isEqualTo("one");
            assertThat(valueKeys.get(1)).isNull();
            assertThat(valueKeys.get(2)).isEqualTo("two");
        }
    }

    public void shouldThrowJsonExceptionWhenFactoryThrowsException() {
        assertThatThrownBy(() -> IteratorTypeAdapter.INSTANCE.create(null, null))
                .isExactlyInstanceOf(JsonException.class)
                .hasCauseInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings({ "unused", "FieldMayBeStatic" })
    private static class Data {

        private final String notNullValue = "oleg.cherednik";
        private final ZonedDateTime nullValue = null;

    }

}
