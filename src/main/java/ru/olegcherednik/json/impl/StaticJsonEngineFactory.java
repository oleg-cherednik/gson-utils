package ru.olegcherednik.json.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.olegcherednik.gson.utils.GsonJsonEngine;
import ru.olegcherednik.gson.utils.GsonJsonEngineSupplier;
import ru.olegcherednik.gson.utils.GsonUtilsHelper;
import ru.olegcherednik.json.api.JsonEngine;
import ru.olegcherednik.json.api.JsonEngineFactory;
import ru.olegcherednik.json.api.JsonSettings;

import java.util.Objects;

/**
 * @author Oleg Cherednik
 * @since 19.11.2023
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StaticJsonEngineFactory implements JsonEngineFactory {

    private static final JsonEngineFactory INSTANCE = new StaticJsonEngineFactory();

    private final GsonJsonEngineSupplier jsonEngineSupplier = new GsonJsonEngineSupplier();

    @SuppressWarnings("unused")
    public static JsonEngineFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public JsonEngine createJsonEngine() {
        return jsonEngineSupplier.get();
    }

    @Override
    public JsonEngine createJsonEnginePrettyPrint() {
        return jsonEngineSupplier.getPrettyPrint();
    }

    @Override
    public void useSettings(JsonSettings jsonSettings) {
        jsonEngineSupplier.setJsonSettings(Objects.requireNonNull(jsonSettings));
    }
}
