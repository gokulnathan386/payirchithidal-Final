package com.payirchithidal.CustomerAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payirchithidal.session.AppStorage;
import com.payirchithidal.Model.FeesList;
import com.payirchithidal.Feeslistactivity;
import com.payirchithidal.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CustomAdapterFees extends ArrayAdapter<FeesList> {
    ArrayList<FeesList>  list_fees;
    Context context;
    int resource;
    private  String URL ="";
    private AppStorage objApp = null;
    public CustomAdapterFees(@NonNull Context context, int resource, @NonNull  ArrayList<FeesList> feesList) {
        super(context, resource, feesList);
        this.list_fees=feesList;
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
            convertView=layoutInflater.inflate(R.layout.feeslist,null,true);
        }

        FeesList pr=getItem(position);

        ImageView feesimg = (ImageView) convertView.findViewById(R.id.prImg);

        TextView playername =(TextView) convertView.findViewById(R.id.playername);
        TextView playeramount =(TextView) convertView.findViewById(R.id.playeramount);
        TextView branch =(TextView) convertView.findViewById(R.id.branch);
        TextView joindate =(TextView) convertView.findViewById(R.id.joindate);
        ImageView delFees =(ImageView) convertView.findViewById(R.id.delFees);
        View finalConvertView = convertView;

        delFees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getRootView().getContext(), R.style.MyAlertDialogStyle);
                builder.setMessage("Are you sure you want to delete this data?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int arg1) {
                        feesdel(pr.getFeesId());
                        Intent intent = new Intent(context, Feeslistactivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
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

        playername.setText(pr.getplayername());
        branch.setText(pr.getbranch());


        if (pr.getProfile().equals(objApp.apiImageUrl)) {
            feesimg.setImageResource(R.drawable.pimg);
        } else {
            Picasso.get().load(pr.getProfile()).into(feesimg);
        }

        if(pr.getFees_date().isEmpty())
        {
            joindate.setText(pr.getPayment_date());
        }else{
            joindate.setText(pr.getFees_date());
        }

        if(pr.getCredit().equals("0.00")) {
            playeramount.setText(pr.getDebit());
        }else{
            playeramount.setText(pr.getCredit());
        }

        return convertView;
    }

    private void feesdel(String feesId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"deleteFees",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(success.equals("1")){
                                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"Fees Not  Removed  Error !"+e,Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getContext(),error.toString(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",feesId);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(getContext());
        requestqueue.add(stringRequest);

    }


}
