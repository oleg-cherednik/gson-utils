package ru.olegcherednik.json.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.olegcherednik.gson.utils.GsonJsonEngine;
import ru.olegcherednik.gson.utils.GsonJsonEngineSupplier;
import ru.olegcherednik.gson.utils.GsonUtilsHelper;
import ru.olegcherednik.json.JsonEngine;
import ru.olegcherednik.json.JsonEngineFactory;

import java.util.function.Supplier;

/**
 * @author Oleg Cherednik
 * @since 19.11.2023
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StaticJsonEngineFactory implements JsonEngineFactory {

    private static final JsonEngineFactory INSTANCE = new StaticJsonEngineFactory();

    private final Supplier<JsonEngine> defaultJsonEngineSupplier =
            new GsonJsonEngineSupplier();

    public static JsonEngineFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public JsonEngine createJsonEngine() {
        return new GsonJsonEngine(GsonUtilsHelper.createGson());
    }

    @Override
    public JsonEngine createJsonEnginePrettyPrint() {
        return new GsonJsonEngine(GsonUtilsHelper.createPrettyPrintGson());
    }
}
