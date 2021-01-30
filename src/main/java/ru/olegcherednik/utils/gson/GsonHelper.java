package ru.olegcherednik.utils.gson;

import com.google.gson.Gson;

import java.util.Optional;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
public final class GsonHelper {

    public static final GsonBuilderDecorator DEFAULT_BUILDER_DECORATOR = new GsonBuilderDecorator();

    private static GsonBuilderDecorator gsonBuilderDecorator = DEFAULT_BUILDER_DECORATOR;
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

    public static Gson createGson() {
        return createGson(gsonBuilderDecorator);
    }

    public static Gson createPrettyPrintGson() {
        return createPrettyPrintGson(gsonBuilderDecorator);
    }

    public static Gson createGson(GsonBuilderDecorator gsonBuilderDecorator) {
        return gsonBuilderDecorator.gson();
    }

    public static Gson createPrettyPrintGson(GsonBuilderDecorator gsonBuilderDecorator) {
        return gsonBuilderDecorator.prettyPrintGson();
    }

    public static GsonDecorator createGsonDecorator(GsonBuilderDecorator gsonBuilderDecorator) {
        return new GsonDecorator(createGson(gsonBuilderDecorator));
    }

    public static GsonDecorator createPrettyPrintGsonDecorator(GsonBuilderDecorator gsonBuilderDecorator) {
        return new GsonDecorator(createPrettyPrintGson(gsonBuilderDecorator));
    }

    @SuppressWarnings("PMD.AvoidReassigningParameters")
    public static synchronized void setGsonBuilderDecorator(GsonBuilderDecorator gsonBuilderDecorator) {
        gsonBuilderDecorator = Optional.ofNullable(gsonBuilderDecorator).orElse(DEFAULT_BUILDER_DECORATOR);

        if (gsonBuilderDecorator == GsonHelper.gsonBuilderDecorator)
            return;

        GsonHelper.gsonBuilderDecorator = gsonBuilderDecorator;
        gson = createGson();
        prettyPrintGson = createPrettyPrintGson();
    }

    private GsonHelper() {
    }
}
