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
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import ru.olegcherednik.gson.utils.adapters.CustomObjectTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.DateTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.EnumIdTypeAdapterFactory;
import ru.olegcherednik.gson.utils.adapters.InstantTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.IteratorTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.LocalDateTimeTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.OffsetDateTimeTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.ZonedDateTimeTypeAdapter;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
public class GsonUtilsBuilder {

    private DateTimeFormatter df = ISO_OFFSET_DATE_TIME;

    protected Consumer<GsonBuilder> customizer = ((Consumer<GsonBuilder>) GsonBuilder::enableComplexMapKeySerialization)
            .andThen(b -> b.registerTypeAdapterFactory(IteratorTypeAdapter.INSTANCE))
            .andThen(b -> b.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter(df).nullSafe()))
            .andThen(b -> b.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter(df).nullSafe()))
            .andThen(b -> b.registerTypeAdapter(Date.class, new DateTypeAdapter(df).nullSafe()))
            .andThen(b -> b.registerTypeAdapter(Instant.class, new InstantTypeAdapter(df).nullSafe()))
            .andThen(b -> b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter(df).nullSafe()))
            .andThen(b -> b.registerTypeAdapterFactory(new EnumIdTypeAdapterFactory()))
            .andThen(b -> b.registerTypeAdapterFactory(new TypeAdapterFactory() {
                @Override
                public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
                    return null;
                }
            }));

    public Gson gson() {
        return postCreate(gsonBuilder().create());
    }

    public Gson prettyPrintGson() {
        return postCreate(gsonBuilder().setPrettyPrinting().create());
    }

    protected Gson postCreate(Gson gson) {
        try {
            updateFactories(gson);
            return gson;
        } catch (Exception e) {
            throw new GsonUtilsException(e);
        }
    }

    protected void updateFactories(Gson gson) throws Exception {
//        List<TypeAdapterFactory> factories = FieldUtils.<List<TypeAdapterFactory>>getFieldValue(gson, "factories")
//                                                       .stream()
//                                                       .map(factory -> factory == ObjectTypeAdapter.FACTORY ? CustomObjectTypeAdapter.FACTORY : factory)
//                                                       .collect(Collectors.toList());
//
//        FieldUtils.setFieldValue(gson, "factories", factories);
    }

    protected GsonBuilder gsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        customizer.accept(builder);
        return builder;
    }

    // ---------- extended ----------

    public GsonUtilsBuilder dateTimeFormatter(DateTimeFormatter df) {
        this.df = Objects.requireNonNull(df);
        return zonedDateTimeFormatter(df)
                .localDateTimeFormatter(df)
                .dateFormatter(df)
                .instantFormatter(df)
                .offsetDateTimeFormatter(df);
    }

    public GsonUtilsBuilder zonedDateTimeFormatter(DateTimeFormatter df) {
        TypeAdapter<ZonedDateTime> typeAdapter = new ZonedDateTimeTypeAdapter(df);
        return registerTypeAdapter(ZonedDateTime.class, typeAdapter.nullSafe());
    }

    public GsonUtilsBuilder localDateTimeFormatter(DateTimeFormatter df) {
        TypeAdapter<LocalDateTime> typeAdapter = new LocalDateTimeTypeAdapter(df);
        return registerTypeAdapter(LocalDateTime.class, typeAdapter.nullSafe());
    }

    public GsonUtilsBuilder dateFormatter(DateTimeFormatter df) {
        TypeAdapter<Date> typeAdapter = new DateTypeAdapter(df);
        return registerTypeAdapter(Date.class, typeAdapter.nullSafe());
    }

    public GsonUtilsBuilder instantFormatter(DateTimeFormatter df) {
        TypeAdapter<Instant> typeAdapter = new InstantTypeAdapter(df);
        return registerTypeAdapter(Instant.class, typeAdapter.nullSafe());
    }

    public GsonUtilsBuilder offsetDateTimeFormatter(DateTimeFormatter df) {
        TypeAdapter<OffsetDateTime> typeAdapter = new OffsetDateTimeTypeAdapter(df);
        return registerTypeAdapter(OffsetDateTime.class, typeAdapter.nullSafe());
    }

    public GsonUtilsBuilder addCustomizer(Consumer<GsonBuilder> customizer) {
        this.customizer = this.customizer.andThen(customizer);
        return this;
    }

    // ---------- GsonBuilder ----------

    public GsonUtilsBuilder version(double ignoreVersionsAfter) {
        return addCustomizer(builder -> builder.setVersion(ignoreVersionsAfter));
    }

    public GsonUtilsBuilder excludeFieldsWithModifiers(int... modifiers) {
        return addCustomizer(builder -> builder.excludeFieldsWithModifiers(modifiers));
    }

    public GsonUtilsBuilder generateNonExecutableJson() {
        return addCustomizer(GsonBuilder::generateNonExecutableJson);
    }

    public GsonUtilsBuilder excludeFieldsWithoutExposeAnnotation() {
        return addCustomizer(GsonBuilder::excludeFieldsWithoutExposeAnnotation);
    }

    public GsonUtilsBuilder serializeNulls() {
        return addCustomizer(GsonBuilder::serializeNulls);
    }

    public GsonUtilsBuilder disableInnerClassSerialization() {
        return addCustomizer(GsonBuilder::disableInnerClassSerialization);
    }

    public GsonUtilsBuilder longSerializationPolicy(LongSerializationPolicy serializationPolicy) {
        return addCustomizer(builder -> builder.setLongSerializationPolicy(serializationPolicy));
    }

    public GsonUtilsBuilder fieldNamingPolicy(FieldNamingPolicy namingConvention) {
        return addCustomizer(builder -> builder.setFieldNamingPolicy(namingConvention));
    }

    public GsonUtilsBuilder fieldNamingStrategy(FieldNamingStrategy fieldNamingStrategy) {
        return addCustomizer(builder -> builder.setFieldNamingStrategy(fieldNamingStrategy));
    }

    public GsonUtilsBuilder exclusionStrategies(ExclusionStrategy... strategies) {
        return addCustomizer(builder -> builder.setExclusionStrategies(strategies));
    }

    public GsonUtilsBuilder addSerializationExclusionStrategy(ExclusionStrategy strategy) {
        return addCustomizer(builder -> builder.addSerializationExclusionStrategy(strategy));
    }

    public GsonUtilsBuilder addDeserializationExclusionStrategy(ExclusionStrategy strategy) {
        return addCustomizer(builder -> builder.addDeserializationExclusionStrategy(strategy));
    }

    public GsonUtilsBuilder setLenient() {
        return addCustomizer(GsonBuilder::setLenient);
    }

    public GsonUtilsBuilder disableHtmlEscaping() {
        return addCustomizer(GsonBuilder::disableHtmlEscaping);
    }

    public GsonUtilsBuilder registerTypeAdapter(Type type, Object typeAdapter) {
        return addCustomizer(builder -> builder.registerTypeAdapter(type, typeAdapter));
    }

    public GsonUtilsBuilder registerTypeAdapterFactory(TypeAdapterFactory factory) {
        return addCustomizer(builder -> builder.registerTypeAdapterFactory(factory));
    }

    public GsonUtilsBuilder registerTypeHierarchyAdapter(Class<?> baseType, Object typeAdapter) {
        return addCustomizer(builder -> builder.registerTypeHierarchyAdapter(baseType, typeAdapter));
    }

    public GsonUtilsBuilder serializeSpecialFloatingPointValues() {
        return addCustomizer(GsonBuilder::serializeSpecialFloatingPointValues);
    }

}
