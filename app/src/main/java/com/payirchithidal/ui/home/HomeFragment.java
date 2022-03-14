package com.payirchithidal.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payirchithidal.session.AppStorage;
import com.payirchithidal.R;
import com.payirchithidal.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private AppStorage objApp = null;
    private  String URL ="";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        String userid = objApp.getUserId();
        TextView name=(TextView)root.findViewById(R.id.username);
        name.setText(objApp.getUserName());

        TextView playercount =(TextView)root.findViewById(R.id.playercount);
        TextView kitcount =(TextView)root.findViewById(R.id.kitcount);
        TextView batchcount =(TextView)root.findViewById(R.id.batchcount);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getDashboard",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String Playercount = jsonobject.getString("player");
                            String KitCount = jsonobject.getString("kit");
                            String Batchcount = jsonobject.getString("batch");
                            playercount.setText(Playercount);
                            kitcount.setText(KitCount);
                            batchcount.setText(Batchcount);



                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Invalid Credentials! hello"+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getContext(),error.toString(), Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user_id",userid);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(getContext());
        requestqueue.add(stringRequest);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}