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
package ru.olegcherednik.gson.utils.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.olegcherednik.gson.utils.dto.Data;
import ru.olegcherednik.gson.utils.spring.app.DataTypeAdapter;
import ru.olegcherednik.gson.utils.spring.app.SpringBootApp;
import ru.olegcherednik.gson.utils.spring.app.SpringBootService;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 26.07.2021
 */
@Test
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootApp.class)
public class SpringBootAppTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private SpringBootService service;

    public void shouldWriteObjectToJsonStringUsingCustomBuilder() {
//        Data data = new Data(666, "oleg");
//        String json = service.toJson(data);
//
//        System.out.println(json);
//
//        Map<String, Object> expected = new HashMap<>();
//        expected.put(DataTypeAdapter.FIELD_INT, 666);
//        expected.put(DataTypeAdapter.FIELD_STRING, "oleg");

//        Map<String, ?> actual = GsonUtils.readMap(json);
//        assertThat(actual).isEqualTo(expected);
    }

    public void shouldReadJsonUsingCustomBuilder() {
//        String json = "{\"int\":666,\"str\":\"oleg\"}\n";
//        Data actual = service.fromJson(json);
//        assertThat(actual.getIntVal()).isEqualTo(666);
//        assertThat(actual.getStrVal()).isEqualTo("oleg");
    }

}
