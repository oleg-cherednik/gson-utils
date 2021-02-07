package ru.olegcherednik.utils.gson;

import com.google.gson.Gson;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import ru.olegcherednik.utils.gson.utils.MapUtils;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.olegcherednik.utils.gson.utils.PrettyPrintUtils.UNIX_LINE_SEPARATOR;
import static ru.olegcherednik.utils.gson.utils.PrettyPrintUtils.withUnixLineSeparator;

/**
 * @author Oleg Cherednik
 * @since 11.01.2021
 */
@Test
public class GsonUtilsHelperTest {

    @AfterMethod
    public void clear() {
        GsonUtilsHelper.setGsonBuilder(null);
    }

    public void shouldUseNewBuilderWhenSetNotNullBuilderToGsonHelper() {
        Map<String, ZonedDateTime> map = createData();
        assertThat(GsonUtils.writeValue(map)).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\"}");
        assertThat(withUnixLineSeparator(GsonUtils.prettyPrint().writeValue(map))).isEqualTo('{' + UNIX_LINE_SEPARATOR +
                "  \"UTC\": \"2017-07-23T13:57:14.225Z\"" + UNIX_LINE_SEPARATOR +
                '}');

        GsonUtilsHelper.setGsonBuilder(new GsonUtilsBuilder().withZoneModifier(zone -> ZoneId.of("Asia/Singapore")));
        assertThat(GsonUtils.writeValue(map)).isEqualTo("{\"UTC\":\"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\"}");
        assertThat(withUnixLineSeparator(GsonUtils.prettyPrint().writeValue(map))).isEqualTo('{' + UNIX_LINE_SEPARATOR +
                "  \"UTC\": \"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\"" + UNIX_LINE_SEPARATOR +
                '}');
    }

    public void shouldNotRebuildMapperWhenSetSameBuilder() {
        Gson expectedGson = GsonUtilsHelper.gson();
        Gson expectedPrettyPrintGson = GsonUtilsHelper.prettyPrintGson();

        GsonUtilsHelper.setGsonBuilder(GsonUtilsHelper.DEFAULT_BUILDER);
        assertThat(GsonUtilsHelper.gson()).isSameAs(expectedGson);
        assertThat(GsonUtilsHelper.prettyPrintGson()).isSameAs(expectedPrettyPrintGson);
    }

    private static Map<String, ZonedDateTime> createData() {
        String str = "2017-07-23T13:57:14.225";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return MapUtils.of("UTC", ZonedDateTime.parse(str, df.withZone(ZoneOffset.UTC)));
    }


}
