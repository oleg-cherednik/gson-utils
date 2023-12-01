package ru.olegcherednik.gson.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;
import lombok.Setter;
import ru.olegcherednik.gson.utils.adapters.DateTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.EnumIdTypeAdapterFactory;
import ru.olegcherednik.gson.utils.adapters.InstantTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.IteratorTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.LocalDateTimeTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.OffsetDateTimeTypeAdapter;
import ru.olegcherednik.gson.utils.adapters.ZonedDateTimeTypeAdapter;
import ru.olegcherednik.json.api.JsonEngine;
import ru.olegcherednik.json.api.JsonSettings;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Oleg Cherednik
 * @since 20.11.2023
 */
@Getter
@Setter
public class GsonJsonEngineSupplier implements Supplier<JsonEngine> {

    private JsonSettings jsonSettings = JsonSettings.builder().build();

    @Override
    public JsonEngine get() {
        return new GsonJsonEngine(builder().create());
    }

    public JsonEngine getPrettyPrint() {
        return new GsonJsonEngine(builder().setPrettyPrinting().create());
    }

    protected GsonBuilder builder() {
        GsonBuilder builder = new GsonBuilder().setObjectToNumberStrategy(DynamicToNumberStrategy.INSTANCE);
        DateTimeFormatter df = jsonSettings.getDateTimeFormatter();

        Consumer<GsonBuilder> customizer = ((Consumer<GsonBuilder>) GsonBuilder::enableComplexMapKeySerialization)
                .andThen(b -> b.registerTypeAdapterFactory(IteratorTypeAdapter.INSTANCE))
                .andThen(b -> b.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter(df).nullSafe()))
                .andThen(b -> b.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter(df).nullSafe()))
                .andThen(b -> b.registerTypeAdapter(Date.class, new DateTypeAdapter(df).nullSafe()))
                .andThen(b -> b.registerTypeAdapter(Instant.class, new InstantTypeAdapter(df).nullSafe()))
                .andThen(b -> b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter(df).nullSafe()))
                .andThen(b -> b.registerTypeAdapterFactory(new EnumIdTypeAdapterFactory()))
                .andThen(b -> b.registerTypeAdapter(Object.class, new TypeAdapter<Number>() {
                    @Override
                    public void write(JsonWriter out, Number value) throws IOException {
                        int a = 0;
                        a++;
                    }

                    @Override
                    public Number read(JsonReader in) throws IOException {
                        return null;
                    }
                }));

        customizer.accept(builder);
        return builder;
    }

}
