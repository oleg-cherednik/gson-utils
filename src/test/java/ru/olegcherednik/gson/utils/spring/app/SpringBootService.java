package ru.olegcherednik.gson.utils.spring.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.olegcherednik.gson.utils.GsonDecorator;
import ru.olegcherednik.gson.utils.dto.Data;

/**
 * @author Oleg Cherednik
 * @since 26.07.2021
 */
@Service
public class SpringBootService {

    @Autowired
    private GsonDecorator gson;

    public String toJson(Data data) {
        return gson.writeValue(data);
    }

    public Data fromJson(String json) {
        return gson.readValue(json, Data.class);
    }

}
