package ru.olegcherednik.json.gson.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import lombok.RequiredArgsConstructor;
import ru.olegcherednik.json.api.JsonException;
import ru.olegcherednik.json.api.iterator.AutoCloseableIterator;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * @author Oleg Cherednik
 * @since 04.01.2024
 */
@RequiredArgsConstructor
public class JsonReaderIterator<V> implements AutoCloseableIterator<V> {

    protected final JsonReader in;
    protected final TypeAdapter<V> typeAdapter;

    @Override
    public boolean hasNext() {
        try {
            return in.hasNext();
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public V next() {
        try {
            if (!hasNext())
                throw new NoSuchElementException();
            return typeAdapter.read(in);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void close() throws Exception {
        in.close();
    }
}
