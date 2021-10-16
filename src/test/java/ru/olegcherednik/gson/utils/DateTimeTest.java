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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Oleg Cherednik
 * @since 16.10.2021
 */
@Test
public class DateTimeTest {

    private final Data data = new Data();

    public void foo() {
        String json = GsonUtils.writeValue(data);
        System.out.println(json);
    }

    @SuppressWarnings("unused")
    private static class Data {

        private final Date date = new Date();
        private final Instant instant = date.toInstant();
        private final LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        private final ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
//        private final OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
//        private final LocalDate localDate = LocalDate.now();
//        private final LocalTime localTime = LocalTime.now();

    }

}
