package at.ac.univie.t0306.expensetracker.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.Alert;

/**
 * An Data access object which defines an interfaces about the CRUD methods provided by this Table (Alert)
 */
@Dao
public interface AlertDao {
    @Query("SELECT * FROM alert")
    List<Alert> getAll();

    @Query("SELECT * FROM alert  WHERE associatedAccountId= :accountId")
    Alert getAlertFoAccount(int accountId);


    @Insert
    void insert(Alert alert);


    @Update
    void update(Alert alert);

    @Delete
    void delete(Alert alert);
}
