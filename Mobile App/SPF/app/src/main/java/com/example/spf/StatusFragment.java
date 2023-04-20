package com.example.spf;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatusFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public SharedPreferences userSP;

    Spinner devicesSpiner;
    TextView foodText;
    TextView waterText;
    TextView eatText;

    ArrayList<String> devicesNamesList=new ArrayList<>();
    ArrayList<String> devicesLocal=new ArrayList<String>();
    ArrayList<Integer> devicesId=new ArrayList<Integer>();
    ArrayList<Integer> devicesRef_agua=new ArrayList<Integer>();
    ArrayList<Integer> devicesRef_comida=new ArrayList<Integer>();

    TableLayout statusTable;
    public StatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatusFragment newInstance(String param1, String param2) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        userSP = this.getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        //Simulation of data get from json package -----------------------------
        getDevices();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Creation of select box -------------------------------------------------------------------------------------------------------------
        View view=inflater.inflate(R.layout.fragment_status,container,false);
        devicesSpiner = view.findViewById(R.id.spinner);
        foodText = view.findViewById(R.id.foodRefillTimes);
        waterText = view.findViewById(R.id.waterRefillTimes);
        eatText = view.findViewById(R.id.mealTimes);
        statusTable = view.findViewById(R.id.statusTable);
        statusTable.setVisibility(View.GONE);
        //-----------------------


        devicesSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getItemAtPosition(position).equals(""))
                {
                    Toast.makeText(parent.getContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
                    statusTable.setVisibility(View.GONE);
                }
                else
                {
                    foodText.setText("");
                    waterText.setText("");
                    getStatus(devicesId.get(position-1));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO Auto-genarated method stub
            }
        });

        //------------------------------------------------------------------------------------------------------------------------------------

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    public void getDevices(){
        String url = getString(R.string.ip_adress)+"API_Func/get_devices_by_userid.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJson(response,1);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StatusFragment.this.getContext(),"ERRO ",Toast.LENGTH_SHORT);
            }
        }){@Override
        protected Map<String, String> getParams(){
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid",String.valueOf(userSP.getInt("id",0)));
            return params;
        }};
        RequestQueue mQueue = Volley.newRequestQueue(StatusFragment.this.getContext());
        mQueue.add(stringRequest);
    }
    public void parseJson(String response, int v){
        try {
            if(v==1){
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("devices");
            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject devices = jsonArray.getJSONObject(i);
                devicesNamesList.add(devices.getString("Nome"));
                devicesId.add(devices.getInt("ID_comedouro"));
                devicesLocal.add(devices.getString("Local_comedouro"));
                devicesRef_agua.add(devices.getInt("Nivelref_agua"));
                devicesRef_comida.add(devices.getInt("Nivelref_racao"));
            }
            updateSpinner();
            }else if(v==2){
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("historic");
                for(int i=0; i < jsonArray.length(); i++) {
                    JSONObject status = jsonArray.getJSONObject(i);
                    switch (status.getString("sensor")) {
                        case "racao":
                            eatText.setText(status.getString("count"));
                        break;
                        case "agua":
                            waterText.setText(status.getString("count"));
                        break;
                        default:
                            foodText.setText("NOT FOUND");
                            //waterText.setText("NOT FOUND");
                            break;
                    }
                }
                statusTable.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void updateSpinner(){
        devicesNamesList.add(0,"");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(StatusFragment.this.getContext(), android.R.layout.simple_list_item_1,devicesNamesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        devicesSpiner.setAdapter(adapter);

    }

    public void getStatus(int deviceId){
        String url = getString(R.string.ip_adress)+"API_Func/get_status_by_deviceid.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJson(response,2);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StatusFragment.this.getContext(),"ERRO ",Toast.LENGTH_SHORT);
            }
        }){@Override
        protected Map<String, String> getParams(){
            Map<String, String> params = new HashMap<String, String>();
            params.put("deviceid",String.valueOf(deviceId));
            return params;
        }};
        RequestQueue mQueue = Volley.newRequestQueue(StatusFragment.this.getContext());
        mQueue.add(stringRequest);
    }

}