package ru.olegcherednik.utils.gson;

import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
public final class GsonUtils {

    private static final GsonDecorator DELEGATE = new GsonDecorator(GsonHelper::gson);
    private static final GsonDecorator PRETTY_PRINT_DELEGATE = new GsonDecorator(GsonHelper::prettyPrintMapper);

    // ---------- read String ----------

    public static <V> V readValue(String json, Class<V> valueClass) {
        return print().readValue(json, valueClass);
    }

    public static <V> List<V> readList(String json, Class<V> valueClass) {
        return print().readList(json, valueClass);
    }

    public static Map<String, ?> readMap(String json) {
        return print().readMap(json);
    }

    public static <V> Map<String, V> readMap(String json, Class<V> valueClass) {
        return print().readMap(json, valueClass);
    }

    public static <K, V> Map<K, V> readMap(String json, Class<K> keyClass, Class<V> valueClass) {
        return print().readMap(json, keyClass, valueClass);
    }

    // ---------- read Reader ----------

    public static <V> V readValue(Reader in, Class<V> valueClass) {
        return print().readValue(in, valueClass);
    }

    public static <V> List<V> readList(Reader in, Class<V> valueClass) {
        return print().readList(in, valueClass);
    }

    public static <V> Iterator<V> readListLazy(Reader in, Class<V> valueClass) {
        return print().readListLazy(in, valueClass);
    }

    public static Map<String, ?> readMap(Reader in) {
        return print().readMap(in);
    }

    public static <V> Map<String, V> readMap(Reader in, Class<V> valueClass) {
        return print().readMap(in, valueClass);
    }

    public static <K, V> Map<K, V> readMap(Reader in, Class<K> keyClass, Class<V> valueClass) {
        return print().readMap(in, keyClass, valueClass);
    }

    // ---------- write ----------

    public static <V> String writeValue(V obj) {
        return print().writeValue(obj);
    }

    public static <V> void writeValue(V obj, Writer out) {
        print().writeValue(obj, out);
    }

    // ---------- print ----------

    public static GsonDecorator print() {
        return DELEGATE;
    }

    public static GsonDecorator prettyPrint() {
        return PRETTY_PRINT_DELEGATE;
    }

    private GsonUtils() {
    }

}
