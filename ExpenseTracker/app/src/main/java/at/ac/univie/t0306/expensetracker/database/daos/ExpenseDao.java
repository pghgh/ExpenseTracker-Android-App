package at.ac.univie.t0306.expensetracker.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.ExpenseTransaction;

/**
 * An Data access object which defines an interfaces about the CRUD methods provided by this Table (Expensetrasaction)
 */
@Dao
public interface ExpenseDao {
    @Query("SELECT * FROM 'expensetransaction'")
    List<ExpenseTransaction> getAll();

    @Query("SELECT * FROM 'expensetransaction'")
    LiveData<List<ExpenseTransaction>> getAllAsLiveData();

    @Insert
    void insert(ExpenseTransaction transactions);

    @Update
    void update(ExpenseTransaction transaction);

    @Delete
    void delete(ExpenseTransaction transaction);
}
