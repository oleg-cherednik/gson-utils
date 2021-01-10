package ru.olegcherednik.utils.gson.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import static com.google.gson.stream.JsonToken.NUMBER;

/**
 * @author Oleg Cherednik
 * @since 09.01.2021
 */
public class CustomObjectTypeAdapter extends TypeAdapter<Object> {

    protected final Gson gson;
    protected final TypeAdapter<Object> delegate;

    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            TypeAdapter<Object> delegate = (TypeAdapter<Object>)ObjectTypeAdapter.FACTORY.<T>create(gson, type);
            return delegate == null ? null : (TypeAdapter<T>)new CustomObjectTypeAdapter(gson, delegate);
        }
    };

    public CustomObjectTypeAdapter(Gson gson, TypeAdapter<Object> delegate) {
        this.gson = gson;
        this.delegate = delegate;
    }

    @Override
    public void write(JsonWriter out, Object value) throws IOException {
        delegate.write(out, value);
    }

    @Override
    public Object read(JsonReader in) throws IOException {
        JsonToken token = in.peek();

        if (token != NUMBER)
            return delegate.read(in);

        String str = in.nextString();

        try {
            return Integer.parseInt(str);
        } catch(NumberFormatException ignored) {
        }

        try {
            return Long.parseLong(str);
        } catch(NumberFormatException ignored) {
        }

        return Double.parseDouble(str);
    }

}
