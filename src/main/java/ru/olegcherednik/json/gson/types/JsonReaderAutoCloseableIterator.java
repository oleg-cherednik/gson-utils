/*
 * Copyright Oleg Cherednik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.olegcherednik.json.gson.types;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import ru.olegcherednik.json.api.AutoCloseableIterator;

/**
 * @param <V> Type of the value object
 * @author Oleg Cherednik
 * @since 05.01.2024
 */
public class JsonReaderAutoCloseableIterator<V> extends JsonReaderIterator<V> implements AutoCloseableIterator<V> {

    public JsonReaderAutoCloseableIterator(JsonReader in, TypeAdapter<V> typeAdapter) {
        super(in, typeAdapter);
    }

    @Override
    public void close() throws Exception {
        in.close();
    }

}
