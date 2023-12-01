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

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import org.testng.annotations.Test;
import ru.olegcherednik.gson.utils.dto.Data;

import java.lang.reflect.Modifier;

import static org.mockito.Mockito.mock;

/**
 * @author Oleg Cherednik
 * @since 17.01.2021
 */
@Test
@SuppressWarnings("LocalVariableNamingConvention")
public class GsonUtilsBuilderTest {

    public void shouldThrowGsonUtilsExceptionWhenPostCreateThrowsException() {
//        GsonUtilsBuilder builder = new GsonUtilsBuilder();
//
//        assertThatThrownBy(() -> builder.postCreate(null))
//                .isExactlyInstanceOf(GsonUtilsException.class)
//                .hasCauseInstanceOf(NullPointerException.class);
    }

    public void shouldDelegateSettingsToGsonBuilderWhenUseDecorator() throws Exception {
        ExclusionStrategy exclusionStrategy = mock(ExclusionStrategy.class);
        ExclusionStrategy serializationExclusionStrategy = mock(ExclusionStrategy.class);
        ExclusionStrategy deserializationExclusionStrategy = mock(ExclusionStrategy.class);

        TypeAdapter<Data> typeAdapter = (TypeAdapter<Data>) mock(TypeAdapter.class);
        FieldNamingStrategy fieldNamingStrategy = mock(FieldNamingStrategy.class);

        GsonUtilsBuilder builder = new GsonUtilsBuilder()
                .version(666.66)
                .excludeFieldsWithModifiers(Modifier.PUBLIC | Modifier.PROTECTED)
                .generateNonExecutableJson()
                .excludeFieldsWithoutExposeAnnotation()
                .disableInnerClassSerialization()
                .longSerializationPolicy(LongSerializationPolicy.STRING)
                .fieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
                .fieldNamingStrategy(fieldNamingStrategy)
                .exclusionStrategies(exclusionStrategy)
                .addSerializationExclusionStrategy(serializationExclusionStrategy)
                .addDeserializationExclusionStrategy(deserializationExclusionStrategy)
                .setLenient()
                .disableHtmlEscaping()
                .registerTypeAdapter(Data.class, typeAdapter)
                .registerTypeAdapterFactory(mock(TypeAdapterFactory.class))
                .registerTypeHierarchyAdapter(Data.class, typeAdapter)
                .serializeSpecialFloatingPointValues();

        Gson gson = builder.gson();

//        Excluder excluder = FieldUtils.getFieldValue(gson, "excluder");
//        List<ExclusionStrategy> serializationStrategies = FieldUtils.getFieldValue(excluder, "serializationStrategies");
//        List<ExclusionStrategy> deserializationStrategies = FieldUtils.getFieldValue(excluder, "deserializationStrategies");

//        assertThat(FieldUtils.<Double>getFieldValue(excluder, "version")).isEqualTo(666.66);
//        assertThat(FieldUtils.<Integer>getFieldValue(excluder, "modifiers")).isEqualTo(Modifier.PUBLIC | Modifier.PROTECTED);
//        assertThat(FieldUtils.<Boolean>getFieldValue(excluder, "requireExpose")).isTrue();
//        assertThat(FieldUtils.<Boolean>getFieldValue(excluder, "serializeInnerClasses")).isFalse();
//        assertThat(serializationStrategies).containsExactly(exclusionStrategy, serializationExclusionStrategy);
//        assertThat(deserializationStrategies).containsExactly(exclusionStrategy, deserializationExclusionStrategy);

//        assertThat(FieldUtils.<Boolean>getFieldValue(gson, "generateNonExecutableJson")).isTrue();
//        assertLongSerializationPolicyString(gson);
//        assertThat(FieldUtils.<Boolean>getFieldValue(gson, "lenient")).isTrue();
//        assertThat(FieldUtils.<Boolean>getFieldValue(gson, "htmlSafe")).isFalse();
//        assertSerializeSpecialFloatingPointValuesTrue(gson);
    }

//    private static void assertLongSerializationPolicyString(Gson gson) throws Exception {
//        try {
//            LongSerializationPolicy actual = FieldUtils.getFieldValue(gson, "longSerializationPolicy");
//            assertThat(actual).isEqualTo(LongSerializationPolicy.STRING);
//        } catch (NoSuchElementException ignored) {
//        }
//    }

//    private static void assertSerializeSpecialFloatingPointValuesTrue(Gson gson) throws Exception {
//        try {
//            boolean actual = FieldUtils.getFieldValue(gson, "serializeSpecialFloatingPointValues");
//            assertThat(actual).isTrue();
//        } catch (NoSuchElementException ignored) {
//        }
//    }

}
