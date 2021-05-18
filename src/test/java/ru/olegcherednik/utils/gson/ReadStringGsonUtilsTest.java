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

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
@Test
public class ReadStringGsonUtilsTest {

    public void shouldRetrieveNullWhenObjectNull() {
        assertThat(GsonUtils.readValue((String)null, Object.class)).isNull();
        assertThat(GsonUtils.readList((String)null, Object.class)).isNull();
        assertThat(GsonUtils.readMap((String)null)).isNull();
        assertThat(GsonUtils.readMap((String)null, String.class, String.class)).isNull();
    }

    public void shouldRetrieveDeserializedObjectWhenReadJson() {
        Data expected = new Data(666, "omen");
        Data actual = GsonUtils.readValue("{\"intVal\":666,\"strVal\":\"omen\"}", Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldRetrieveEmptyDeserializedObjectWhenReadEmptyJson() {
        Data expected = new Data();
        Data actual = GsonUtils.readValue("{}", Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldRetrieveDeserializedListWhenReadJsonAsList() {
        String json = "[{\"intVal\":555,\"strVal\":\"victory\"},{\"intVal\":666,\"strVal\":\"omen\"}]";
        List<Data> actual = GsonUtils.readList(json, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(ListUtils.of(new Data(555, "victory"), new Data(666, "omen")));
    }

    public void shouldRetrieveEmptyListWhenReadEmptyJsonAsList() {
        assertThat(GsonUtils.readList("{}", Data.class)).isSameAs(Collections.emptyList());
        assertThat(GsonUtils.readList("[]", Data.class)).isSameAs(Collections.emptyList());
    }

    public void shouldRetrieveMapWhenReadJsonAsMap() {
        String json = "{\"sample\":[\"one, two\",\"three\"],\"order\":{\"key1\":\"val1\",\"key2\":\"val2\"}}";
        Map<String, ?> actual = GsonUtils.readMap(json);
        assertThat(actual).isNotNull();
        assertThat(actual.keySet()).containsExactly("sample", "order");
        assertThat(actual.get("sample")).isEqualTo(ListUtils.of("one, two", "three"));
        assertThat(actual.get("order")).isEqualTo(MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveStringValueMapWhenReadJsonAsMap() {
        String json = "{\"key1\":\"val1\",\"key2\":\"val2\"}";
        Map<String, ?> actual = GsonUtils.readMap(json);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveDataMapWhenReadJsonAsMapWithStringKeyAndGivenValueType() {
        String json = "{\"victory\":{\"intVal\":555,\"strVal\":\"victory\"},\"omen\":{\"intVal\":666,\"strVal\":\"omen\"}}";
        Map<String, Data> actual = GsonUtils.readMap(json, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual.keySet()).containsExactly("victory", "omen");
        assertThat(actual.get("victory")).isEqualTo(new Data(555, "victory"));
        assertThat(actual.get("omen")).isEqualTo(new Data(666, "omen"));
    }

    public void shouldRetrieveStringValueMapWhenReadJsonAsMapWithStringKeyAndType() {
        String json = "{\"key1\":\"val1\",\"key2\":\"val2\"}";
        Map<String, String> actual = GsonUtils.readMap(json, String.class);
        assertThat(actual).isNotNull();
        assertThat(actual.keySet()).containsExactly("key1", "key2");
        assertThat(actual).isEqualTo(MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveIntegerValueMapWhenReadJsonAsMapWithIntegerKeyAndGivenValueType() {
        String json = "{\"1\":{\"intVal\":555,\"strVal\":\"victory\"},\"2\":{\"intVal\":666,\"strVal\":\"omen\"}}";
        Map<Integer, Data> actual = GsonUtils.readMap(json, Integer.class, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual.keySet()).containsExactly(1, 2);
        assertThat(actual.get(1)).isEqualTo(new Data(555, "victory"));
        assertThat(actual.get(2)).isEqualTo(new Data(666, "omen"));
    }

    public void shouldRetrieveEmptyMapWhenReadEmptyJsonAsMap() {
        assertThat(GsonUtils.readMap("{}")).isSameAs(Collections.emptyMap());
        assertThat(GsonUtils.readMap("[]")).isSameAs(Collections.emptyMap());
        assertThat(GsonUtils.readMap("{}", String.class, Data.class)).isSameAs(Collections.emptyMap());
        assertThat(GsonUtils.readMap("[]", String.class, Data.class)).isSameAs(Collections.emptyMap());
    }

    public void shouldThrowGsonUtilsExceptionWhenReadIncorrectJson() {
        assertThatThrownBy(() -> GsonUtils.readValue("incorrect", Data.class)).isExactlyInstanceOf(GsonUtilsException.class);
        assertThatThrownBy(() -> GsonUtils.readList("incorrect", Data.class)).isExactlyInstanceOf(GsonUtilsException.class);
        assertThatThrownBy(() -> GsonUtils.readMap("incorrect")).isExactlyInstanceOf(GsonUtilsException.class);
        assertThatThrownBy(() -> GsonUtils.readMap("incorrect", String.class, Data.class)).isExactlyInstanceOf(GsonUtilsException.class);
    }

    public void shouldRetrieveCorrectNumericWhenObjectContainsDifferentNumeric() {
        String json = "[1,2.0,3.1,12345678912,123456789123456789123456789123456789]";
        List<?> actual = GsonUtils.readList(json);

        assertThat(actual).hasSize(5);
        assertThat(actual.get(0)).isEqualTo(1);
        assertThat(actual.get(1)).isEqualTo(2);
        assertThat(actual.get(2)).isEqualTo(3.1);
        assertThat(actual.get(3)).isEqualTo(12345678912L);
        assertThat(actual.get(4)).isEqualTo(new BigInteger("123456789123456789123456789123456789"));
    }

}
