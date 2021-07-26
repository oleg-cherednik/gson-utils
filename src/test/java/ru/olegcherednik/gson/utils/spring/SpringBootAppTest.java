package ru.olegcherednik.gson.utils.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.olegcherednik.gson.utils.GsonUtils;
import ru.olegcherednik.gson.utils.dto.Data;
import ru.olegcherednik.gson.utils.spring.app.DataTypeAdapter;
import ru.olegcherednik.gson.utils.spring.app.SpringBootApp;
import ru.olegcherednik.gson.utils.spring.app.SpringBootService;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 26.07.2021
 */
@Test
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootApp.class)
public class SpringBootAppTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private SpringBootService service;

    public void shouldWriteObjectToJsonStringUsingCustomBuilder() {
        Data data = new Data(666, "oleg");
        String json = service.toJson(data);

        System.out.println(json);

        Map<String, Object> expected = new HashMap<>();
        expected.put(DataTypeAdapter.FIELD_INT, 666);
        expected.put(DataTypeAdapter.FIELD_STRING, "oleg");

        Map<String, ?> actual = GsonUtils.readMap(json);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldReadJsonUsingCustomBuilder() {
        String json = "{\"int\":666,\"str\":\"oleg\"}\n";
        Data actual = service.fromJson(json);
        assertThat(actual.getIntVal()).isEqualTo(666);
        assertThat(actual.getStrVal()).isEqualTo("oleg");
    }

}
