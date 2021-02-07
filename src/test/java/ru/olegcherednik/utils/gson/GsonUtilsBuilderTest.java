package ru.olegcherednik.utils.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Excluder;
import org.testng.annotations.Test;
import ru.olegcherednik.utils.gson.data.Data;
import ru.olegcherednik.utils.reflection.FieldUtils;

import java.lang.reflect.Modifier;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

/**
 * @author Oleg Cherednik
 * @since 17.01.2021
 */
@Test
@SuppressWarnings("LocalVariableNamingConvention")
public class GsonUtilsBuilderTest {

    public void shouldThrowGsonUtilsExceptionWhenPostCreateThrowsException() {
        GsonUtilsBuilder builder = new GsonUtilsBuilder();

        assertThatThrownBy(() -> builder.postCreate(null))
                .isExactlyInstanceOf(GsonUtilsException.class)
                .hasCauseInstanceOf(NullPointerException.class);
    }

    public void shouldDelegateSettingsToGsonBuilderWhenUseDecorator() throws Exception {
        ExclusionStrategy exclusionStrategy = mock(ExclusionStrategy.class);
        ExclusionStrategy serializationExclusionStrategy = mock(ExclusionStrategy.class);
        ExclusionStrategy deserializationExclusionStrategy = mock(ExclusionStrategy.class);

        TypeAdapter<Data> typeAdapter = (TypeAdapter<Data>)mock(TypeAdapter.class);
        FieldNamingStrategy fieldNamingStrategy = mock(FieldNamingStrategy.class);

        GsonUtilsBuilder builder = new GsonUtilsBuilder()
                .setVersion(666.66)
                .excludeFieldsWithModifiers(Modifier.PUBLIC | Modifier.PROTECTED)
                .generateNonExecutableJson()
                .excludeFieldsWithoutExposeAnnotation()
                .disableInnerClassSerialization()
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DOTS)
                .setFieldNamingStrategy(fieldNamingStrategy)
                .setExclusionStrategies(exclusionStrategy)
                .addSerializationExclusionStrategy(serializationExclusionStrategy)
                .addDeserializationExclusionStrategy(deserializationExclusionStrategy)
                .setLenient()
                .disableHtmlEscaping()
                .registerTypeAdapter(Data.class, typeAdapter)
                .registerTypeAdapterFactory(mock(TypeAdapterFactory.class))
                .registerTypeHierarchyAdapter(Data.class, typeAdapter)
                .serializeSpecialFloatingPointValues();

        Gson gson = builder.gson();

        Excluder excluder = FieldUtils.getFieldValue(gson, "excluder");
        List<ExclusionStrategy> serializationStrategies = FieldUtils.getFieldValue(excluder, "serializationStrategies");
        List<ExclusionStrategy> deserializationStrategies = FieldUtils.getFieldValue(excluder, "deserializationStrategies");

        assertThat(FieldUtils.<Double>getFieldValue(excluder, "version")).isEqualTo(666.66);
        assertThat(FieldUtils.<Integer>getFieldValue(excluder, "modifiers")).isEqualTo(Modifier.PUBLIC | Modifier.PROTECTED);
        assertThat(FieldUtils.<Boolean>getFieldValue(excluder, "requireExpose")).isTrue();
        assertThat(FieldUtils.<Boolean>getFieldValue(excluder, "serializeInnerClasses")).isFalse();
        assertThat(serializationStrategies).containsExactly(exclusionStrategy, serializationExclusionStrategy);
        assertThat(deserializationStrategies).containsExactly(exclusionStrategy, deserializationExclusionStrategy);

        assertThat(FieldUtils.<Boolean>getFieldValue(gson, "generateNonExecutableJson")).isTrue();
        assertThat(FieldUtils.<LongSerializationPolicy>getFieldValue(gson, "longSerializationPolicy")).isEqualTo(LongSerializationPolicy.STRING);
        assertThat(FieldUtils.<Boolean>getFieldValue(gson, "lenient")).isTrue();
        assertThat(FieldUtils.<Boolean>getFieldValue(gson, "htmlSafe")).isFalse();
        assertThat(FieldUtils.<Boolean>getFieldValue(gson, "serializeSpecialFloatingPointValues")).isTrue();
    }

}
