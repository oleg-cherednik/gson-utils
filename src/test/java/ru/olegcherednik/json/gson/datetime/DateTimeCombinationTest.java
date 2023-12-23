package ru.olegcherednik.json.gson.datetime;

import com.google.gson.Gson;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.api.ZoneModifier;
import ru.olegcherednik.json.gson.ResourceData;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 01.12.2023
 */
@Test
public class DateTimeCombinationTest {

    public void shouldWriteDatesWithDefaultJsonSettings() throws IOException {
        DataOne data = new DataOne(ZonedDateTime.parse("2023-12-03T10:39:20.187+03:00"));

        String actual = Json.writeValue(data);
        String expected = ResourceData.getResourceAsString("/datetime/def_date_one.json").trim();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldReadDatesWithDefaultJsonSettings() throws IOException {
        ZonedDateTime zdt = ZonedDateTime.parse("2023-12-03T10:39:20.187+03:00");

        String json = ResourceData.getResourceAsString("/datetime/def_date_one.json").trim();
        DataOne actual = Json.readValue(json, DataOne.class);
        DataOne expected = new DataOne(zdt.toInstant(),
                                       zdt.toLocalDate(),
                                       zdt.toLocalTime(),
                                       zdt.toLocalDateTime(),
                                       zdt.toOffsetDateTime().toOffsetTime(),
                                       zdt.toOffsetDateTime(),
                                       zdt.withZoneSameInstant(ZoneId.systemDefault()),
                                       Date.from(zdt.toInstant()));
        assertThat(actual).isEqualTo(expected);
    }

//    public void shouldWriteDatesWithAnnotationSettingsPreferable() throws IOException {
//        DataTwo data = new DataTwo(ZonedDateTime.parse("2023-12-03T10:39:20.187+03:00"));
//        String actual = Json.createWriter(getSettings()).writeValue(data);
//        String expected = ResourceData.getResourceAsString("/datetime/custom_date_one.json").trim();
//        assertThat(actual).isEqualTo(expected);
//    }

//    public void shouldReadDatesWithAnnotationSettingsPreferable() throws IOException {
//        ZonedDateTime zdtLocal = ZonedDateTime.parse("2023-12-03T10:39:20.187+03:00");
//        ZonedDateTime zdtSingapore = zdtLocal.withZoneSameInstant(LocalZoneId.ASIA_SINGAPORE);
//
//        String json = ResourceData.getResourceAsString("/datetime/custom_date_one.json").trim();
//
//        DataTwo actual = Json.createReader(getSettings()).readValue(json, DataTwo.class);
//        DataTwo expected = new DataTwo(zdtLocal.toInstant(),
//                                       zdtLocal.toLocalDate(),
//                                       zdtLocal.toLocalTime(),
//                                       zdtLocal.toLocalDateTime(),
//                                       zdtSingapore.toOffsetDateTime().toOffsetTime(),
//                                       zdtSingapore.toOffsetDateTime(),
//                                       zdtSingapore,
//                                       Date.from(zdtLocal.toInstant()));
//
//
//        assertThat(actual).isEqualTo(expected);
//    }

    private static JsonSettings getSettings() {
        return JsonSettings.builder()
                           .zoneModifier(ZoneModifier.CONVERT_TO_UTC)
                           .instantFormatter(DateTimeFormatter.ofPattern("'[one] 'yyyy-MM-dd'T'HH:mm:ss.SSSz"))
                           .localTimeFormatter(DateTimeFormatter.ofPattern("'[one] 'HH:mm:ss.SSS"))
                           .localDateFormatter(DateTimeFormatter.ofPattern("'[one] 'yyyy-MM-dd"))
                           .offsetTimeFormatter(DateTimeFormatter.ofPattern("'[one] 'HH:mm:ss.SSSz"))
                           .offsetDateTimeFormatter(DateTimeFormatter.ofPattern("'[one] 'yyyy-MM-dd'T'HH:mm:ss.SSSz"))
                           .zonedDateTimeFormatter(DateTimeFormatter.ofPattern("'[one] 'yyyy-MM-dd'T'HH:mm:ss.SSSz"))
                           .build();
    }

    @Getter
    @EqualsAndHashCode
    @SuppressWarnings({ "FieldCanBeLocal", "unused" })
    private static final class DataOne {

        private Instant instant;
        private LocalDate localDate;
        private LocalTime localTime;
        private LocalDateTime localDateTime;
        private OffsetTime offsetTime;
        private OffsetDateTime offsetDateTime;
        private ZonedDateTime zonedDateTime;
        private Date date;

        public DataOne(ZonedDateTime zonedDateTime) {
            this(zonedDateTime.toInstant(),
                 zonedDateTime.toLocalDate(),
                 zonedDateTime.toLocalTime(),
                 zonedDateTime.toLocalDateTime(),
                 zonedDateTime.toOffsetDateTime().toOffsetTime(),
                 zonedDateTime.toOffsetDateTime(),
                 zonedDateTime,
                 Date.from(zonedDateTime.toInstant()));
        }

        public DataOne(Instant instant,
                       LocalDate localDate,
                       LocalTime localTime,
                       LocalDateTime localDateTime,
                       OffsetTime offsetTime,
                       OffsetDateTime offsetDateTime,
                       ZonedDateTime zonedDateTime,
                       Date date) {
            this.instant = instant;
            this.localDate = localDate;
            this.localTime = localTime;
            this.localDateTime = localDateTime;
            this.offsetTime = offsetTime;
            this.offsetDateTime = offsetDateTime;
            this.zonedDateTime = zonedDateTime;
            this.date = new Date(date.getTime());
        }

    }

//    @Getter
//    private static final class DataTwo {
//
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'yyyy-MM-dd'T'HH:mm:ss.SSSz", timezone = "Asia/Singapore")
//        private final Instant instant;
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'HH:mm:ss.SSS")
//        private final LocalTime localTime;
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'yyyy-MM-dd")
//        private final LocalDate localDate;
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'yyyy-MM-dd'T'HH:mm:ss.SSS")
//        private final LocalDateTime localDateTime;
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'HH:mm:ss.SSSz", timezone = "Asia/Singapore")
//        private final OffsetTime offsetTime;
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'yyyy-MM-dd'T'HH:mm:ss.SSSz", timezone = "Asia/Singapore")
//        private final OffsetDateTime offsetDateTime;
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'yyyy-MM-dd'T'HH:mm:ss.SSSz", timezone = "Asia/Singapore")
//        private final ZonedDateTime zonedDateTime;
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'yyyy-MM-dd'T'HH:mm:ss.SSSz", timezone = "Asia/Singapore")
//        private final Date date;
//
//        public DataTwo(ZonedDateTime zonedDateTime) {
//            instant = zonedDateTime.toInstant();
//            localTime = zonedDateTime.toLocalTime();
//            localDate = zonedDateTime.toLocalDate();
//            localDateTime = zonedDateTime.toLocalDateTime();
//            offsetDateTime = zonedDateTime.toOffsetDateTime();
//            offsetTime = zonedDateTime.toOffsetDateTime().toOffsetTime();
//            this.zonedDateTime = zonedDateTime;
//            date = Date.from(instant);
//        }
//
//    }

}
