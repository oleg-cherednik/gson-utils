package ru.olegcherednik.gson.utils.spring.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.olegcherednik.gson.utils.GsonDecorator;
import ru.olegcherednik.gson.utils.GsonUtilsBuilder;
import ru.olegcherednik.gson.utils.GsonUtilsHelper;
import ru.olegcherednik.gson.utils.dto.Data;

/**
 * @author Oleg Cherednik
 * @since 26.07.2021
 */
@Configuration
public class SpringBootConfig {

    @Bean
    public GsonUtilsBuilder gsonUtilsBuilder() {
        return new GsonUtilsBuilder()
                .addCustomizer(gsonBuilder -> gsonBuilder.registerTypeAdapter(Data.class, new DataTypeAdapter()));
    }

    @Bean
    public GsonDecorator gsonDecorator(GsonUtilsBuilder gsonUtilsBuilder) {
        return GsonUtilsHelper.createGsonDecorator(gsonUtilsBuilder);
    }

}
