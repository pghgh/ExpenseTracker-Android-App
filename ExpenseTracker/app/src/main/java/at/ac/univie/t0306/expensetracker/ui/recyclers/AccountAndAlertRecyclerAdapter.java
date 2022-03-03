package at.ac.univie.t0306.expensetracker.ui.recyclers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import at.ac.univie.t0306.expensetracker.R;
import at.ac.univie.t0306.expensetracker.database.data.AccountAndAlert;
import at.ac.univie.t0306.expensetracker.managers.AlertManager;

public class AccountAndAlertRecyclerAdapter extends RecyclerView.Adapter<AccountAndAlertRecyclerAdapter.ItemViewHolder> {

    private List<AccountAndAlert> itemList = new ArrayList<>();
    private Context context;
    private AlertManager alertManager;

    public AccountAndAlertRecyclerAdapter(Context context, AlertManager alertManager) {
        this.context = context;
        this.alertManager = alertManager;
    }

    public void setItemList(List<AccountAndAlert> itemList) {
        Log.i("list size by refreshing : ", "size:" + itemList.size());
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public List<AccountAndAlert> getItemList() {
        return itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_account_and_alert_item, parent, false);
        return new ItemViewHolder(view, alertManager);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private AccountAndAlert accountAndAlert;
        private TextView nameView, balanceView, thresholdView;
        private ImageButton btnMoreOptionsAlert;


        public ItemViewHolder(@NonNull View itemView, AlertManager alertManager) {
            super(itemView);
            inflateViews(itemView);
            setOnMoreOptionsClicked(btnMoreOptionsAlert, alertManager);
        }

        private void inflateViews(@NonNull View itemView) {
            btnMoreOptionsAlert = itemView.findViewById(R.id.btnMoreOptionsAlert);
            nameView = itemView.findViewById(R.id.tvAlertItemAccountName);
            balanceView = itemView.findViewById(R.id.tvAletItemAccountBalance);
            thresholdView = itemView.findViewById(R.id.tvAlertItemThreshold);
        }

        private void setOnMoreOptionsClicked(@NonNull ImageButton btnMoreOptionsAccount, AlertManager alertManager) {
            btnMoreOptionsAccount.setOnClickListener((view) -> {
                alertManager.deleteAlert(itemList.get(getAdapterPosition()).getAccount().getId());
                Toast.makeText(context, "Alert deleted", Toast.LENGTH_SHORT).show();
            });
        }

        private void doChangeAccountOperation() {
//            Intent intent =new Intent(context , EditAccountActivity.class);
//            intent.putExtra(ACCOUNT_PARAMETER, account);
//            context.startActivity(intent);

            Toast.makeText(context, "Alert changed(Fake)", Toast.LENGTH_SHORT).show();
        }

        @SuppressLint("SetTextI18n")
        public void bind(AccountAndAlert accountAndAlert) {
            this.accountAndAlert = accountAndAlert;

            nameView.setText(accountAndAlert.getAccount().getName());
            balanceView.setText(accountAndAlert.getAccount().getBalance() + " €");
            thresholdView.setText(accountAndAlert.getAlert().getThreshold() + " €");

        }
    }
}



