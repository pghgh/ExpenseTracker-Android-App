package at.ac.univie.t0306.expensetracker.ui.recyclers;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import at.ac.univie.t0306.expensetracker.R;
import at.ac.univie.t0306.expensetracker.database.data.IncomeTransaction;
import at.ac.univie.t0306.expensetracker.database.data.Transaction;
import at.ac.univie.t0306.expensetracker.ui.accounts.OnMoreOptionsButtonListener;


public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.ItemViewHolder> {

    private List<Transaction> transactionList = new ArrayList<>();

    private OnMoreOptionsButtonListener<Transaction> optionsButtonListener;

    public TransactionRecyclerAdapter(OnMoreOptionsButtonListener<Transaction> optionsButtonListener) {
        this.optionsButtonListener = optionsButtonListener;
    }

    /**
     * @param transactionList
     */
    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
        notifyDataSetChanged();
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    /**
     * inflates the view for each account item in the list
     * {@inheritDoc}
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_transaction_item, parent, false);
        return new ItemViewHolder(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(transactionList.get(position));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    /**
     * class for holding the account data for each item in the recycler list
     */
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private Transaction transaction;
        private TextView transNameView, amountView, dateView, categoryView;
        private ImageButton btnMoreOptionsAccount;
        private View TransactionTypeView;

        /**
         * @param itemView
         */
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            inflateUI(itemView);
            setOnMoreOptionsClicked(btnMoreOptionsAccount);
        }

        /**
         * find the UI views of the item view
         *
         * @param itemView
         */
        private void inflateUI(@NonNull View itemView) {
            btnMoreOptionsAccount = itemView.findViewById(R.id.btnMoreOptionsTransactoin);
            transNameView = itemView.findViewById(R.id.tvItemTransactionNames);
            amountView = itemView.findViewById(R.id.tvItemTransactionAmount);
            dateView = itemView.findViewById(R.id.tvItemTransactionDate);
            categoryView = itemView.findViewById(R.id.tvItemTransactionCategory);
            TransactionTypeView = itemView.findViewById(R.id.dividerType);
        }

        /**
         * handle the actions of  more options Button when clicked
         *
         * @param btnMoreOptionsAccount
         */
        private void setOnMoreOptionsClicked(@NonNull ImageButton btnMoreOptionsAccount) {
            btnMoreOptionsAccount.setOnClickListener((view) -> {
                optionsButtonListener.clicked(view, transaction, getAdapterPosition());

            });
        }

        /**
         * fill the ui field with the account data for each account item
         *
         * @param transaction
         */
        @SuppressLint("SetTextI18n")
        public void bind(@NonNull Transaction transaction) {
            this.transaction = transaction;
            transNameView.setText(transaction.getDescription());
            amountView.setText(String.valueOf(transaction.getAmount()) + " €");
            OffsetDateTime date = transaction.getCreationData();
            String simplified_date = date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
            dateView.setText(simplified_date);
            categoryView.setText(transaction.getCategory());

            TransactionTypeView.setBackgroundColor(transaction instanceof IncomeTransaction ? itemView.getResources().getColor(R.color.green) : itemView.getResources().getColor(R.color.red));
        }
    }
}
