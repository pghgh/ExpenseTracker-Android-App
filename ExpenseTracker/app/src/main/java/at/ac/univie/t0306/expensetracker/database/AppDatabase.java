package at.ac.univie.t0306.expensetracker.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import at.ac.univie.t0306.expensetracker.database.converters.TimeStampConverter;
import at.ac.univie.t0306.expensetracker.database.daos.AccountDao;
import at.ac.univie.t0306.expensetracker.database.daos.AlertDao;
import at.ac.univie.t0306.expensetracker.database.daos.CategoryDao;
import at.ac.univie.t0306.expensetracker.database.daos.ExpenseDao;
import at.ac.univie.t0306.expensetracker.database.daos.IncomeDao;
import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.Alert;
import at.ac.univie.t0306.expensetracker.database.data.Category;
import at.ac.univie.t0306.expensetracker.database.data.ExpenseTransaction;
import at.ac.univie.t0306.expensetracker.database.data.IncomeTransaction;
import at.ac.univie.t0306.expensetracker.database.data.Transaction;

/**
 * It used to define the database tables to be created by the room database.
 */
@Database(entities = {Transaction.class, IncomeTransaction.class, ExpenseTransaction.class, Category.class, Account.class, Alert.class}, version = 1, exportSchema = false)
@TypeConverters({TimeStampConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract IncomeDao incomeDao();

    public abstract ExpenseDao expenseDao();

    public abstract CategoryDao categoryDao();

    public abstract AccountDao accountDao();

    public abstract AlertDao alertDao();


}
