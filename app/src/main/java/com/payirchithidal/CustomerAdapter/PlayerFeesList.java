package com.payirchithidal.CustomerAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.payirchithidal.Model.FeesListPlayer;
import com.payirchithidal.R;

import java.util.ArrayList;

public class PlayerFeesList extends ArrayAdapter<FeesListPlayer> {
    ArrayList<FeesListPlayer> feeslist;
    Context context;
    int resource;

    public PlayerFeesList(@NonNull Context context, int resource, @NonNull  ArrayList<FeesListPlayer> feeslist) {
        super(context, resource, feeslist);
        this.feeslist=feeslist;
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.playerfeeslistlayout,null,true);
        }

        FeesListPlayer pr = getItem(position);

        TextView feesamount = (TextView) convertView.findViewById(R.id.feesamount);
        TextView paid =(TextView) convertView.findViewById(R.id.PaidList);
        TextView feesdate = (TextView) convertView.findViewById(R.id.feesdate);
        TextView feespaymentdate =(TextView) convertView.findViewById(R.id.feespaymentdate);


        feesamount.setText(pr.getCredit());

        if(pr.getDebit().equals("null")){
            paid.setText("");
        }else{
            paid.setText(pr.getDebit());
        }

        if(pr.getFeesdate().equals("null")){
            feesdate.setText("");
        }else{
            feesdate.setText(pr.getFeesdate());
        }

        if(pr.getPaymentDate().equals("null")){
            feespaymentdate.setText("");
        }else{
            feespaymentdate.setText(pr.getPaymentDate());
        }

        return convertView;
    }

}
