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

package ru.olegcherednik.json.gson;

import com.google.gson.ToNumberStrategy;
import com.google.gson.stream.JsonReader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author Oleg Cherednik
 * @since 01.12.2023
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DynamicToNumberStrategy implements ToNumberStrategy {

    public static final DynamicToNumberStrategy INSTANCE = new DynamicToNumberStrategy();

    @Override
    public Number readNumber(JsonReader in) throws IOException {
        String str = in.nextString();
        double val = Double.parseDouble(str);

        if (str.indexOf('.') >= 0)
            return val;
        if (Double.compare((int) val, val) == 0)
            return (int) val;
        if (Double.compare((long) val, val) == 0)
            return (long) val;

        return new BigInteger(str);
    }

}
