package ru.olegcherednik.utils.gson;

import org.testng.annotations.Test;
import ru.olegcherednik.utils.gson.utils.MapUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.olegcherednik.utils.gson.utils.PrettyPrintUtils.UNIX_LINE_SEPARATOR;
import static ru.olegcherednik.utils.gson.utils.PrettyPrintUtils.withUnixLineSeparator;

/**
 * @author Oleg Cherednik
 * @since 08.01.2021
 */
@Test
public class LocalDateTimeGsonUtilsTest {

    public void shouldRetrieveJsonWhenWriteZonedDateTime() throws IOException {
        Map<String, LocalDateTime> map = createData();
        String actual = GsonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"local\":\"2017-07-23T13:57:14.225\"}");
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteZonedDateTimeMapWithPrettyPrint() {
        Map<String, LocalDateTime> map = createData();
        String actual = GsonUtils.prettyPrint().writeValue(map);
        assertThat(withUnixLineSeparator(actual)).isEqualTo('{' + UNIX_LINE_SEPARATOR +
                "  \"local\": \"2017-07-23T13:57:14.225\"" + UNIX_LINE_SEPARATOR +
                '}');
    }

    public void shouldRetrieveDeserializedZonedDateTimeMapWhenReadJsonAsMap() {
        String json = "{\"local\":\"2017-07-23T13:57:14.225\"}";
        Map<String, LocalDateTime> expected = createData();
        Map<String, LocalDateTime> actual = GsonUtils.readMap(json, String.class, LocalDateTime.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldReadNullableValueWhenListContainsNull() {
        String json = "[null,\"2017-07-23T13:57:14.225\"]";
        List<LocalDateTime> actual = GsonUtils.readList(json, LocalDateTime.class);

        assertThat(actual).hasSize(2);
        assertThat(actual.get(0)).isNull();
        assertThat(actual.get(1)).isEqualTo(LocalDateTime.parse("2017-07-23T13:57:14.225", ISO_LOCAL_DATE_TIME));
    }

    public void shouldWriteNullWhenSerializeWithNullValue() {
        GsonDecorator gson = GsonHelper.createGsonDecorator(new GsonBuilderDecorator().serializeNulls());
        String json = gson.writeValue(new Data());
        assertThat(json).isEqualTo("{\"notNullValue\":\"2017-07-23T13:57:14.225\",\"nullValue\":null}");
    }

    public void shouldIgnoreNullValueWhenSerializeWithIgnoreNullValue() {
        GsonDecorator gson = GsonHelper.createGsonDecorator(new GsonBuilderDecorator());
        String json = gson.writeValue(new Data());
        assertThat(json).isEqualTo("{\"notNullValue\":\"2017-07-23T13:57:14.225\"}");
    }

    private static Map<String, LocalDateTime> createData() {
        String str = "2017-07-23T13:57:14.225";
        return MapUtils.of("local", LocalDateTime.parse(str, ISO_LOCAL_DATE_TIME));
    }

    @SuppressWarnings("unused")
    private static class Data {

        final LocalDateTime notNullValue = LocalDateTime.parse("2017-07-23T13:57:14.225", ISO_LOCAL_DATE_TIME);
        final LocalDateTime nullValue = null;
    }

}
