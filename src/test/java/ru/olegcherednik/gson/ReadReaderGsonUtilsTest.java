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
package ru.olegcherednik.gson;

import org.testng.annotations.Test;
import ru.olegcherednik.gson.data.Book;
import ru.olegcherednik.gson.utils.ListUtils;
import ru.olegcherednik.gson.utils.MapUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Oleg Cherednik
 * @since 08.01.2021
 */
@Test
public class ReadReaderGsonUtilsTest {

    public void shouldRetrieveNullWhenObjectNull() {
        assertThat(GsonUtils.readValue((Reader)null, Object.class)).isNull();
        assertThat(GsonUtils.readList((Reader)null, Object.class)).isNull();
        assertThat(GsonUtils.readListLazy(null, Object.class)).isNull();
        assertThat(GsonUtils.readMap((Reader)null)).isNull();
        assertThat(GsonUtils.readMap((Reader)null, String.class, String.class)).isNull();
    }

    public void shouldRetrieveMapWhenReadReaderAsMap() throws IOException {
        Map<String, ?> expected = MapUtils.of(
                "title", "Thinking in Java",
                "date", "2017-07-23T13:57:14.225Z",
                "year", 1998,
                "authors", ListUtils.of("Bruce Eckel"));

        try (Reader in = new InputStreamReader(ReadReaderGsonUtilsTest.class.getResourceAsStream("/book.json"))) {
            Map<String, ?> actual = GsonUtils.readMap(in);
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }
    }

    public void shouldRetrieveDeserializedObjectWhenReadReaderAsCustomClass() throws IOException {
        Book expected = new Book(
                "Thinking in Java",
                ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                1998,
                ListUtils.of("Bruce Eckel"));

        try (Reader in = new InputStreamReader(ReadReaderGsonUtilsTest.class.getResourceAsStream("/book.json"))) {
            Book actual = GsonUtils.readValue(in, Book.class);
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }
    }

    public void shouldRetrieveDeserializedObjectWhenReadReaderAsCustomType() throws IOException {
        Book expected = new Book(
                "Thinking in Java",
                ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                1998,
                ListUtils.of("Bruce Eckel"));

        try (Reader in = new InputStreamReader(ReadReaderGsonUtilsTest.class.getResourceAsStream("/book.json"))) {
            Book actual = GsonUtils.read(in, Book.class);
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }
    }

    public void shouldRetrieveDeserializedListOfObjectsWhenReadReaderAsListWithCustomType() throws IOException {
        List<Book> expected = ListUtils.of(
                new Book(
                        "Thinking in Java",
                        ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                        1998,
                        ListUtils.of("Bruce Eckel")),
                new Book(
                        "Ready for a victory",
                        ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
                        2020,
                        ListUtils.of("Oleg Cherednik")));

        try (Reader in = new InputStreamReader(ReadReaderGsonUtilsTest.class.getResourceAsStream("/books.json"))) {
            List<Book> actual = GsonUtils.readList(in, Book.class);
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }
    }

    public void shouldRetrieveDeserializedMapWhenReadReaderAsMapListWithStringKeyAndCustomType() throws IOException {
        Map<String, Book> expected = MapUtils.of(
                "one", new Book(
                        "Thinking in Java",
                        ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                        1998,
                        ListUtils.of("Bruce Eckel")),
                "two", new Book(
                        "Ready for a victory",
                        ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
                        2020,
                        ListUtils.of("Oleg Cherednik")));

        try (Reader in = new InputStreamReader(ReadReaderGsonUtilsTest.class.getResourceAsStream("/books_dict_string_key.json"))) {
            Map<String, Book> actual = GsonUtils.readMap(in, Book.class);
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }
    }

    public void shouldRetrieveDeserializedMapWhenReadReaderAsMapListWithIntegerKeyAndCustomType() throws IOException {
        Map<Integer, Book> expected = MapUtils.of(
                1, new Book(
                        "Thinking in Java",
                        ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                        1998,
                        ListUtils.of("Bruce Eckel")),
                2, new Book(
                        "Ready for a victory",
                        ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
                        2020,
                        ListUtils.of("Oleg Cherednik")));

        try (Reader in = new InputStreamReader(ReadReaderGsonUtilsTest.class.getResourceAsStream("/books_dict_int_key.json"))) {
            Map<Integer, Book> actual = GsonUtils.readMap(in, Integer.class, Book.class);
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }
    }

    public void shouldRetrieveIteratorOfDeserializedObjectsWhenReadReaderAsLazyList() throws IOException {
        Book expected1 = new Book(
                "Thinking in Java",
                ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                1998,
                ListUtils.of("Bruce Eckel"));
        Book expected2 = new Book(
                "Ready for a victory",
                ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
                2020,
                ListUtils.of("Oleg Cherednik"));

        try (Reader in = new InputStreamReader(ReadReaderGsonUtilsTest.class.getResourceAsStream("/books.json"))) {
            Iterator<Book> it = GsonUtils.readListLazy(in, Book.class);
            assertThat(it.hasNext()).isTrue();

            Book actual1 = it.next();
            assertThat(actual1).isNotNull();
            assertThat(actual1).isEqualTo(expected1);
            assertThat(it.hasNext()).isTrue();

            Book actual2 = it.next();
            assertThat(actual2).isNotNull();
            assertThat(actual2).isEqualTo(expected2);
            assertThat(it.hasNext()).isFalse();
            assertThatThrownBy(it::next).isExactlyInstanceOf(NoSuchElementException.class);
        }
    }

    public void shouldReadListWithObjectsWhenReadListNoSpecifyValueClass() throws IOException {
        try (Reader in = new InputStreamReader(ReadReaderGsonUtilsTest.class.getResourceAsStream("/books.json"))) {
            List<?> actual = GsonUtils.readList(in);
            assertThat(actual).hasSize(2);
            assertThat(actual.get(0)).isInstanceOf(Map.class);
            assertThat(actual.get(1)).isInstanceOf(Map.class);
        }
    }

    public void shouldReadListWithObjectsWhenReadListLazyNoSpecifyValueClass() throws IOException {
        try (Reader in = new InputStreamReader(ReadReaderGsonUtilsTest.class.getResourceAsStream("/books.json"))) {
            Iterator<?> it = GsonUtils.readListLazy(in);
            assertThat(it.hasNext()).isTrue();
            assertThat(it.next()).isInstanceOf(Map.class);
            assertThat(it.hasNext()).isTrue();
            assertThat(it.next()).isInstanceOf(Map.class);
            assertThat(it.hasNext()).isFalse();
            assertThatThrownBy(it::next).isExactlyInstanceOf(NoSuchElementException.class);
        }
    }

}
