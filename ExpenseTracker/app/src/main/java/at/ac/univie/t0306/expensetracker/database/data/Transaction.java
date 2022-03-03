package at.ac.univie.t0306.expensetracker.database.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * The IncomeTransaction data class which represents an Table in the SQLLite database!!
 */
@Entity
public class Transaction implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private int accountId;

    @ColumnInfo
    private String category;

    @ColumnInfo
    private String description;

    @ColumnInfo
    private double amount;

    @ColumnInfo
    private OffsetDateTime creationData;


    public Transaction(int accountId, String category, String description, double amount, OffsetDateTime creationData) {
        this.accountId = accountId;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.creationData = creationData;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public OffsetDateTime getCreationData() {
        return creationData;
    }

    public void setCreationData(OffsetDateTime creationData) {
        this.creationData = creationData;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountId='" + accountId + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", creationData=" + creationData +
                '}';
    }
}
