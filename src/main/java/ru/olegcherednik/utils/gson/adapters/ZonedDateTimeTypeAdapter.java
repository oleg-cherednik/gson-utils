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
    protected final DateTimeFormatter dateTimeFormatter;

    public ZonedDateTimeTypeAdapter(Function<ZoneId, ZoneId> zoneModifier, DateTimeFormatter dateTimeFormatter) {
        this.zoneModifier = zoneModifier;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @Override
    public void write(JsonWriter out, ZonedDateTime value) throws IOException {
        if (value == null) {
            if (out.getSerializeNulls())
                out.nullValue();
        } else {
            ZoneId zone = zoneModifier.apply(value.getZone());
            out.value(dateTimeFormatter.format(value.withZoneSameInstant(zone)));
        }
    }

    @Override
    public ZonedDateTime read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        return ZonedDateTime.parse(in.nextString(), dateTimeFormatter);
    }
}
