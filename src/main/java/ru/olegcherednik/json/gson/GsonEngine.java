package ru.olegcherednik.json.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import lombok.RequiredArgsConstructor;
import ru.olegcherednik.json.gson.types.IteratorParameterizedType;
import ru.olegcherednik.json.gson.types.MapParameterizedType;
import ru.olegcherednik.json.api.JsonEngine;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Oleg Cherednik
 * @since 20.11.2023
 */
@RequiredArgsConstructor
public class GsonEngine implements JsonEngine {

    private final Gson gson;

    // ---------- read String ----------

    @Override
    public <T> T readValue(String json, Class<T> valueClass) {
        return gson.fromJson(json, valueClass);
    }

    @Override
    public <V> List<V> readList(String json, Class<V> valueClass) {
        Type type = TypeToken.getParameterized(List.class, valueClass).getType();
        return gson.fromJson(json, type);
    }

    @Override
    public <V> Set<V> readSet(String json, Class<V> valueClass) {
        Type type = TypeToken.getParameterized(LinkedHashSet.class, valueClass).getType();
        return gson.fromJson(json, type);
    }

    @Override
    public List<Map<String, Object>> readListOfMap(String json) {
        return (List<Map<String, Object>>) gson.fromJson(json, List.class);
    }

    @Override
    public <K, V> Map<K, V> readMap(String json, Class<K> keyClass, Class<V> valueClass) {
        return gson.fromJson(json, new MapParameterizedType<>(keyClass, valueClass));
    }

    // ---------- read Reader ----------

    @Override
    public <V> V readValue(Reader reader, Class<V> valueClass) throws IOException {
        try (Reader r = reader) {
            return gson.fromJson(r, valueClass);
        }
    }

    @Override
    public <V> List<V> readList(Reader reader, Class<V> valueClass) throws IOException {
        try (Reader r = reader) {
            Type type = TypeToken.getParameterized(List.class, valueClass).getType();
            return gson.fromJson(r, type);
        }
    }

    @Override
    public <V> Set<V> readSet(Reader reader, Class<V> valueClass) throws IOException {
        try (Reader r = reader) {
            Type type = TypeToken.getParameterized(LinkedHashSet.class, valueClass).getType();
            return gson.fromJson(r, type);
        }
    }

    @Override
    public List<Map<String, Object>> readListOfMap(Reader reader) throws IOException {
        try (Reader r = reader) {
            return (List<Map<String, Object>>) gson.fromJson(r, List.class);
        }
    }

    @Override
    public <V> Iterator<V> readListLazy(Reader reader, Class<V> valueClass) throws IOException {
        try (Reader r = reader) {
            JsonReader jsonReader = gson.newJsonReader(r);
            return gson.fromJson(jsonReader, new IteratorParameterizedType<>(valueClass));
        }
    }

    @Override
    public Iterator<Map<String, Object>> readListOfMapLazy(Reader reader) throws IOException {
        try (Reader r = reader) {
            JsonReader jsonReader = gson.newJsonReader(r);
            return gson.fromJson(jsonReader, new IteratorParameterizedType<>(Map.class));
        }
    }

    @Override
    public <K, V> Map<K, V> readMap(Reader reader, Class<K> keyClass, Class<V> valueClass) throws IOException {
        try (Reader r = reader) {
            Type type = new MapParameterizedType<>(keyClass, valueClass);
            return gson.fromJson(r, type);
        }
    }

    // ---------- write ----------

    @Override
    public <V> String writeValue(V obj) {
        return gson.toJson(obj);
    }

    @Override
    public <V> void writeValue(V obj, Writer writer) throws IOException {
        try (Writer w = writer) {
            gson.toJson(obj, w);
        }
    }

    // ---------- convert ----------

    @Override
    public <V> Map<String, Object> convertToMap(V obj) {
        String json = writeValue(obj);
        return readMap(json, String.class, Object.class);
    }

}
