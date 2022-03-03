package at.ac.univie.t0306.expensetracker.database;

import android.content.Context;

import androidx.room.Room;

/**
 * It provides the getDB method to get the database instance, and be able to access the database
 */
public class DatabaseProvider {


    private static AppDatabase databaseInstance = null;
    private static final String DATABASE_NAME = "app_database";

    public synchronized static AppDatabase getDB(Context appContext) {
        if (databaseInstance == null)
            databaseInstance = buildDatabaseObject(appContext);
        return databaseInstance;


    }

    private static AppDatabase buildDatabaseObject(Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
    }
}
