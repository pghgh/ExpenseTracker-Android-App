package at.ac.univie.t0306.expensetracker.database.data_facade;

import androidx.lifecycle.LiveData;

import java.util.List;

import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.database.data.AccountAndAlert;
import at.ac.univie.t0306.expensetracker.database.data.Alert;
import at.ac.univie.t0306.expensetracker.database.data.Category;
import at.ac.univie.t0306.expensetracker.database.data.ExpenseTransaction;
import at.ac.univie.t0306.expensetracker.database.data.IncomeTransaction;
import at.ac.univie.t0306.expensetracker.database.data.Transaction;

public interface ITransactionsReadOperations {

    public List<IncomeTransaction> getIncomeTransactions();

    public List<ExpenseTransaction> getExpenseTransactions();

}
