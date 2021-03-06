package com.apptec.camello.repository.localdatabase.converter;

import androidx.room.TypeConverter;

import com.apptec.camello.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * This class is used only for convert date to timestamp and solve some problems
 */
public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Long toTimestamp(String stringDate) {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.PATTERN_DATE_FORMAT);

            Date date = dateFormat.parse(stringDate);
            return toTimestamp(date);
        } catch (ParseException p) {
            Timber.e(p);
            return null;
        }
    }


    @TypeConverter
    public static String toStringDateFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.PATTERN_DATE_FORMAT);
        return dateFormat.format(date);
    }


    @TypeConverter
    public static String toStringDateFormat(Long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.PATTERN_DATE_FORMAT);
        return dateFormat.format(new Date(timestamp));
    }

    public static String toStringDateFormat(Long timestamp, String custom_pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(custom_pattern);
        return dateFormat.format(new Date(timestamp));
    }


}
