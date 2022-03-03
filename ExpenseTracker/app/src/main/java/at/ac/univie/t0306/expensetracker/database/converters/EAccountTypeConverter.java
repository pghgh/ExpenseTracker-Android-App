package at.ac.univie.t0306.expensetracker.database.converters;

import androidx.room.TypeConverter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import at.ac.univie.t0306.expensetracker.database.data.EAccountType;

public class EAccountTypeConverter {


    @TypeConverter
    public EAccountType toEAccountType(String value) {
        return EAccountType.valueOf(value);

    }

    @TypeConverter
    public String fromEAccountType(EAccountType type) {
        return type.name();
    }
}
