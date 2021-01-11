package ru.olegcherednik.utils.gson.types;

import org.testng.annotations.Test;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 11.01.2021
 */
@Test
public class MapParameterizedTypeTest {

    public void shouldRetrieveGivenClassesWhenCreateNewInstance() {
        MapParameterizedType<String, Integer> type = new MapParameterizedType<>(String.class, Integer.class);
        assertThat(type.getActualTypeArguments()).containsExactly(String.class, Integer.class);
        assertThat(type.getRawType()).isSameAs(LinkedHashMap.class);
        assertThat(type.getOwnerType()).isNull();
    }

}
