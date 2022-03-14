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

import com.payirchithidal.Model.PayoutList;
import com.payirchithidal.R;

import java.util.ArrayList;

public class CustomAdapterPayout extends ArrayAdapter<PayoutList> {

    ArrayList<PayoutList> list_payout;
    Context context;
    int resource;


    public CustomAdapterPayout(@NonNull Context context, int resource, @NonNull  ArrayList<PayoutList> list_payout) {
        super(context, resource, list_payout);
        this.list_payout=list_payout;
        this.context=context;
        this.resource=resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.payout_list_design,null,true);
        }

        PayoutList pr=getItem(position);

        TextView clubname =(TextView) convertView.findViewById(R.id.clubname);
        TextView amount =(TextView) convertView.findViewById(R.id.amount);
        TextView reason =(TextView) convertView.findViewById(R.id.reason);

        clubname.setText(pr.getclubname());
        reason.setText(pr.getreason());

        return convertView;
    }

}
