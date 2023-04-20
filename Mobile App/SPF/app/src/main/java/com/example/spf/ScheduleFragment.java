package com.example.spf;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ScheduleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public SharedPreferences userSP;
    TableLayout t1;
    TableRow tr;
    TextView timeText;
    TextView devicenameTextView;
    TextView nDosesTextView;
    CheckBox checkBox;
    ArrayList<CheckBox> checkBoxesList = new ArrayList<CheckBox>();

    Button mealsButn;
    Button addButton;

    EditText numberOfDoses;
    EditText hourOfMeal;
    EditText minuteOfMeal;

    Spinner devicesSpinner;

    TableLayout[] tables;

    ArrayList<String> hourMealList = new ArrayList<String>();
    ArrayList<Integer> nDosesList = new ArrayList<Integer>();
    ArrayList<Integer> ids = new ArrayList<Integer>();
    ArrayList<Integer> devicesId = new ArrayList<Integer>();
    ArrayList<String> devicesNames = new ArrayList<String>();
    ArrayList<String> devicesNamesForShow = new ArrayList<String>();

     public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_schedule,container,false);
        setViews(view);
        getDevices();
        getMeals(view);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealsButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> idsSelected = new ArrayList<Integer>();
                for(int i=0; i<checkBoxesList.size();i++)
                {
                    if(checkBoxesList.get(i).isChecked())
                        idsSelected.add(ids.get(i));
                }
                deleteMeal(idsSelected, v);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hourOfMeal.getText().toString().equals("")||minuteOfMeal.getText().toString().equals("")||numberOfDoses.getText().toString().equals("")){}
                else{addMeals(v);}
            }
        });
    }

    public void setViews(View view){
        mealsButn=view.findViewById(R.id.checkMealsBtn);
        t1= (TableLayout) view.findViewById(R.id.meal_table_layout);
        t1.setColumnStretchable(0,true);
        numberOfDoses=view.findViewById(R.id.meal_edit_ndoses);
        hourOfMeal=view.findViewById(R.id.meal_edit_hour);
        minuteOfMeal=view.findViewById(R.id.meal_edit_min);
        addButton=view.findViewById(R.id.addMealsBtn);
        devicesSpinner=view.findViewById(R.id.spinner);
    }

    public void setMealsContent(View view){
        for(int i=0;i<hourMealList.size();i++){
            checkBox = new CheckBox(view.getContext());
            nDosesTextView = new TextView(view.getContext());
            timeText = new TextView(view.getContext());
            devicenameTextView = new TextView(view.getContext());
            tr = new TableRow(view.getContext());
            tr.setPadding(0,5,0,5);

            nDosesTextView.setTextSize(20);
            nDosesTextView.setTypeface(null, Typeface.BOLD);
            timeText.setTextSize(20);
            timeText.setTypeface(null, Typeface.BOLD);

            devicenameTextView.setTextSize(20);
            devicenameTextView.setTypeface(null, Typeface.BOLD);

            checkBox.setId(ids.get(i));
            checkBoxesList.add(checkBox);

            nDosesTextView.setGravity(Gravity.CENTER);
            devicenameTextView.setText(devicesNamesForShow.get(i));
            timeText.setText(String.valueOf(hourMealList.get(i)));
            nDosesTextView.setText(String.valueOf(nDosesList.get(i)));

            t1.setColumnStretchable(2, true);
            tr.addView(checkBox);
            tr.addView(timeText);
            tr.addView(nDosesTextView);
            tr.addView(devicenameTextView);
            t1.addView(tr);
        }
    }

    public void getMeals(View view){
        String url = getString(R.string.ip_adress)+"API_Func/get_meals.php";
        StringRequest objectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response, view);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ScheduleFragment.this.getContext(), "Meal not deleted", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid",String.valueOf(userSP.getInt("id",0)));
                return params;
            }
        };
        RequestQueue mQueue = Volley.newRequestQueue(ScheduleFragment.this.getContext());
        mQueue.add(objectRequest);
    }
    public void parseJson(String response, View view){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("meals");
            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject meals = jsonArray.getJSONObject(i);
                String time = meals.getString("Hora");
                hourMealList.add(time.substring(0,5));
                nDosesList.add(meals.getInt("Dosagem_racao"));
                ids.add(meals.getInt("ID_Horarios"));
                devicesNamesForShow.add(meals.getString("Nome"));
            }
            setMealsContent(view);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void addMeals(View view){
        String apiUrl= getString(R.string.ip_adress)+"API_Func/add_meal.php";
        StringRequest objectRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ScheduleFragment.this.getContext(), "Meal added", Toast.LENGTH_SHORT).show();
                t1.removeAllViews();
                hourMealList.clear();
                nDosesList.clear();
                ids.clear();
                checkBoxesList.clear();
                getMeals(view);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ScheduleFragment.this.getContext(), "Meal not deleted", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("hour",hourOfMeal.getText().toString()+":"+minuteOfMeal.getText().toString()+":10");
                params.put("min",minuteOfMeal.getText().toString());
                params.put("ndoses",numberOfDoses.getText().toString());
                params.put("id_device",devicesId.get(devicesSpinner.getSelectedItemPosition()).toString());
                return params;
            }
        };
        RequestQueue mQueue = Volley.newRequestQueue(ScheduleFragment.this.getContext());
        mQueue.add(objectRequest);
    }
    public void deleteMeal(ArrayList<Integer>idsSend, View view){
        String apiUrl= getString(R.string.ip_adress)+"API_Func/delete_meal.php";
        StringRequest objectRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ScheduleFragment.this.getContext(), "Meal deleted", Toast.LENGTH_SHORT).show();
                t1.removeAllViews();
                hourMealList.clear();
                nDosesList.clear();
                ids.clear();
                checkBoxesList.clear();
                devicesNamesForShow.clear();
                getMeals(view);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ScheduleFragment.this.getContext(), "Meal not deleted", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                String data="";
                for(int i=0; i<idsSend.size();i++)
                {
                    data +="horarios.ID_Horarios = "+String.valueOf(idsSend.get(i))+" ||";
                }
                data = data.substring(0,data.length()-3);
                params.put("data",data);
                return params;
            }
        };
        RequestQueue mQueue = Volley.newRequestQueue(ScheduleFragment.this.getContext());
        mQueue.add(objectRequest);
    }

    public void getDevices(){
        String url = getString(R.string.ip_adress)+"API_Func/get_devices_by_userid.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJson2(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ScheduleFragment.this.getContext(),"ERROR ",Toast.LENGTH_SHORT);
            }
        }){@Override
        protected Map<String, String> getParams(){
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid",String.valueOf(userSP.getInt("id",0)));
            return params;
        }};
        RequestQueue mQueue = Volley.newRequestQueue(ScheduleFragment.this.getContext());
        mQueue.add(stringRequest);
    }
    public void parseJson2(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("devices");
            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject devices = jsonArray.getJSONObject(i);
                devicesNames.add(devices.getString("Nome"));
                devicesId.add(devices.getInt("ID_comedouro"));
            }
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(ScheduleFragment.this.getContext(), android.R.layout.simple_list_item_1,devicesNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            devicesSpinner.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}