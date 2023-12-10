package ru.olegcherednik.json.impl;

import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.olegcherednik.json.gson.utils.DynamicToNumberStrategy;
import ru.olegcherednik.json.gson.utils.GsonEngine;
import ru.olegcherednik.json.gson.utils.datetime.DateTypeAdapter;
import ru.olegcherednik.json.gson.utils.adapters.EnumIdTypeAdapterFactory;
import ru.olegcherednik.json.gson.utils.datetime.InstantTypeAdapter;
import ru.olegcherednik.json.gson.utils.adapters.IteratorTypeAdapter;
import ru.olegcherednik.json.gson.utils.datetime.LocalDateTimeTypeAdapter;
import ru.olegcherednik.json.gson.utils.datetime.LocalDateTypeAdapter;
import ru.olegcherednik.json.gson.utils.datetime.LocalTimeTypeAdapter;
import ru.olegcherednik.json.gson.utils.datetime.OffsetDateTimeTypeAdapter;
import ru.olegcherednik.json.gson.utils.datetime.OffsetTimeTypeAdapter;
import ru.olegcherednik.json.gson.utils.datetime.ZonedDateTimeTypeAdapter;
import ru.olegcherednik.json.api.JsonEngine;
import ru.olegcherednik.json.api.JsonEngineFactory;
import ru.olegcherednik.json.api.JsonSettings;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Oleg Cherednik
 * @since 19.11.2023
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StaticJsonEngineFactory implements JsonEngineFactory {

    private static final StaticJsonEngineFactory INSTANCE = new StaticJsonEngineFactory();

    @SuppressWarnings("unused")
    public static StaticJsonEngineFactory getInstance() {
        return INSTANCE;
    }

    // ---------- JsonEngineFactory ----------

    @Override
    public GsonEngine createJsonEngine(JsonSettings settings) {
        GsonBuilder builder = createGsonBuilder(settings);
        return new GsonEngine(builder.create());
    }

    @Override
    public GsonEngine createPrettyPrintJsonEngine(JsonSettings settings) {
        GsonBuilder builder = createGsonBuilder(settings).setPrettyPrinting();
        return new GsonEngine(builder.create());
    }

    // ---------- supplier ----------

    private static GsonBuilder createGsonBuilder(JsonSettings settings) {
        Objects.requireNonNull(settings);

        GsonBuilder builder = new GsonBuilder().setObjectToNumberStrategy(DynamicToNumberStrategy.INSTANCE);
        InstantTypeAdapter instant = new InstantTypeAdapter(settings.getInstantFormatter(), settings.getZoneModifier());
        LocalDateTypeAdapter localDate = new LocalDateTypeAdapter(settings.getLocalDateFormatter());
        LocalTimeTypeAdapter localTime = new LocalTimeTypeAdapter(settings.getLocalTimeFormatter());
        LocalDateTimeTypeAdapter localDateTime = new LocalDateTimeTypeAdapter(settings.getLocalDateTimeFormatter());
        OffsetTimeTypeAdapter offsetTime = new OffsetTimeTypeAdapter(settings.getOffsetTimeFormatter(), settings.getZoneModifier());
        OffsetDateTimeTypeAdapter offsetDateTime = new OffsetDateTimeTypeAdapter(settings.getOffsetDateTimeFormatter(),
                                                                                 settings.getZoneModifier());
        ZonedDateTimeTypeAdapter zonedDateTime = new ZonedDateTimeTypeAdapter(settings.getOffsetDateTimeFormatter(),
                                                                              settings.getZoneModifier());
        DateTypeAdapter date = new DateTypeAdapter(settings.getDateFormatter());

        Consumer<GsonBuilder> customizer = ((Consumer<GsonBuilder>) GsonBuilder::enableComplexMapKeySerialization)
                .andThen(b -> b.registerTypeAdapterFactory(IteratorTypeAdapter.INSTANCE))
                .andThen(b -> b.registerTypeAdapterFactory(new EnumIdTypeAdapterFactory()))
                .andThen(b -> b.registerTypeAdapter(Instant.class, instant.nullSafe()))
                .andThen(b -> b.registerTypeAdapter(LocalTime.class, localTime.nullSafe()))
                .andThen(b -> b.registerTypeAdapter(LocalDate.class, localDate.nullSafe()))
                .andThen(b -> b.registerTypeAdapter(LocalDateTime.class, localDateTime.nullSafe()))
                .andThen(b -> b.registerTypeAdapter(OffsetTime.class, offsetTime.nullSafe()))
                .andThen(b -> b.registerTypeAdapter(OffsetDateTime.class, offsetDateTime.nullSafe()))
                .andThen(b -> b.registerTypeAdapter(ZonedDateTime.class, zonedDateTime.nullSafe()))
                .andThen(b -> b.registerTypeAdapter(Date.class, date.nullSafe()));

        customizer.accept(builder);
        return builder;
    }


}
