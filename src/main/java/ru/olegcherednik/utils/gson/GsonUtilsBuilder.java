package ru.olegcherednik.utils.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import ru.olegcherednik.utils.gson.adapters.CustomObjectTypeAdapter;
import ru.olegcherednik.utils.gson.adapters.IteratorTypeAdapter;
import ru.olegcherednik.utils.gson.adapters.LocalDateTimeTypeAdapter;
import ru.olegcherednik.utils.gson.adapters.ZonedDateTimeTypeAdapter;
import ru.olegcherednik.utils.reflection.FieldUtils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
public class GsonUtilsBuilder {

    public static final Function<ZoneId, ZoneId> ZONE_MODIFIER_USE_ORIGINAL = zoneId -> zoneId;
    public static final Function<ZoneId, ZoneId> ZONE_MODIFIER_TO_UTC = zoneId -> ZoneOffset.UTC;

    protected Consumer<GsonBuilder> customizer = ((Consumer<GsonBuilder>)GsonBuilder::enableComplexMapKeySerialization)
            .andThen(b -> b.registerTypeAdapterFactory(IteratorTypeAdapter.FACTORY))
            .andThen(b -> b.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter(ZONE_MODIFIER_TO_UTC, ISO_ZONED_DATE_TIME)))
            .andThen(b -> b.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter(ISO_LOCAL_DATE_TIME)));

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
        } catch(Exception e) {
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

    public GsonUtilsBuilder zonedDateTimeFormatter(Function<ZoneId, ZoneId> zoneModifier, DateTimeFormatter dateTimeFormatter) {
        return registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter(zoneModifier, dateTimeFormatter));
    }

    public GsonUtilsBuilder localDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        return registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter(dateTimeFormatter));
    }

    // ---------- GsonBuilder ----------

    public GsonUtilsBuilder version(double ignoreVersionsAfter) {
        customizer = customizer.andThen(builder -> builder.setVersion(ignoreVersionsAfter));
        return this;
    }

    public GsonUtilsBuilder excludeFieldsWithModifiers(int... modifiers) {
        customizer = customizer.andThen(builder -> builder.excludeFieldsWithModifiers(modifiers));
        return this;
    }

    public GsonUtilsBuilder generateNonExecutableJson() {
        customizer = customizer.andThen(GsonBuilder::generateNonExecutableJson);
        return this;
    }

    public GsonUtilsBuilder excludeFieldsWithoutExposeAnnotation() {
        customizer = customizer.andThen(GsonBuilder::excludeFieldsWithoutExposeAnnotation);
        return this;
    }

    public GsonUtilsBuilder serializeNulls() {
        customizer = customizer.andThen(GsonBuilder::serializeNulls);
        return this;
    }

    public GsonUtilsBuilder disableInnerClassSerialization() {
        customizer = customizer.andThen(GsonBuilder::disableInnerClassSerialization);
        return this;
    }

    public GsonUtilsBuilder longSerializationPolicy(LongSerializationPolicy serializationPolicy) {
        customizer = customizer.andThen(builder -> builder.setLongSerializationPolicy(serializationPolicy));
        return this;
    }

    public GsonUtilsBuilder fieldNamingPolicy(FieldNamingPolicy namingConvention) {
        customizer = customizer.andThen(builder -> builder.setFieldNamingPolicy(namingConvention));
        return this;
    }

    public GsonUtilsBuilder fieldNamingStrategy(FieldNamingStrategy fieldNamingStrategy) {
        customizer = customizer.andThen(builder -> builder.setFieldNamingStrategy(fieldNamingStrategy));
        return this;
    }

    public GsonUtilsBuilder exclusionStrategies(ExclusionStrategy... strategies) {
        customizer = customizer.andThen(builder -> builder.setExclusionStrategies(strategies));
        return this;
    }

    public GsonUtilsBuilder addSerializationExclusionStrategy(ExclusionStrategy strategy) {
        customizer = customizer.andThen(builder -> builder.addSerializationExclusionStrategy(strategy));
        return this;
    }

    public GsonUtilsBuilder addDeserializationExclusionStrategy(ExclusionStrategy strategy) {
        customizer = customizer.andThen(builder -> builder.addDeserializationExclusionStrategy(strategy));
        return this;
    }

    public GsonUtilsBuilder setLenient() {
        customizer = customizer.andThen(GsonBuilder::setLenient);
        return this;
    }

    public GsonUtilsBuilder disableHtmlEscaping() {
        customizer = customizer.andThen(GsonBuilder::disableHtmlEscaping);
        return this;
    }

    public GsonUtilsBuilder registerTypeAdapter(Type type, Object typeAdapter) {
        customizer = customizer.andThen(builder -> builder.registerTypeAdapter(type, typeAdapter));
        return this;
    }

    public GsonUtilsBuilder registerTypeAdapterFactory(TypeAdapterFactory factory) {
        customizer = customizer.andThen(builder -> builder.registerTypeAdapterFactory(factory));
        return this;
    }

    public GsonUtilsBuilder registerTypeHierarchyAdapter(Class<?> baseType, Object typeAdapter) {
        customizer = customizer.andThen(builder -> builder.registerTypeHierarchyAdapter(baseType, typeAdapter));
        return this;
    }

    public GsonUtilsBuilder serializeSpecialFloatingPointValues() {
        customizer = customizer.andThen(GsonBuilder::serializeSpecialFloatingPointValues);
        return this;
    }

}
