package ru.olegcherednik.gson.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import lombok.RequiredArgsConstructor;
import ru.olegcherednik.gson.utils.type.IteratorParameterizedType;
import ru.olegcherednik.gson.utils.type.MapParameterizedType;
import ru.olegcherednik.json.JsonEngine;

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
public class GsonJsonEngine implements JsonEngine {

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
    public <V> V readValue(Reader reader, Class<V> valueClass) {
        return gson.fromJson(reader, valueClass);
    }

    @Override
    public <V> List<V> readList(Reader reader, Class<V> valueClass) {
        Type type = TypeToken.getParameterized(List.class, valueClass).getType();
        return gson.fromJson(reader, type);
    }

    @Override
    public <V> Set<V> readSet(Reader reader, Class<V> valueClass) {
        Type type = TypeToken.getParameterized(LinkedHashSet.class, valueClass).getType();
        return gson.fromJson(reader, type);
    }

    @Override
    public List<Map<String, Object>> readListOfMap(Reader reader) {
        return (List<Map<String, Object>>) gson.fromJson(reader, List.class);
    }

    @Override
    public <V> Iterator<V> readListLazy(Reader reader, Class<V> valueClass) {
        JsonReader jsonReader = gson.newJsonReader(reader);
        return gson.fromJson(jsonReader, new IteratorParameterizedType<>(valueClass));
    }

    @Override
    public Iterator<Map<String, Object>> readListOfMapLazy(Reader reader) {
        JsonReader jsonReader = gson.newJsonReader(reader);
        return gson.fromJson(jsonReader, new IteratorParameterizedType<>(Map.class));
    }

    @Override
    public <K, V> Map<K, V> readMap(Reader reader, Class<K> keyClass, Class<V> valueClass) {
        Type type = new MapParameterizedType<>(keyClass, valueClass);
        return gson.fromJson(reader, type);
    }

    @Override
    public <V> String writeValue(V obj) {
        return gson.toJson(obj);
    }

    @Override
    public <V> void writeValue(V obj, Writer writer) {
        gson.toJson(obj, writer);
    }

}
