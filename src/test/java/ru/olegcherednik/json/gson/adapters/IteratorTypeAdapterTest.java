/*
 * Copyright Oleg Cherednik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.olegcherednik.json.gson.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Oleg Cherednik
 * @since 05.01.2024
 */
@Test
public class IteratorTypeAdapterTest {

    @SuppressWarnings({ "unchecked", "PMD.CloseResource" })
    public void shouldWriteNullWhenWriteNullValue() throws IOException {
        TypeAdapter<Object> elementTypeAdapter = mock(TypeAdapter.class);
        BiFunction<JsonReader, TypeAdapter<Object>, Iterator<?>> createIt = mock(BiFunction.class);
        IteratorTypeAdapter<Integer> typeAdapter = new IteratorTypeAdapter<>(elementTypeAdapter, createIt);

        JsonWriter out = mock(JsonWriter.class);
        typeAdapter.write(out, null);

        verify(out, times(1)).nullValue();
        verify(out, never()).beginArray();
        verify(out, never()).endArray();
        verify(elementTypeAdapter, never()).write(any(), any());
    }

    @SuppressWarnings({ "unchecked", "PMD.CloseResource" })
    public void shouldReadNullWhenReadJsonTokenNull() throws IOException {
        TypeAdapter<Object> elementTypeAdapter = mock(TypeAdapter.class);
        BiFunction<JsonReader, TypeAdapter<Object>, Iterator<?>> createIt = mock(BiFunction.class);
        IteratorTypeAdapter<Integer> typeAdapter = new IteratorTypeAdapter<>(elementTypeAdapter, createIt);

        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);

        Object actual = typeAdapter.read(in);
        assertThat(actual).isNull();

        verify(in, never()).beginArray();
        verify(createIt, never()).apply(any(), any());
    }

}
