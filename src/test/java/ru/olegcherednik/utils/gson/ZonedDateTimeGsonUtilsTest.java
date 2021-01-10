package ru.olegcherednik.utils.gson;

import org.testng.annotations.Test;
import ru.olegcherednik.utils.gson.utils.MapUtils;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 08.01.2021
 */
@Test
public class ZonedDateTimeGsonUtilsTest {

    public void shouldRetrieveJsonUTCZoneWhenWriteZonedDateTimeDefaultSettings() {
        Map<String, ZonedDateTime> map = createData();
        String actual = GsonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\"," +
                "\"Asia/Singapore\":\"2017-07-23T05:57:14.225Z\"," +
                "\"Australia/Sydney\":\"2017-07-23T03:57:14.225Z\"}");
    }

    public void shouldRetrieveJsonSingaporeZoneWhenWriteZonedDateTimeSingaporeZone() throws IOException {
        GsonDecorator gsonUtils = GsonHelper.createGsonDecorator(
                new GsonBuilderDecorator().withZone(zone -> ZoneId.of("Asia/Singapore")));

        Map<String, ZonedDateTime> map = createData();
        String actual = gsonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Australia/Sydney\":\"2017-07-23T11:57:14.225+08:00[Asia/Singapore]\"}");
    }

    public void shouldRetrieveJsonWithNoZoneChangeWhenWriteZonedDateTimeWithSameZone() throws IOException {
        GsonDecorator gsonUtils = GsonHelper.createGsonDecorator(
                new GsonBuilderDecorator().withZone(GsonBuilderDecorator.WITH_NO_CHANGE_ZONE));

        Map<String, ZonedDateTime> map = createData();
        String actual = gsonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\"," +
                "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Australia/Sydney\":\"2017-07-23T13:57:14.225+10:00[Australia/Sydney]\"}");
    }

    public void shouldRetrieveDeserializedZonedDateTimeMapWhenReadJsonAsMap() {
        String json = "{\"UTC\":\"2017-07-23T13:57:14.225Z\"," +
                "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\"," +
                "\"Australia/Sydney\":\"2017-07-23T13:57:14.225+10:00[Australia/Sydney]\"}";
        Map<String, ZonedDateTime> expected = createData();
        Map<String, ZonedDateTime> actual = GsonUtils.readMap(json, String.class, ZonedDateTime.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    static Map<String, ZonedDateTime> createData() {
        String str = "2017-07-23T13:57:14.225";

        return MapUtils.of(
                "UTC", ZonedDateTime.parse(str, ISO_LOCAL_DATE_TIME.withZone(ZoneOffset.UTC)),
                "Asia/Singapore", ZonedDateTime.parse(str, ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("Asia/Singapore"))),
                "Australia/Sydney", ZonedDateTime.parse(str, ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("Australia/Sydney"))));
    }

}
