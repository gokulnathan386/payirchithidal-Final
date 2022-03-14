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

import com.payirchithidal.session.AppStorage;
import com.payirchithidal.Model.FeesBalanceList;
import com.payirchithidal.R;

import java.util.ArrayList;

public class CustomAdapterplayer extends ArrayAdapter<FeesBalanceList> {
    ArrayList<FeesBalanceList> list_balance;
    Context context;
    int resource;
    private  String URL ="";
    private AppStorage objApp = null;
    public CustomAdapterplayer(@NonNull Context context, int resource, @NonNull ArrayList<FeesBalanceList> list_balance) {
        super(context, resource, list_balance);
        this.list_balance=list_balance;
        this.context=context;
        this.resource=resource;
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.balanceamount,null,true);
        }

        FeesBalanceList pr=getItem(position);


        CheckBox playID = (CheckBox) convertView.findViewById(R.id.playID);
        TextView amount =(TextView) convertView.findViewById(R.id.amount);
        TextView date =(TextView) convertView.findViewById(R.id.date);
        date.setText(pr.getFeesDate());
        amount.setText(pr.getCredit());

        playID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(playID.isChecked()) {
                    objApp.addItem(Integer.valueOf(pr.getFeesId()));
                }else{
                   objApp.removeItem(Integer.valueOf(pr.getFeesId()));
                }
                //Toast.makeText(getContext(),pr.getFeesId(), Toast.LENGTH_SHORT).show();

            }
        });
        return convertView;
    }
}
