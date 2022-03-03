package at.ac.univie.t0306.expensetracker.database.data;


/**
 * this enum has some predefined accounts type.
 */
public enum EAccountType {
    CASH("Cash"), BANK_ACCOUNT("Bank Account"), STOCK("Stock"), CARD("Card"), OTHER("Other");
    private String name;

    EAccountType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
