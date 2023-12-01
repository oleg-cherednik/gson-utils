package ru.olegcherednik.gson.utils;

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

        if (str.indexOf('.') >= 0 || str.indexOf(',') >= 0)
            return val;
        if (Double.compare((int) val, val) == 0)
            return (int) val;
        if (Double.compare((long) val, val) == 0)
            return (long) val;

        try {
            return new BigInteger(str);
        } catch (NumberFormatException ignore) {
            return val;
        }
    }

}
