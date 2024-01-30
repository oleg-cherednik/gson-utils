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

package ru.olegcherednik.json.gson.types;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import org.testng.annotations.Test;
import ru.olegcherednik.json.api.JsonException;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Oleg Cherednik
 * @since 05.01.2024
 */
@Test
public class JsonReaderIteratorTest {

    @SuppressWarnings({ "unchecked", "AbbreviationAsWordInName", "PMD.CloseResource" })
    public void shouldThrowJsonExceptionWhenHasNextThrowIOException() throws IOException {
        JsonReader in = mock(JsonReader.class);
        TypeAdapter<Integer> typeAdapter = mock(TypeAdapter.class);

        when(in.hasNext()).thenThrow(IOException.class);

        JsonReaderIterator<Integer> it = new JsonReaderIterator<>(in, typeAdapter);
        assertThatThrownBy(it::hasNext).isExactlyInstanceOf(JsonException.class);
    }

    @SuppressWarnings({ "unchecked", "PMD.CloseResource" })
    public void shouldThrowNoSuchElementExceptionWhenNextForEmptyIterator() throws IOException {
        JsonReader in = mock(JsonReader.class);
        TypeAdapter<Integer> typeAdapter = mock(TypeAdapter.class);

        when(in.hasNext()).thenReturn(false);

        JsonReaderIterator<Integer> it = new JsonReaderIterator<>(in, typeAdapter);
        assertThatThrownBy(it::next).isExactlyInstanceOf(NoSuchElementException.class);
    }

    @SuppressWarnings({ "unchecked", "PMD.CloseResource" })
    public void shouldThrowJsonExceptionWhenNextAndExceptionOnRead() throws IOException {
        JsonReader in = mock(JsonReader.class);
        TypeAdapter<Integer> typeAdapter = mock(TypeAdapter.class);

        when(in.hasNext()).thenReturn(true);
        when(typeAdapter.read(same(in))).thenThrow(IOException.class);

        JsonReaderIterator<Integer> it = new JsonReaderIterator<>(in, typeAdapter);
        assertThatThrownBy(it::next).isExactlyInstanceOf(JsonException.class);
    }

}
