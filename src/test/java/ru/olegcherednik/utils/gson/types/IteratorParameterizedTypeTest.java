package ru.olegcherednik.utils.gson.types;

import org.testng.annotations.Test;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 11.01.2021
 */
@Test
public class IteratorParameterizedTypeTest {

    public void shouldRetrieveGivenClassesWhenCreateNewInstance() {
        IteratorParameterizedType<String> type = new IteratorParameterizedType<>(String.class);
        assertThat(type.getActualTypeArguments()).containsExactly(String.class);
        assertThat(type.getRawType()).isSameAs(Iterator.class);
        assertThat(type.getOwnerType()).isNull();
    }

}
