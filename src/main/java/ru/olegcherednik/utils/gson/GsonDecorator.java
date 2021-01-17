package ru.olegcherednik.utils.gson;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import ru.olegcherednik.utils.gson.types.IteratorParameterizedType;
import ru.olegcherednik.utils.gson.types.MapParameterizedType;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
public class GsonDecorator {

    protected final Supplier<Gson> supplier;

    public GsonDecorator(Gson gson) {
        this(() -> gson);
    }

    public GsonDecorator(Supplier<Gson> supplier) {
        this.supplier = supplier;
    }

    // ---------- read String ----------

    public <V> V readValue(String json, Class<V> valueClass) {
        Objects.requireNonNull(valueClass, "'valueClass' should not be null");

        if (json == null)
            return null;

        return withRuntimeException(() -> supplier.get().fromJson(json, valueClass));
    }

    public List<?> readList(String json) {
        return readList(json, Object.class);
    }

    public <V> List<V> readList(String json, Class<V> valueClass) {
        Objects.requireNonNull(valueClass, "'valueClass' should not be null");

        if (json == null)
            return null;
        if (isEmpty(json))
            return Collections.emptyList();

        return withRuntimeException(() -> {
            Class<V[]> arrayCls = (Class<V[]>)Array.newInstance(valueClass, 0).getClass();
            return Arrays.asList(supplier.get().fromJson(json, arrayCls));
        });
    }

    public Map<String, ?> readMap(String json) {
        if (json == null)
            return null;
        if (isEmpty(json))
            return Collections.emptyMap();

        return withRuntimeException(() -> (Map<String, ?>)supplier.get().fromJson(json, LinkedHashMap.class));
    }

    public <V> Map<String, V> readMap(String json, Class<V> valueClass) {
        return readMap(json, String.class, valueClass);
    }

    public <K, V> Map<K, V> readMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (json == null)
            return null;
        if (isEmpty(json))
            return Collections.emptyMap();

        return withRuntimeException(() -> supplier.get().fromJson(json, new MapParameterizedType<>(keyClass, valueClass)));
    }

    // ---------- read Reader ----------

    public <V> V readValue(Reader in, Class<V> valueClass) {
        Objects.requireNonNull(valueClass, "'valueClass' should not be null");

        if (in == null)
            return null;

        return withRuntimeException(() -> supplier.get().fromJson(in, valueClass));
    }

    public List<?> readList(Reader in) {
        return readList(in, Object.class);
    }

    public <V> List<V> readList(Reader in, Class<V> valueClass) {
        Objects.requireNonNull(valueClass, "'valueClass' should not be null");

        if (in == null)
            return null;

        return withRuntimeException(() -> {
            Class<V[]> arrayCls = (Class<V[]>)Array.newInstance(valueClass, 0).getClass();
            return Arrays.asList(supplier.get().fromJson(in, arrayCls));
        });
    }

    public Iterator<?> readListLazy(Reader in) {
        return readListLazy(in, Object.class);
    }

    public <V> Iterator<V> readListLazy(Reader in, Class<V> valueClass) {
        Objects.requireNonNull(valueClass, "'valueClass' should not be null");

        if (in == null)
            return null;

        return withRuntimeException(() -> {
            Gson gson = supplier.get();
            JsonReader jsonReader = gson.newJsonReader(in);
            return gson.fromJson(jsonReader, new IteratorParameterizedType<>(valueClass));
        });
    }

    public Map<String, ?> readMap(Reader in) {
        if (in == null)
            return null;

        return withRuntimeException(() -> {
            Gson gson = supplier.get();
            return (Map<String, ?>)gson.fromJson(in, LinkedHashMap.class);
        });
    }

    public <V> Map<String, V> readMap(Reader in, Class<V> valueClass) {
        return readMap(in, String.class, valueClass);
    }

    public <K, V> Map<K, V> readMap(Reader in, Class<K> keyClass, Class<V> valueClass) {
        if (in == null)
            return null;

        return withRuntimeException(() -> supplier.get().fromJson(in, new MapParameterizedType<>(keyClass, valueClass)));
    }

    // ---------- write ----------

    public <V> String writeValue(V obj) {
        if (obj == null)
            return null;

        return withRuntimeException(() -> supplier.get().toJson(obj));
    }

    public <V> void writeValue(V obj, Writer out) {
        Objects.requireNonNull(out, "'out' should not be null");

        withRuntimeException(() -> {
            supplier.get().toJson(obj, out);
            return null;
        });
    }

    // ---------- misc ----------

    private static <V> V withRuntimeException(Callable<V> task) {
        try {
            return task.call();
        } catch(Exception e) {
            throw new GsonUtilsException(e);
        }
    }

    @SuppressWarnings("PMD.AvoidReassigningParameters")
    private static boolean isEmpty(String json) {
        json = json.trim();
        return "{}".equals(json) || "[]".equals(json);
    }

}
