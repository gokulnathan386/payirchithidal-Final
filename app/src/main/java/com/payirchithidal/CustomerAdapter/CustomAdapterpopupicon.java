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
import com.payirchithidal.Model.PopupIcon;
import com.payirchithidal.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapterpopupicon extends ArrayAdapter<PopupIcon> {
    ArrayList<PopupIcon>  list_notify;
    Context context;
    int resource;
    private  String URL ="";
    private AppStorage objApp = null;

    public CustomAdapterpopupicon(@NonNull Context context, int resource, @NonNull   ArrayList<PopupIcon>  list_notify) {
        super(context, resource, list_notify);
        this.list_notify=list_notify;
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
            convertView=layoutInflater.inflate(R.layout.popupnotifymsg,null,true);
        }

        PopupIcon pr = getItem(position);

        TextView notifyname = (TextView) convertView.findViewById(R.id.notifyname);
        ImageView prImg = (ImageView) convertView.findViewById(R.id.prImg);
        TextView message =(TextView) convertView.findViewById(R.id.message);
        TextView notifymsg =(TextView) convertView.findViewById(R.id.notifymsg);

        notifyname.setText(pr.getname());
        notifymsg.setText(pr.getcreated_at());
        message.setText(pr.getmessage());

        if (pr.getprofile().equals(objApp.apiImageUrl)) {
            prImg.setImageResource(R.drawable.pimg);
        } else {
            Picasso.get().load(pr.getprofile()).into(prImg);
        }
        return convertView;
    }

}
