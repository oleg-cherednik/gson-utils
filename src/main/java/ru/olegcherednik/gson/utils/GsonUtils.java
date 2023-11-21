/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ru.olegcherednik.gson.utils;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
public final class GsonUtils {

    private static final GsonDecorator DELEGATE = new GsonDecorator(GsonUtilsHelper::gson);
    private static final GsonDecorator PRETTY_PRINT_DELEGATE = new GsonDecorator(GsonUtilsHelper::prettyPrintGson);

    // ---------- read String ----------

    public static <V> V readValue(String json, Class<V> valueClass) {
        return print().readValue(json, valueClass);
    }

    public static List<Object> readList(String json) {
        return print().readList(json);
    }

    public static <V> List<V> readList(String json, Class<V> valueClass) {
        return print().readList(json, valueClass);
    }

    public static List<Map<String, Object>> readListOfMap(String json) {
        return print().readListOfMap(json);
    }

    public static Map<String, Object> readMap(String json) {
        return print().readMap(json);
    }

    public static <V> Map<String, V> readMap(String json, Class<V> valueClass) {
        return print().readMap(json, valueClass);
    }

    public static <K, V> Map<K, V> readMap(String json, Class<K> keyClass, Class<V> valueClass) {
        return print().readMap(json, keyClass, valueClass);
    }

    // ---------- read Reader ----------

    public static <V> V read(Reader in, Type type) {
        return print().read(in, type);
    }

    public static <V> V readValue(Reader in, Class<V> valueClass) {
        return print().readValue(in, valueClass);
    }

    public static List<Object> readList(Reader in) {
        return print().readList(in);
    }

    public static <V> List<V> readList(Reader in, Class<V> valueClass) {
        return print().readList(in, valueClass);
    }

    public static Iterator<Object> readListLazy(Reader in) {
        return print().readListLazy(in);
    }

    public static <V> Iterator<V> readListLazy(Reader in, Class<V> valueClass) {
        return print().readListLazy(in, valueClass);
    }

    public static Map<String, Object> readMap(Reader in) {
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
