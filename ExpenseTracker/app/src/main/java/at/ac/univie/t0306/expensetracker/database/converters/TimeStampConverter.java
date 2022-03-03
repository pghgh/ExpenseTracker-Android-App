package at.ac.univie.t0306.expensetracker.database.converters;

import androidx.room.TypeConverter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is used from the room database to convert the time (OffsetDateTime object) to a string and vise versa, since the SQLLite database do not accepts complex data types like OffsetDateTime
 */
public class TimeStampConverter {

    private DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    /**
     * converts a formatted  string to an OffsetDateTime object
     *
     * @param value
     * @return
     */
    @TypeConverter
    public OffsetDateTime toOffsetDateTime(String value) {
        return formatter.parse(value, OffsetDateTime::from);

    }

    /**
     * converts an OffsetDateTime to a formatted string
     *
     * @param date
     * @return
     */
    @TypeConverter
    public String fromOffsetDateTime(OffsetDateTime date) {
        return date.format(formatter);
    }
}
