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

    protected final DateTimeFormatter dateFormatFormatter;

    public LocalDateTimeTypeAdapter(DateTimeFormatter dateFormatFormatter) {
        this.dateFormatFormatter = dateFormatFormatter;
    }

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            if (out.getSerializeNulls())
                out.nullValue();
        } else
            out.value(dateFormatFormatter.format(value));
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        return LocalDateTime.parse(in.nextString(), dateFormatFormatter);
    }
}
