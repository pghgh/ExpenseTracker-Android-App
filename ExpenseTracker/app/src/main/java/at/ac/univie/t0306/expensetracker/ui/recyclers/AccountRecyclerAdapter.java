package at.ac.univie.t0306.expensetracker.ui.recyclers;

import android.annotation.SuppressLint;
import android.util.Log;
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
import at.ac.univie.t0306.expensetracker.database.data.Account;
import at.ac.univie.t0306.expensetracker.ui.accounts.OnMoreOptionsButtonListener;


public class AccountRecyclerAdapter extends RecyclerView.Adapter<AccountRecyclerAdapter.ItemViewHolder> {

    private List<Account> itemList = new ArrayList<>();
    private OnMoreOptionsButtonListener<Account> optionsButtonListener;

    public AccountRecyclerAdapter(OnMoreOptionsButtonListener<Account> optionsButtonListener) {
        this.optionsButtonListener = optionsButtonListener;
    }

    /**
     * set new accounts to be displayed in the list
     *
     * @param itemList
     */
    public void setItemList(List<Account> itemList) {
        Log.i("data_refreshed", itemList.toString());
        this.itemList = itemList;
        notifyDataSetChanged();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_account_item, parent, false);
        return new ItemViewHolder(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * class for holding the account data for each item in the recycler list
     */
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private Account account;
        private TextView nameView, descriptionView, balanceView, dateView, typeView;
        private ImageButton btnMoreOptionsAccount;

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
            btnMoreOptionsAccount = itemView.findViewById(R.id.btnMoreOptionsAccount);
            nameView = itemView.findViewById(R.id.tvItemAccountName);
            // descriptionView =itemView.findViewById(R.id.btnMoreOptionsAccount);
            balanceView = itemView.findViewById(R.id.tvItemAccountBalance);
            dateView = itemView.findViewById(R.id.tvItemAccountDate);
            typeView = itemView.findViewById(R.id.tvItemAccountType);
        }

        /**
         * handle the actions of  more options Button when clicked
         *
         * @param btnMoreOptionsAccount
         */
        private void setOnMoreOptionsClicked(@NonNull ImageButton btnMoreOptionsAccount) {
            btnMoreOptionsAccount.setOnClickListener((view) -> {
                optionsButtonListener.clicked(view, account, getAdapterPosition());
            });
        }

        /**
         * fill the ui field with the account data for each account item
         *
         * @param account
         */
        @SuppressLint("SetTextI18n")
        public void bind(@NonNull Account account) {
            this.account = account;
            nameView.setText(account.getName());
            balanceView.setText(String.valueOf(account.getBalance()) + " €");
            OffsetDateTime date = account.getCreationData();
            String simplified_date = date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth();
            dateView.setText(simplified_date);
            typeView.setText(account.getAccountType());

        }
    }
}
