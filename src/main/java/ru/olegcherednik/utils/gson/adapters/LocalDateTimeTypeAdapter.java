package ru.olegcherednik.utils.gson.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 08.01.2021
 */
public class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {

    protected final DateTimeFormatter df;

    public LocalDateTimeTypeAdapter(DateTimeFormatter df) {
        this.df = df;
    }

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null)
            out.nullValue();
        else
            out.value(df.format(value));
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        LocalDateTime res = null;

        if (in.peek() == JsonToken.NULL)
            in.nextNull();
        else
            res = LocalDateTime.parse(in.nextString(), df);

        return res;
    }
}
