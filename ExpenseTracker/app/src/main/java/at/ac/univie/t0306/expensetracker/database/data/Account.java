package at.ac.univie.t0306.expensetracker.database.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * The Account data class which represents an Table in the SQLLite database!!
 */
@Entity
public class Account implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String name;

    @ColumnInfo
    private String description;

    @ColumnInfo
    private double balance;

    @ColumnInfo
    private String accountType;

    @ColumnInfo
    private OffsetDateTime creationData;


    public Account(String name, String description, double balance, String accountType, OffsetDateTime creationData) {
        this.name = name;
        this.description = description;
        this.balance = balance;
        this.accountType = accountType;
        this.creationData = creationData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public OffsetDateTime getCreationData() {
        return creationData;
    }

    public void setCreationData(OffsetDateTime creationData) {
        this.creationData = creationData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && Double.compare(account.balance, balance) == 0 && name.equals(account.name) && Objects.equals(description, account.description) && accountType.equals(account.accountType);
    }


}
