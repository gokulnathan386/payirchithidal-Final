package com.payirchithidal.CustomerAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.payirchithidal.Model.AccountDetail;
import com.payirchithidal.session.AppStorage;
import com.payirchithidal.EditAccountDetails;
import com.payirchithidal.R;

import java.util.ArrayList;

public class AccountDetailsAdapter extends ArrayAdapter<AccountDetail> {
    ArrayList<AccountDetail> accdetail;
    Context context;
    int resource;
    private  String URL ="";
    private AppStorage objApp = null;
    public AccountDetailsAdapter(@NonNull Context context, int resource, @NonNull   ArrayList<AccountDetail> accdetail) {
        super(context, resource, accdetail);
        this.accdetail = accdetail;
        this.context = context;
        this.resource = resource;
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.accountdetails, null, true);
        }
        String userid = objApp.getUserId();
        AccountDetail pr = getItem(position);

        TextView clubname = (TextView) convertView.findViewById(R.id.clubname);
        TextView holdername = (TextView) convertView.findViewById(R.id.holdername);
        TextView bankname = (TextView) convertView.findViewById(R.id.bankname);
        TextView accnumber = (TextView) convertView.findViewById(R.id.accnumber);
        TextView ifsccode = (TextView) convertView.findViewById(R.id.ifsccode);
        TextView branchname = (TextView) convertView.findViewById(R.id.branchname);

        ImageView  editAccountDetail =(ImageView) convertView.findViewById(R.id.editAccountDetail);
        editAccountDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAccDetail(pr.getAccount_id());
            }
        });
        clubname.setText(pr.getCulbName());
        holdername.setText(pr.getHolderName());
        bankname.setText(pr.getBankName());
        accnumber.setText(pr.getAccountName());
        ifsccode.setText(pr.getIfscCode());
        branchname.setText(pr.getBranchName());

        return convertView;
    }
    private void editAccDetail(String account_id) {
        Intent intent = new Intent(context, EditAccountDetails.class);
        intent.putExtra("accountId", account_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
