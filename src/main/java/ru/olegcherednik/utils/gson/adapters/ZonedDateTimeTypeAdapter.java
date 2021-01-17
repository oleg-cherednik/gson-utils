package ru.olegcherednik.utils.gson.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

/**
 * @author Oleg Cherednik
 * @since 08.01.2021
 */
public class ZonedDateTimeTypeAdapter extends TypeAdapter<ZonedDateTime> {

    protected final Function<ZoneId, ZoneId> zoneModifier;
    protected final DateTimeFormatter df;

    public ZonedDateTimeTypeAdapter(Function<ZoneId, ZoneId> zoneModifier, DateTimeFormatter df) {
        this.zoneModifier = zoneModifier;
        this.df = df;
    }

    @Override
    public void write(JsonWriter out, ZonedDateTime value) throws IOException {
        if (value == null)
            out.nullValue();
        else {
            ZoneId zone = zoneModifier.apply(value.getZone());
            out.value(df.format(value.withZoneSameInstant(zone)));
        }
    }

    @Override
    public ZonedDateTime read(JsonReader in) throws IOException {
        ZonedDateTime res = null;

        if (in.peek() == JsonToken.NULL)
            in.nextNull();
        else
            res = ZonedDateTime.parse(in.nextString(), df);

        return res;
    }
}
