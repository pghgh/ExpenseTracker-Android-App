package at.ac.univie.t0306.expensetracker.database.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * The Alert data class which represents an Table in the SQLLite database!!
 */
@Entity
public class Alert {
    @PrimaryKey(autoGenerate = true)
    private int id;


    @ColumnInfo()
    private double threshold;


    private int associatedAccountId;

    public Alert(double threshold, int associatedAccountId) {
        this.threshold = threshold;
        this.associatedAccountId = associatedAccountId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getAssociatedAccountId() {
        return associatedAccountId;
    }

    public void setAssociatedAccountId(int associatedAccountId) {
        this.associatedAccountId = associatedAccountId;
    }


    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "id=" + id +
                ", threshold=" + threshold +
                ", associatedAccountId=" + associatedAccountId +
                '}';
    }
}
