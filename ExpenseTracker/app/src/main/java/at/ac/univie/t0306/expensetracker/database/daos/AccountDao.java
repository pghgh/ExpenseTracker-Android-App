package at.ac.univie.t0306.expensetracker.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.AccountAndAlert;

/**
 * An Data access object which defines an interfaces about the CRUD methods provided by this Table (Account)
 */
@Dao
public interface AccountDao {
    @Query("SELECT * FROM account")
    LiveData<List<Account>> getAllAsLiveData();

    @Query("SELECT * FROM account")
    List<Account> getAll();

    @androidx.room.Transaction
    @Query("SELECT * FROM account")
    public LiveData<List<AccountAndAlert>> getAccountAndAlert();

    @Query("SELECT * FROM account WHERE id= :id")
    public Account getAccount(int id);

    @Insert
    void insert(Account account);


    @Update
    void update(Account account);

    @Delete
    void delete(Account account);
}
