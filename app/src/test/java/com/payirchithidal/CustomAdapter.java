package com.payirchithidal;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;

        import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

    Context context;
    String nameList[];
    String numberList[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] nameList, String[] numberList) {
        this.context = context;
        this.nameList= nameList;
        this.numberList = numberList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return nameList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.player_list, null);
        TextView name = (TextView) view.findViewById(R.id.playername);
        TextView number = (TextView) view.findViewById(R.id.playernumber);
        name.setText(nameList[i]);
        number.setText(numberList[i]);
        return view;
    }



}
