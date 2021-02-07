package ru.olegcherednik.utils.gson;

import com.google.gson.Gson;

import java.util.Optional;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
public final class GsonUtilsHelper {

    public static final GsonUtilsBuilder DEFAULT_BUILDER = new GsonUtilsBuilder();

    private static GsonUtilsBuilder gsonBuilder = DEFAULT_BUILDER;
    private static Gson gson = createGson();
    private static Gson prettyPrintGson = createPrettyPrintGson();

    @SuppressWarnings("PMD.DefaultPackage")
    static synchronized Gson gson() {
        return gson;
    }

    @SuppressWarnings("PMD.DefaultPackage")
    static synchronized Gson prettyPrintGson() {
        return prettyPrintGson;
    }

    public static synchronized Gson createGson() {
        return createGson(gsonBuilder);
    }

    public static synchronized Gson createPrettyPrintGson() {
        return createPrettyPrintGson(gsonBuilder);
    }

    public static Gson createGson(GsonUtilsBuilder gsonUtilsBuilder) {
        return gsonUtilsBuilder.gson();
    }

    public static Gson createPrettyPrintGson(GsonUtilsBuilder gsonUtilsBuilder) {
        return gsonUtilsBuilder.prettyPrintGson();
    }

    public static GsonDecorator createGsonDecorator(GsonUtilsBuilder gsonUtilsBuilder) {
        return new GsonDecorator(createGson(gsonUtilsBuilder));
    }

    public static GsonDecorator createPrettyPrintGsonDecorator(GsonUtilsBuilder gsonUtilsBuilder) {
        return new GsonDecorator(createPrettyPrintGson(gsonUtilsBuilder));
    }

    @SuppressWarnings("PMD.AvoidReassigningParameters")
    public static synchronized void setGsonBuilder(GsonUtilsBuilder gsonBuilder) {
        gsonBuilder = Optional.ofNullable(gsonBuilder).orElse(DEFAULT_BUILDER);

        if (gsonBuilder == GsonUtilsHelper.gsonBuilder)
            return;

        GsonUtilsHelper.gsonBuilder = gsonBuilder;
        gson = createGson();
        prettyPrintGson = createPrettyPrintGson();
    }

    private GsonUtilsHelper() {
    }
}
