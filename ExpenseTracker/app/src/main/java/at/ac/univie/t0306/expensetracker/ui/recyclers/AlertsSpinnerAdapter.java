package at.ac.univie.t0306.expensetracker.ui.recyclers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import at.ac.univie.t0306.expensetracker.R;
import at.ac.univie.t0306.expensetracker.database.data.Account;

public class AlertsSpinnerAdapter extends BaseAdapter {


    private Context context;
    private List<Account> accountList;

    public AlertsSpinnerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Account> accountList) {
        this.accountList = accountList;
        notifyDataSetChanged();
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    @Override
    public int getCount() {
        return accountList == null ? 0 : accountList.size();
    }

    @Override
    public Object getItem(int position) {
        return accountList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_alert_item, parent, false);


        TextView accountIdView = view.findViewById(R.id.accountId);
        TextView accountNameView = view.findViewById(R.id.accountName);

        Account account = accountList.get(position);

        accountIdView.setText(account.getId() + "");
        accountNameView.setText(account.getName());


        return view;
    }
}
