package ru.olegcherednik.utils.gson;

import org.testng.annotations.Test;
import ru.olegcherednik.utils.gson.utils.MapUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public void shouldRetrieveJsonWhenWriteLocalDateTime() throws IOException {
        Map<String, LocalDateTime> map = createData();
        String actual = GsonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"local\":\"2017-07-23T13:57:14.225\"}");
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteLocalDateTimeMapWithPrettyPrint() {
        Map<String, LocalDateTime> map = createData();
        String actual = GsonUtils.prettyPrint().writeValue(map);
        assertThat(withUnixLineSeparator(actual)).isEqualTo('{' + UNIX_LINE_SEPARATOR +
                "  \"local\": \"2017-07-23T13:57:14.225\"" + UNIX_LINE_SEPARATOR +
                '}');
    }

    public void shouldRetrieveDeserializedZonedLocalDateTimeMapWhenReadJsonAsMap() {
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
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder().serializeNulls());
        String json = gson.writeValue(new Data());
        assertThat(json).isEqualTo("{\"notNullValue\":\"2017-07-23T13:57:14.225\",\"nullValue\":null}");
    }

    public void shouldIgnoreNullValueWhenSerializeWithIgnoreNullValue() {
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder());
        String json = gson.writeValue(new Data());
        assertThat(json).isEqualTo("{\"notNullValue\":\"2017-07-23T13:57:14.225\"}");
    }

    public void shouldRetrieveJsonWithCustomFormatWriteSerializeWithCustomFormatter() throws IOException {
        DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd");
        GsonDecorator gson = GsonUtilsHelper.createGsonDecorator(new GsonUtilsBuilder().localDateTimeFormatter(localDateTimeFormatter));
        String json = gson.writeValue(new Data());
        assertThat(json).isEqualTo("{\"notNullValue\":\"13:57:14 2017-07-23\"}");
    }

    private static Map<String, LocalDateTime> createData() {
        String str = "2017-07-23T13:57:14.225";
        return MapUtils.of("local", LocalDateTime.parse(str, ISO_LOCAL_DATE_TIME));
    }

    @SuppressWarnings("unused")
    private static class Data {

        private final LocalDateTime notNullValue = LocalDateTime.parse("2017-07-23T13:57:14.225", ISO_LOCAL_DATE_TIME);
        private final LocalDateTime nullValue = null;
    }

}
