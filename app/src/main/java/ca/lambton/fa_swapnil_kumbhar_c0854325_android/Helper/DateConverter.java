package ca.lambton.fa_swapnil_kumbhar_c0854325_android.Helper;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    public static String getFullDate(Date date){
        SimpleDateFormat simpleFormat = new SimpleDateFormat("MMMM dd, yyyy");
        return simpleFormat.format(date.getTime());
    }
}