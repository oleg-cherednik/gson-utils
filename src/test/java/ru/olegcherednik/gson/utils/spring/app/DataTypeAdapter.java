package ru.olegcherednik.gson.utils.spring.app;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ru.olegcherednik.gson.utils.dto.Data;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 26.07.2021
 */
public class DataTypeAdapter extends TypeAdapter<Data> {

    public static final String NULL = "<null_data>";
    public static final String FIELD_INT = "int";
    public static final String FIELD_STRING = "str";

    @Override
    public void write(JsonWriter out, Data data) throws IOException {
        if (data == null)
            out.value(NULL);
        else {
            int intVal = data.getIntVal();
            String strVal = data.getStrVal();

            out.beginObject();
            out.name(FIELD_INT).value(intVal);
            out.name(FIELD_STRING).value(strVal);
            out.endObject();
        }
    }

    @Override
    public Data read(JsonReader in) throws IOException {
        Data data = null;

        if (in.peek() == JsonToken.NULL)
            assertThat(in.nextString()).isEqualTo(NULL);
        else {
            in.beginObject();
            in.nextName();
            int intVal = in.nextInt();
            in.nextName();
            String strVal = in.nextString();
            data = new Data(intVal, strVal);
            in.endObject();
        }

        return data;
    }

}
