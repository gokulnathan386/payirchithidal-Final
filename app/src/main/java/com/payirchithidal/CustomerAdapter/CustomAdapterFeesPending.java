package com.payirchithidal.CustomerAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payirchithidal.session.AppStorage;
import com.payirchithidal.Model.FeesPending;
import com.payirchithidal.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapterFeesPending extends ArrayAdapter<FeesPending> {
    ArrayList<FeesPending> fees_pending_list;
    Context context;
    int resource;
    private  String URL ="";
    private AppStorage objApp = null;
    ProgressDialog progressDialog;
    String userid;
    public CustomAdapterFeesPending(@NonNull Context context, int resource, @NonNull ArrayList<FeesPending> fees_pending_list) {
        super(context, resource, fees_pending_list);
        this.fees_pending_list = fees_pending_list;
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
            convertView = layoutInflater.inflate(R.layout.pendinglistdesign, null, true);
        }
        userid = objApp.getUserId();
        FeesPending pr = getItem(position);
        ImageView prImg = (ImageView) convertView.findViewById(R.id.prImg);
        TextView FirstNameFeesPendingList = (TextView) convertView.findViewById(R.id.FirstNameFeesPendingList);
        TextView TotalAmountFees = (TextView) convertView.findViewById(R.id.TotalAmountFees);
        TextView PaidAmountFees = (TextView) convertView.findViewById(R.id.PaidAmountFees);
        TextView BalanceAmountFees = (TextView) convertView.findViewById(R.id.BalanceAmountFees);
        TextView ButtonLinkSendFees = (TextView) convertView.findViewById(R.id.ButtonLinkSendFees);
        View finalConvertView = convertView;
        ButtonLinkSendFees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getRootView().getContext(), R.style.MyAlertDialogStyle);
                builder.setMessage("Are you sure?\nDo you really want to send payment link?.");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int arg1) {
                        SendLinkFees(pr.get_PlayerId(),pr.get_Balance());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        FirstNameFeesPendingList.setText(pr.get_FirstName());
        TotalAmountFees.setText(pr.get_Total());
        PaidAmountFees.setText(pr.get_Paid());
        BalanceAmountFees.setText(pr.get_Balance());

        if (pr.get_Player().equals(objApp.apiImageUrl)) {
            prImg.setImageResource(R.drawable.pimg);
        } else {
            Picasso.get().load(pr.get_Player()).into(prImg);
        }


        return convertView;
    }

    private void SendLinkFees(String PlayerId, String balance) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"sendFees",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(success.equals("1")){
                                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("player_id",PlayerId);
                params.put("amount", balance);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
