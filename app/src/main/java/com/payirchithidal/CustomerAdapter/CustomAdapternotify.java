package com.payirchithidal.CustomerAdapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.payirchithidal.session.AppStorage;
import com.payirchithidal.Model.NotifyList;
import com.payirchithidal.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapternotify extends ArrayAdapter<NotifyList> {

    ArrayList<NotifyList> notifylist;
    Context context;
    int resource;
    private  String URL ="";
    private AppStorage objApp = null;

    public CustomAdapternotify(@NonNull Context context, int resource, @NonNull  ArrayList<NotifyList> notifylist) {
        super(context, resource, notifylist);
        this.notifylist=notifylist;
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
            convertView=layoutInflater.inflate(R.layout.notificationlist_design,null,true);
        }

        NotifyList pr = getItem(position);

        TextView playername = (TextView) convertView.findViewById(R.id.playername);
        ImageView notifyimg = (ImageView) convertView.findViewById(R.id.prImg);
        TextView branchname =(TextView) convertView.findViewById(R.id.branchname);
        playername.setText(pr.getfirstname());
        branchname.setText(pr.getbranchname());
        
        if (pr.getprofile().equals(objApp.apiImageUrl)) {
            notifyimg.setImageResource(R.drawable.pimg);
        } else {
            Picasso.get().load(pr.getprofile()).into(notifyimg);
        }
        return convertView;
    }


}
