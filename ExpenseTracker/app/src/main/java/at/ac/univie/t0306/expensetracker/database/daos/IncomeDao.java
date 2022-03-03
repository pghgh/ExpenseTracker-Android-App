package at.ac.univie.t0306.expensetracker.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.IncomeTransaction;

/**
 * An Data access object which defines an interfaces about the CRUD methods provided by this Table (Incometransaction)
 */
@Dao
public interface IncomeDao {
    @Query("SELECT * FROM 'incometransaction'")
    List<IncomeTransaction> getAll();

    @Query("SELECT * FROM 'incometransaction'")
    LiveData<List<IncomeTransaction>> getAllAsLiveData();

    @Insert
    void insert(IncomeTransaction transactions);

    @Update
    void update(IncomeTransaction transaction);

    @Delete
    void delete(IncomeTransaction transaction);
}
