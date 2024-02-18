/*
 * Copyright Oleg Cherednik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.olegcherednik.json.gson.factories;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.Test;
import ru.olegcherednik.json.gson.adapters.IteratorTypeAdapter;
import ru.olegcherednik.json.gson.types.AutoCloseableIteratorParameterizedType;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * @author Oleg Cherednik
 * @since 05.01.2024
 */
@Test
public class IteratorTypeAdapterFactoryTest {

    @SuppressWarnings("unchecked")
    public void shouldUseObjectTypeAdapterWhenParametrizedTypeDoesNotHaveElementTypeData()
            throws NoSuchFieldException, IllegalAccessException {
        Gson gson = mock(Gson.class);
        TypeAdapter<Object> elementTypeAdapter = mock(TypeAdapter.class);

        doAnswer(invocation -> {
            TypeToken<Integer> typeToken = invocation.getArgument(0);

            if (typeToken.getRawType() == Object.class && typeToken.getType() == Object.class)
                return elementTypeAdapter;

            return null;
        }).when(gson).getAdapter(any(TypeToken.class));

        TypeToken<Integer> typeToken = (TypeToken<Integer>) TypeToken.get(new IntegerParameterizedType());
        TypeAdapter<Integer> actual = IteratorTypeAdapterFactory.INSTANCE.create(gson, typeToken);

        assertThat(actual).isNotNull().isExactlyInstanceOf(IteratorTypeAdapter.class);

        Field field = IteratorTypeAdapter.class.getDeclaredField("itemTypeAdapter");
        field.setAccessible(true);
        assertThat(field.get(actual)).isSameAs(elementTypeAdapter);
    }

    private static final class IntegerParameterizedType extends AutoCloseableIteratorParameterizedType<Integer> {

        private IntegerParameterizedType() {
            super(Integer.class);
        }

        @Override
        @SuppressWarnings("ZeroLengthArrayAllocation")
        public Type[] getActualTypeArguments() {
            return new Type[0];
        }

    }

}
