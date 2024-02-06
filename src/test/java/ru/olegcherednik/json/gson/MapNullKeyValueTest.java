package ru.olegcherednik.json.gson;

import ru.olegcherednik.json.api.Json;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 06.02.2024
 */
// TODO should be fixed, because Jackson works with this
// @Test
public class MapNullKeyValueTest {

    public void shouldSerializeNulWhenWriteMap() {
        Map<String, String> expected = MapUtils.of("one", "1",
                                                   "two", null);
        String json = Json.writeValue(expected);
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo("{\"one\":\"1\",\"two\":null}");

        Map<String, String> actual = Json.readMap(json, String.class, String.class);
        assertThat(actual).isEqualTo(expected);

    }

}
