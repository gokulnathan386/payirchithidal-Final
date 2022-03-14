package com.payirchithidal.CustomerAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.payirchithidal.session.AppStorage;
import com.payirchithidal.Model.AttendeanceList;
import com.payirchithidal.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapterattendance extends ArrayAdapter<AttendeanceList> {

    ArrayList<AttendeanceList> list_attendance;
    Context context;
    int resource;
    private  String URL ="";
    private AppStorage objApp = null;
    public CustomAdapterattendance(@NonNull Context context, int resource, @NonNull ArrayList<AttendeanceList> list_attendance) {
        super(context, resource, list_attendance);
        this.list_attendance = list_attendance;
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
            convertView = layoutInflater.inflate(R.layout.attendancelist_design, null, true);
        }

        AttendeanceList pr = getItem(position);
        ImageView attimg = (ImageView) convertView.findViewById(R.id.prImgatt);
        TextView playername = (TextView) convertView.findViewById(R.id.playername);
        TextView branchname = (TextView) convertView.findViewById(R.id.branchname);
        playername.setText(pr.getfirstname());
        branchname.setText(pr.getbranchname());
        if (pr.getprofile().equals(objApp.apiImageUrl)) {
            attimg.setImageResource(R.drawable.pimg);
        } else {
            Picasso.get().load(pr.getprofile()).into(attimg);
        }


        return convertView;
    }

}
