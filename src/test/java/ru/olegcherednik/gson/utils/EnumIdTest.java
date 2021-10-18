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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * @author Oleg Cherednik
 * @since 17.10.2021
 */
@Test
public class EnumIdTest {

    public void shouldRetrieveJsonWhenEnumIdValue() {
        Data data = new Data(Auto.AUDI, Color.RED);
        String json = GsonUtils.writeValue(data);
        assertThat(json).isEqualTo("{\"notNullAuto\":\"audi\",\"notNullColor\":\"Red\"}");
    }

    @SuppressWarnings("ConstantConditions")
    public void shouldParseJsonWhenEnumIdValue() {
        String json = "{\"notNullAuto\":\"bmw\",\"notNullColor\":\"Green\"}";
        Data actual = GsonUtils.readValue(json, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual.notNullAuto).isSameAs(Auto.BMW);
        assertThat(actual.notNullColor).isSameAs(Color.GREEN);
        assertThat(actual.nullAuto).isNull();
        assertThat(actual.nullColor).isNull();
    }

    public void shouldRetrieveJsonWithNullWhenEnumIdValueAndSerializeNull() {
        Data data = new Data(Auto.MERCEDES, Color.BLUE);
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder().serializeNulls());
        String json = gson.writeValue(data);
        assertThat(json).isEqualTo("{\"notNullAuto\":\"mercedes\",\"notNullColor\":\"Blue\","
                + "\"nullAuto\":null,\"nullColor\":null}");
    }

    public void shouldThrowExceptionWhenReadEnumIdNoFactoryMethod() {
        String json = GsonUtils.writeValue(City.SAINT_PETERSBURG);
        assertThat(json).isEqualTo("\"Saint-Petersburg\"");

        assertThatCode(() -> GsonUtils.readValue(json, City.class))
                .isExactlyInstanceOf(GsonUtilsException.class);
    }

    public void shouldUseJsonCreatorAnnotatedMethodWhenParseIdAlsoExists() {
        String json = "\"Square_jsonCreator\"";
        Shape actual = GsonUtils.readValue(json, Shape.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isSameAs(Shape.SQUARE);
    }

    public void shouldThrowExceptionWhenDeserializeWithMultipleJsonCreatorMethods() {
        String json = GsonUtils.writeValue(Vodka.SMIRNOFF);
        assertThat(json).isEqualTo("\"smirnoff\"");
        assertThatCode(() -> GsonUtils.readValue(json, Vodka.class))
                .isExactlyInstanceOf(GsonUtilsException.class);
    }

    public void shouldUseNameWhenNoGetId() {
        assertThat(GsonUtils.writeValue(Shape.SQUARE)).isEqualTo("\"SQUARE\"");
    }

    @SuppressWarnings("FieldCanBeLocal")
    private static class Data {

        private final Auto notNullAuto;
        private final Color notNullColor;
        private final Auto nullAuto = null;
        private final Color nullColor = null;

        public Data(Auto notNullAuto, Color notNullColor) {
            this.notNullAuto = notNullAuto;
            this.notNullColor = notNullColor;
        }

    }

    public enum Auto implements EnumId {
        AUDI("audi"),
        BMW("bmw"),
        MERCEDES("mercedes");

        private final String id;

        Auto(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @JsonCreator
        @SuppressWarnings("unused")
        public static Auto parseId(String id) {
            return EnumId.parseId(Auto.class, id);
        }
    }

    public enum Color implements EnumId {
        RED("Red"),
        GREEN("Green"),
        BLUE("Blue");

        private final String id;

        Color(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @SuppressWarnings("unused")
        public static Color parseId(String id) {
            return EnumId.parseId(Color.class, id);
        }
    }

    public enum City implements EnumId {
        SAINT_PETERSBURG("Saint-Petersburg");

        private final String id;

        City(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }
    }

    @SuppressWarnings("unused")
    public enum Shape implements EnumId {
        SQUARE;

        @JsonCreator
        public static Shape jsonCreator(String id) {
            return "Square_jsonCreator".equals(id) ? SQUARE : null;
        }

        public static Shape parseId(String id) {
            return "Square_parseId".equals(id) ? SQUARE : null;
        }
    }

    @SuppressWarnings("unused")
    public enum Vodka implements EnumId {
        SMIRNOFF("smirnoff");

        private final String id;

        Vodka(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @JsonCreator
        public static Vodka one(String id) {
            return EnumId.parseId(Vodka.class, id);
        }

        @JsonCreator
        public static Vodka two(String id) {
            return EnumId.parseId(Vodka.class, id);
        }

    }

}
