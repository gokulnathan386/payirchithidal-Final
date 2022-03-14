package com.payirchithidal.CustomerAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.payirchithidal.Model.FeesBalanceListtype;
import com.payirchithidal.R;

import java.util.ArrayList;

public class CustomAdapterplayertype extends ArrayAdapter<FeesBalanceListtype> {

    ArrayList<FeesBalanceListtype> list_balance;
    Context context;
    int resource;
    public CustomAdapterplayertype(@NonNull Context context, int resource, @NonNull ArrayList<FeesBalanceListtype> list_balance) {
        super(context, resource, list_balance);
        this.list_balance=list_balance;
        this.context=context;
        this.resource=resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.balanceamount,null,true);
        }
        FeesBalanceListtype pr=getItem(position);
        CheckBox playID =(CheckBox) convertView.findViewById(R.id.playID);
        playID.setVisibility(View.GONE);
        playID.setEnabled(false);
        TextView amount =(TextView) convertView.findViewById(R.id.amount);
        TextView date =(TextView) convertView.findViewById(R.id.date);
        TextView paid =(TextView) convertView.findViewById(R.id.paid);
        date.setText(pr.getpayment_date());
        amount.setText(pr.getcredit());
        paid.setText(pr.getdebit());
        return convertView;
    }
}
