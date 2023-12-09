package ru.olegcherednik.json.gson.utils.datetime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.gson.utils.ResourceData;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 01.12.2023
 */
@Test
public class DateTimeCombinationTest {

    public void shouldWriteDatesWithDefaultJsonSettings() throws IOException {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse("2023-12-03T10:39:20.187+03:00");
        DataOne data = new DataOne(zonedDateTime);

        System.out.println(Json.writer().writeValue(data));

        String actual = Json.writeValue(data);
        String expected = ResourceData.getResourceAsString("/datetime/def_date_one.json.txt").trim();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldReadDatesWithDefaultJsonSettings() throws IOException {
        String json = ResourceData.getResourceAsString("/datetime/def_date_one.json.txt").trim();
        ZonedDateTime zonedDateTime = ZonedDateTime.parse("2023-12-03T10:39:20.187+03:00");

        DataOne actual = Json.readValue(json, DataOne.class);
        DataOne expected = new DataOne(zonedDateTime);
        assertThat(actual).isEqualTo(expected);
    }

//    public void shouldSerializeDatesWithAnnotationSettingsPreferable() throws JsonProcessingException {
//        JsonSettings jsonSettings = JsonSettings.builder()
//                                                .zoneModifier(ZoneModifier.CONVERT_TO_UTC)
//                                                .instantFormatter(DateTimeFormatter.ofPattern("'[one] 'yyyy-MM-dd'T'HH:mm:ss.SSSz"))
//                                                .localTimeFormatter(DateTimeFormatter.ofPattern("'[one] 'HH:mm:ss.SSS"))
//                                                .localDateFormatter(DateTimeFormatter.ofPattern("'[one] 'yyyy-MM-dd"))
//                                                .offsetTimeFormatter(DateTimeFormatter.ofPattern("'[one] 'HH:mm:ss.SSSz"))
//                                                .offsetDateTimeFormatter(DateTimeFormatter.ofPattern("'[one] 'yyyy-MM-dd'T'HH:mm:ss.SSSz"))
//                                                .build();
//
//        ZonedDateTime zonedDateTime = ZonedDateTime.parse("2023-12-03T10:39:20.187+03:00");
//        DataTwo data = new DataTwo(zonedDateTime);
//
//        String actual = Json.createWriter(jsonSettings).writeValue(data);
//        assertThat(actual).isEqualTo("{\"instant\":\"[two] 2023-12-03T15:39:20.187SGT\","
//                                             + "\"localTime\":\"[two] 10:39:20.187\","
//                                             + "\"localDate\":\"[two] 2023-12-03\","
//                                             + "\"localDateTime\":\"[two] 2023-12-03T10:39:20.187\","
//                                             + "\"offsetTime\":\"[two] 15:39:20.187SGT\","
//                                             + "\"offsetDateTime\":\"[two] 2023-12-03T15:39:20.187SGT\""
//                                             + ",\"zonedDateTime\":\"[two] 2023-12-03T15:39:20.187SGT\""
//                                             + ",\"date\":\"[two] 2023-12-03T15:39:20.187SGT\"}");
//        // 2023-12-03T07:39:20.187 Z
//        // 2023-12-03T10:39:20.187 MSK
//        // 2023-12-03T15:39:20.187 SGT
//    }

//    public void shouldSerializeDatesWithAnnotationSettingsPreferable1() throws JsonProcessingException {
////        DateTimeFormatter df1 = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
////        DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
////        DateTimeFormatter df3 = df2.withZone(ZoneOffset.UTC);
//
//        JsonSettings jsonSettings = JsonSettings.builder()
//                                                .zoneModifier(ZoneModifier.CONVERT_TO_UTC)
////                                           .instantFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
////                                           .localDateFormatter(DateTimeFormatter.ISO_DATE)
////                                           .localTimeFormatter(DateTimeFormatter.ofPattern("HH:mm:ss"))
////                                           .dateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
////                                           .offsetTimeFormatter(DateTimeFormatter.ISO_OFFSET_TIME)
////                                           .offsetDateTimeFormatter(df2.withZone(ZoneId.systemDefault()))
////                                           .zonedDateTimeFormatter(df2)
//                                                .build();
//
//        ZonedDateTime zonedDateTime = ZonedDateTime.parse("2023-12-03T10:39:20.187+03:00");
//        DataTwo data = new DataTwo(zonedDateTime);
//
////        String actual = Json.writeValue(data);
//
////        DataTwo data = new DataTwo(ZonedDateTime.parse("2023-12-03T07:39:20.187Z"));
//
////        String actual = Json.writeValue(data);
////        assertThat(actual).isEqualTo("{\"instant\":\"2023-12-03T10:39:20.187000+03:00\","
////                                             + "\"localTime\":\"07:39:20.187\","
////                                             + "\"localDate\":\"2023-12-03\","
////                                             + "\"localDateTime\":\"2023-12-03T07:39:20.187\","
////                                             + "\"offsetTime\":\"07:39:20.187Z\""
////                                             + ",\"offsetDateTime\":\"2023-12-03T07:39:20.187Z\""
////                                             + ",\"zonedDateTime\":\"2023-12-03T07:39:20.187Z\""
////                                             + ",\"date\":\"2023-12-03T10:39:20.187000+03:00\"}");
//
////        System.out.println(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(data));
//        System.out.println(Json.createPrettyPrintWriter(jsonSettings).writeValue(data));
//        // 2023-12-03T07:39:20.187 Z
//        // 2023-12-03T10:39:20.187 MSK
//        // 2023-12-03T15:39:20.187 SGT
//    }

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
            instant = zonedDateTime.toInstant();
            localDate = zonedDateTime.toLocalDate();
            localTime = zonedDateTime.toLocalTime();
            localDateTime = zonedDateTime.toLocalDateTime();
            offsetDateTime = zonedDateTime.toOffsetDateTime();
            offsetTime = offsetDateTime.toOffsetTime();
            this.zonedDateTime = zonedDateTime;
            date = Date.from(instant);
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
