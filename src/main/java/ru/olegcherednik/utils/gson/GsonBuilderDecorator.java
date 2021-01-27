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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
public class GsonBuilderDecorator {

    public static final Function<ZoneId, ZoneId> ZONE_MODIFIER_USE_ORIGINAL = zoneId -> zoneId;
    public static final Function<ZoneId, ZoneId> ZONE_MODIFIER_TO_UTC = zoneId -> ZoneOffset.UTC;

    protected final List<Consumer<GsonBuilder>> consumers = new ArrayList<>();

    protected DateTimeFormatter zonedDateTimeFormatter = ISO_ZONED_DATE_TIME;
    protected DateTimeFormatter localDateTimeFormatter = ISO_LOCAL_DATE_TIME;
    protected Function<ZoneId, ZoneId> zoneModifier = ZONE_MODIFIER_TO_UTC;

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

        consumers.forEach(consumer -> consumer.accept(builder));

        builder.enableComplexMapKeySerialization();
        builder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter(zoneModifier, zonedDateTimeFormatter));
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter(localDateTimeFormatter));
        builder.registerTypeAdapterFactory(IteratorTypeAdapter.FACTORY);

        return builder;
    }

    // ---------- extended ----------

    public GsonBuilderDecorator withZoneModifier(Function<ZoneId, ZoneId> zoneModifier) {
        this.zoneModifier = Optional.ofNullable(zoneModifier).orElse(this.zoneModifier);
        return this;
    }

    public GsonBuilderDecorator withZonedDateTimeFormatter(DateTimeFormatter zonedDateTimeFormatter) {
        this.zonedDateTimeFormatter = Optional.ofNullable(zonedDateTimeFormatter).orElse(this.zonedDateTimeFormatter);
        return this;
    }

    public GsonBuilderDecorator withLocalDateTimeFormatter(DateTimeFormatter localDateTimeFormatter) {
        this.localDateTimeFormatter = Optional.ofNullable(localDateTimeFormatter).orElse(this.localDateTimeFormatter);
        return this;
    }

    // ---------- GsonBuilder ----------

    public GsonBuilderDecorator setVersion(double ignoreVersionsAfter) {
        consumers.add(delegate -> delegate.setVersion(ignoreVersionsAfter));
        return this;
    }

    public GsonBuilderDecorator excludeFieldsWithModifiers(int... modifiers) {
        consumers.add(delegate -> delegate.excludeFieldsWithModifiers(modifiers));
        return this;
    }

    public GsonBuilderDecorator generateNonExecutableJson() {
        consumers.add(GsonBuilder::generateNonExecutableJson);
        return this;
    }

    public GsonBuilderDecorator excludeFieldsWithoutExposeAnnotation() {
        consumers.add(GsonBuilder::excludeFieldsWithoutExposeAnnotation);
        return this;
    }

    public GsonBuilderDecorator serializeNulls() {
        consumers.add(GsonBuilder::serializeNulls);
        return this;
    }

    public GsonBuilderDecorator disableInnerClassSerialization() {
        consumers.add(GsonBuilder::disableInnerClassSerialization);
        return this;
    }

    public GsonBuilderDecorator setLongSerializationPolicy(LongSerializationPolicy serializationPolicy) {
        consumers.add(delegate -> delegate.setLongSerializationPolicy(serializationPolicy));
        return this;
    }

    public GsonBuilderDecorator setFieldNamingPolicy(FieldNamingPolicy namingConvention) {
        consumers.add(delegate -> delegate.setFieldNamingPolicy(namingConvention));
        return this;
    }

    public GsonBuilderDecorator setFieldNamingStrategy(FieldNamingStrategy fieldNamingStrategy) {
        consumers.add(delegate -> delegate.setFieldNamingStrategy(fieldNamingStrategy));
        return this;
    }

    public GsonBuilderDecorator setExclusionStrategies(ExclusionStrategy... strategies) {
        consumers.add(delegate -> delegate.setExclusionStrategies(strategies));
        return this;
    }

    public GsonBuilderDecorator addSerializationExclusionStrategy(ExclusionStrategy strategy) {
        consumers.add(delegate -> delegate.addSerializationExclusionStrategy(strategy));
        return this;
    }

    public GsonBuilderDecorator addDeserializationExclusionStrategy(ExclusionStrategy strategy) {
        consumers.add(delegate -> delegate.addDeserializationExclusionStrategy(strategy));
        return this;
    }

    public GsonBuilderDecorator setLenient() {
        consumers.add(GsonBuilder::setLenient);
        return this;
    }

    public GsonBuilderDecorator disableHtmlEscaping() {
        consumers.add(GsonBuilder::disableHtmlEscaping);
        return this;
    }

    public GsonBuilderDecorator registerTypeAdapter(Type type, Object typeAdapter) {
        consumers.add(delegate -> delegate.registerTypeAdapter(type, typeAdapter));
        return this;
    }

    public GsonBuilderDecorator registerTypeAdapterFactory(TypeAdapterFactory factory) {
        consumers.add(delegate -> delegate.registerTypeAdapterFactory(factory));
        return this;
    }

    public GsonBuilderDecorator registerTypeHierarchyAdapter(Class<?> baseType, Object typeAdapter) {
        consumers.add(delegate -> delegate.registerTypeHierarchyAdapter(baseType, typeAdapter));
        return this;
    }

    public GsonBuilderDecorator serializeSpecialFloatingPointValues() {
        consumers.add(GsonBuilder::serializeSpecialFloatingPointValues);
        return this;
    }

}
