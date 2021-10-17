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
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.CustomObjectTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.DateTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.IteratorTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.LocalDateTimeTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.ZonedDateTimeTypeAdapter;
import ru.olegcherednik.utils.reflection.FieldUtils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
public class GsonUtilsBuilder {

    public static final UnaryOperator<ZoneId> ZONE_MODIFIER_USE_ORIGINAL = UnaryOperator.identity();
    public static final UnaryOperator<ZoneId> ZONE_MODIFIER_TO_UTC = zone -> ZoneOffset.UTC;

    protected Consumer<GsonBuilder> customizer = ((Consumer<GsonBuilder>)GsonBuilder::enableComplexMapKeySerialization)
            .andThen(b -> b.registerTypeAdapterFactory(IteratorTypeAdapter.INSTANCE))
            .andThen(b -> b.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter(ZONE_MODIFIER_TO_UTC, ISO_OFFSET_DATE_TIME)))
            .andThen(b -> b.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter(ZONE_MODIFIER_TO_UTC, ISO_OFFSET_DATE_TIME)))
            .andThen(b -> b.registerTypeAdapter(Date.class, new DateTypeAdapter(ZONE_MODIFIER_TO_UTC, ISO_OFFSET_DATE_TIME)));

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
        List<TypeAdapterFactory> factories = FieldUtils.<List<TypeAdapterFactory>>getFieldValue(gson, "factories")
                .stream()
                .map(factory -> factory == ObjectTypeAdapter.FACTORY ? CustomObjectTypeAdapter.FACTORY : factory)
                .collect(Collectors.toList());

        FieldUtils.setFieldValue(gson, "factories", factories);
    }

    protected GsonBuilder gsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        customizer.accept(builder);
        return builder;
    }

    // ---------- extended ----------

    public GsonUtilsBuilder zonedModifier(UnaryOperator<ZoneId> zoneModifier) {
        zonedDateTimeFormatter(zoneModifier, ISO_OFFSET_DATE_TIME);
        localDateTimeFormatter(zoneModifier, ISO_OFFSET_DATE_TIME);
        return dateFormatter(zoneModifier, ISO_OFFSET_DATE_TIME);
    }

    public GsonUtilsBuilder dateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        zonedDateTimeFormatter(ZONE_MODIFIER_TO_UTC, dateTimeFormatter);
        localDateTimeFormatter(ZONE_MODIFIER_TO_UTC, dateTimeFormatter);
        return dateFormatter(ZONE_MODIFIER_TO_UTC, dateTimeFormatter);
    }

    public GsonUtilsBuilder dateTimeFormatter(UnaryOperator<ZoneId> zoneModifier, DateTimeFormatter dateTimeFormatter) {
        zonedDateTimeFormatter(zoneModifier, dateTimeFormatter);
        localDateTimeFormatter(zoneModifier, dateTimeFormatter);
        return dateFormatter(zoneModifier, dateTimeFormatter);
    }

    public GsonUtilsBuilder zonedDateTimeFormatter(UnaryOperator<ZoneId> zoneModifier, DateTimeFormatter dateTimeFormatter) {
        return registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter(zoneModifier, dateTimeFormatter));
    }

    public GsonUtilsBuilder localDateTimeFormatter(UnaryOperator<ZoneId> zoneModifier, DateTimeFormatter dateTimeFormatter) {
        return registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter(zoneModifier, dateTimeFormatter));
    }

    public GsonUtilsBuilder dateFormatter(UnaryOperator<ZoneId> zoneModifier, DateTimeFormatter dateTimeFormatter) {
        return registerTypeAdapter(Date.class, new DateTypeAdapter(zoneModifier, dateTimeFormatter));
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
