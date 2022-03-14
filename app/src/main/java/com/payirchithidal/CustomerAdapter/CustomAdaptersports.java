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
import com.payirchithidal.EditSportsActivity;
import com.payirchithidal.R;
import com.payirchithidal.Model.SportsList;
import com.payirchithidal.SportsListActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomAdaptersports extends ArrayAdapter<SportsList> {
    ArrayList<SportsList> sportsList;
    Context context;
    int resource;
    private  String URL ="";
    private AppStorage objApp = null;
    public CustomAdaptersports(@NonNull Context context, int resource, @NonNull  ArrayList<SportsList> sportsList) {
        super(context, resource, sportsList);
        this.sportsList=sportsList;
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
            convertView=layoutInflater.inflate(R.layout.sportslist_design,null,true);
        }

       SportsList pr=getItem(position);

        TextView sportname = (TextView) convertView.findViewById(R.id.sportsname);
        ImageView sportsicon = (ImageView) convertView.findViewById(R.id.prImg);
        ImageView sportsbtn = (ImageView) convertView.findViewById(R.id.sportsbtn);
        sportsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditSports(pr.getsportId());
            }
        });

        ImageView sportsdel =(ImageView)convertView.findViewById(R.id.sportsdel);
        View finalConvertView = convertView;
        sportsdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getRootView().getContext(), R.style.MyAlertDialogStyle);
                builder.setMessage("Are you sure you want to delete this data?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int arg1) {
                        Sportsdel(pr.getsportId());
                        Intent intent = new Intent(context, SportsListActivity.class);;
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


        sportname.setText(pr.getsportsname());

        if (pr.geticon().equals(objApp.apiImageUrl)) {
            sportsicon.setImageResource(R.drawable.pimg);
        } else {
            Picasso.get().load(pr.geticon()).into(sportsicon);
        }

        return convertView;
    }

    private void EditSports(String getsportId) {
        Intent intent = new Intent(context, EditSportsActivity.class);
        intent.putExtra("sports_id", getsportId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void Sportsdel(String SportsId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"deleteSports",
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
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",SportsId);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(getContext());
        requestqueue.add(stringRequest);
    }

}
