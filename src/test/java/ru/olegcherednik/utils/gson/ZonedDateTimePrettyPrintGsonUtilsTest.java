package ru.olegcherednik.utils.gson;

import org.testng.annotations.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.olegcherednik.utils.gson.utils.PrettyPrintUtils.UNIX_LINE_SEPARATOR;
import static ru.olegcherednik.utils.gson.utils.PrettyPrintUtils.withUnixLineSeparator;

/**
 * @author Oleg Cherednik
 * @since 08.01.2021
 */
@Test
@SuppressWarnings("NewClassNamingConvention")
public class ZonedDateTimePrettyPrintGsonUtilsTest {

    public void shouldRetrievePrettyPrintJsonUTCZoneWhenWriteZonedDateTimeMapWithPrettyPrint() {
        Map<String, ZonedDateTime> map = ZonedDateTimeGsonUtilsTest.createData();
        String actual = GsonUtils.prettyPrint().writeValue(map);
        assertThat(withUnixLineSeparator(actual)).isEqualTo('{' + UNIX_LINE_SEPARATOR +
                "  \"UTC\": \"2017-07-23T13:57:14.225Z\"," + UNIX_LINE_SEPARATOR +
                "  \"Asia/Singapore\": \"2017-07-23T05:57:14.225Z\"," + UNIX_LINE_SEPARATOR +
                "  \"Australia/Sydney\": \"2017-07-23T03:57:14.225Z\"" + UNIX_LINE_SEPARATOR +
                '}');
    }

    public void shouldRetrievePrettyPrintJsonSingaporeZoneWhenWriteZonedDateTimeMapWithPrettyPrint() {
        GsonDecorator gsonUtils = GsonHelper.createPrettyPrintGsonDecorator(
                new GsonBuilderDecorator().withZone(zone -> ZoneId.of("Asia/Singapore")));

        Map<String, ZonedDateTime> map = ZonedDateTimeGsonUtilsTest.createData();
        String actual = gsonUtils.writeValue(map);
        assertThat(withUnixLineSeparator(actual)).isEqualTo('{' + UNIX_LINE_SEPARATOR +
                "  \"UTC\": \"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\"," + UNIX_LINE_SEPARATOR +
                "  \"Asia/Singapore\": \"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\"," + UNIX_LINE_SEPARATOR +
                "  \"Australia/Sydney\": \"2017-07-23T11:57:14.225+08:00[Asia/Singapore]\"" + UNIX_LINE_SEPARATOR +
                '}');
    }

}
