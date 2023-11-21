package ru.olegcherednik.gson.utils;

import com.google.gson.Gson;
import ru.olegcherednik.json.JsonEngine;

import java.util.function.Supplier;

/**
 * @author Oleg Cherednik
 * @since 20.11.2023
 */
public class GsonJsonEngineSupplier implements Supplier<JsonEngine> {

    @Override
    public JsonEngine get() {
        Gson gson = GsonUtilsHelper.createGson();
        return new GsonJsonEngine(gson);
    }

}
