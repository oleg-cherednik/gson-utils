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
package ru.olegcherednik.json.gson.datetime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.gson.ResourceData;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 01.12.2023
 */
@Test
public class DateTimeCombinationTest {

    private static final ZonedDateTime ZONED_DATE_TIME =
            ZonedDateTime.parse("2023-12-03T10:39:20.187+03:00[Europe/Moscow]");

    public void shouldWriteDatesWithDefaultJsonSettings() throws IOException {
        DataOne data = new DataOne(ZONED_DATE_TIME);
        String actual = Json.writeValue(data);
        String expected = ResourceData.getResourceAsString("/datetime/def_date_one.json").trim();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldReadDatesWithDefaultJsonSettings() throws IOException {
        String json = ResourceData.getResourceAsString("/datetime/def_date_one.json").trim();
        DataOne actual = Json.readValue(json, DataOne.class);
        DataOne expected = new DataOne(ZONED_DATE_TIME);
        assertThat(actual).isEqualTo(expected);
    }

    @Getter
    @EqualsAndHashCode
    @SuppressWarnings({ "FieldCanBeLocal", "unused" })
    private static final class DataOne {

        private final Instant instant;
        private final LocalDate localDate;
        private final LocalTime localTime;
        private final LocalDateTime localDateTime;
        private final OffsetTime offsetTime;
        private final OffsetDateTime offsetDateTime;
        private final ZonedDateTime zonedDateTime;
        private final Date date;

        public DataOne(ZonedDateTime zonedDateTime) {
            this(zonedDateTime.toInstant(),
                 zonedDateTime.toLocalDate(),
                 zonedDateTime.toLocalTime(),
                 zonedDateTime.toLocalDateTime(),
                 zonedDateTime.toOffsetDateTime().toOffsetTime(),
                 zonedDateTime.toOffsetDateTime(),
                 zonedDateTime,
                 Date.from(zonedDateTime.toInstant()));
        }

        public DataOne(Instant instant,
                       LocalDate localDate,
                       LocalTime localTime,
                       LocalDateTime localDateTime,
                       OffsetTime offsetTime,
                       OffsetDateTime offsetDateTime,
                       ZonedDateTime zonedDateTime,
                       Date date) {
            this.instant = instant;
            this.localDate = localDate;
            this.localTime = localTime;
            this.localDateTime = localDateTime;
            this.offsetTime = offsetTime;
            this.offsetDateTime = offsetDateTime;
            this.zonedDateTime = zonedDateTime;
            this.date = new Date(date.getTime());
        }

    }

}
