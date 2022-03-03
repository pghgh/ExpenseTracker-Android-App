package at.ac.univie.t0306.expensetracker.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.Transaction;

/**
 * An Data access object which defines an interfaces about the CRUD methods provided by this Table (Transaction)
 */
@Dao
public interface TransactionDao {
    @Query("SELECT * FROM 'transaction'")
    List<Transaction> getAll();

    @Insert
    void insert(Transaction... transactions);

    @Update
    void update(Transaction transaction);

    @Delete
    void delete(Transaction transaction);
}
