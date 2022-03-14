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
import com.payirchithidal.EditKitActivity;
import com.payirchithidal.Model.KitList;
import com.payirchithidal.KitlistActivity;
import com.payirchithidal.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapterkit  extends ArrayAdapter<KitList> {

    ArrayList<KitList> kitList;
    Context context;
    int resource;
    private  String URL ="";
    private AppStorage objApp = null;
    String userid;


    public CustomAdapterkit(@NonNull Context context, int resource, @NonNull  ArrayList<KitList> kitList) {
        super(context, resource, kitList);
        this.kitList=kitList;
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
            convertView=layoutInflater.inflate(R.layout.kitlist_design,null,true);
        }

        KitList pr=getItem(position);

        TextView kitname1 = (TextView) convertView.findViewById(R.id.kitname);
        TextView branchname = (TextView) convertView.findViewById(R.id.branchname);
        ImageView Kitimage = (ImageView) convertView.findViewById(R.id.kitImg);
        TextView kitid =(TextView) convertView.findViewById(R.id.kitid);
        ImageView kitedit =(ImageView) convertView.findViewById(R.id.kitedit);

        kitedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditKit(pr.getKitId());

            }
        });

       ImageView del = (ImageView)convertView.findViewById(R.id. del);
        View finalConvertView = convertView;
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getRootView().getContext(), R.style.MyAlertDialogStyle);
                builder.setMessage("Are you sure you want to delete this data?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int arg1) {
                        KitDel(pr.getKitId());
                        Intent intent = new Intent(context, KitlistActivity.class);;
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


        kitname1.setText(pr.getkitname());
        branchname.setText(pr.getBranchname());
        Kitimage.setImageResource(R.drawable.pimg);

        if (pr.getKitimg().equals(objApp.apiImageUrl)) {
            Kitimage.setImageResource(R.drawable.pimg);
        } else {
            Picasso.get().load(pr.getKitimg()).into(Kitimage);
        }
        return convertView;
    }

    private void EditKit(String kitIdPass) {
        Intent intent = new Intent(context, EditKitActivity.class);
        intent.putExtra("Kit_Id", kitIdPass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void KitDel(String kitIdPass) {
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        userid = objApp.getUserId();
                StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"deleteKit",
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
                            Toast.makeText(getContext(),"Kit Not  Removed  Error !"+e,Toast.LENGTH_LONG).show();
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
                params.put("id",kitIdPass);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(getContext());
        requestqueue.add(stringRequest);
    }
}
