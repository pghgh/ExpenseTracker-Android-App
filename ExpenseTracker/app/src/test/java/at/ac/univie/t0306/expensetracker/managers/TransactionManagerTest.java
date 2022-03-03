package at.ac.univie.t0306.expensetracker.managers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.ExpenseTransaction;
import at.ac.univie.t0306.expensetracker.database.data_facade.DataRepo;

public class TransactionManagerTest {

    private DataRepo dataRepoMocked;
    private TransactionManager transactionManager;
    private AccountManager accountManager;

    @Before
    public void setUp() {
        dataRepoMocked = Mockito.mock(DataRepo.class);
        transactionManager = new TransactionManager(dataRepoMocked);
        accountManager = new AccountManager(dataRepoMocked);
    }

    @Test
    public void AddingTransactionTest() {
        ExpenseTransaction transactionToInsert = new ExpenseTransaction(1, "Food", "Some desc", 100.0, OffsetDateTime.now());
        Account accountCorresponding = new Account("name", "desc", 100.0, "type", OffsetDateTime.now());
        transactionManager.addTransaction(transactionToInsert, accountCorresponding);
        Mockito.verify(dataRepoMocked, Mockito.times(1)).addExpenseTransaction(transactionToInsert);
    }

    @Test
    public void TransactionUpdateTest() {
        ExpenseTransaction transactionToUpdate = new ExpenseTransaction(1, "Food", "Some desc", 100.0, OffsetDateTime.now());
        ExpenseTransaction transactionUpdated = new ExpenseTransaction(1, "School", "Some desc", 100.0, OffsetDateTime.now());
        Account accountCorresponding = new Account("name", "desc", 100.0, "type", OffsetDateTime.now());
        transactionManager.updateTransaction(transactionToUpdate, transactionUpdated, accountCorresponding);
        Mockito.verify(dataRepoMocked, Mockito.times(1)).updateExpenseTransaction(transactionToUpdate, transactionUpdated);

    }


    @Test
    public void TransactionDeleteTest() {
        Account accountShouldBeInserted = new Account("name", "desc", 100.0, "type", OffsetDateTime.now());
        accountManager.addAccount(accountShouldBeInserted);
        Mockito.verify(dataRepoMocked, Mockito.times(1)).addAccounts(accountShouldBeInserted);
        ExpenseTransaction transactionToInsert = new ExpenseTransaction(1, "Food", "Some desc", 100.0, OffsetDateTime.now());
        transactionManager.addTransaction(transactionToInsert, accountShouldBeInserted);
        Mockito.verify(dataRepoMocked, Mockito.times(1)).addExpenseTransaction(transactionToInsert);
        ExpenseTransaction transactionToDelete = transactionToInsert;
        transactionManager.deleteTransaction(transactionToDelete);
        Mockito.verify(dataRepoMocked, Mockito.times(1)).deleteExpenseTransaction(transactionToDelete);
    }


}
