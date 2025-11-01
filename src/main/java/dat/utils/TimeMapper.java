package dat.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String LocalDateTimeToString(LocalDateTime LDT){
        return LDT.format(formatter);
    }

    public static LocalDateTime StringToLocalDateTime(String time){
        return LocalDateTime.parse(time, formatter);
    }
}
