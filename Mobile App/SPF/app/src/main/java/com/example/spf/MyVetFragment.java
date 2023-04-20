package com.example.spf;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class MyVetFragment extends Fragment {

    private TextView nameTextView;
    private TextView ageTextView;
    private TextView typeTextView;
    private TextView weightTextView;
    private EditText nameInput;
    private EditText ageInput;
    private EditText typeInput;
    private EditText weightInput;
    Spinner spinnerAnimal;
    Spinner spinnerFreeDevices;
    Button addAnimal;

    ArrayList<String> animalsNamesList=new ArrayList<>();
    ArrayList<Animal> animalList = new ArrayList<>();
    ArrayList<Integer> comedouroIdList = new ArrayList<>();
    ArrayList<Integer> freeComedouroIdList = new ArrayList<>();
    ArrayList<String> freeComedouroName = new ArrayList<>();

    public SharedPreferences userSP;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyVetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyVetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyVetFragment newInstance(String param1, String param2) {
        MyVetFragment fragment = new MyVetFragment();
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
        View view=inflater.inflate(R.layout.fragment_my_vet,container,false);
        setViews(view);
        getAnimals();
        getFreeDevices();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = new Bundle();

        spinnerAnimal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0) {
                    nameTextView.setText("");
                    typeTextView.setText("");
                    weightTextView.setText("");
                    ageTextView.setText("");
                }else{
                    nameTextView.setText(animalList.get(position-1).getNome());
                    typeTextView.setText(animalList.get(position-1).getTipo());
                    weightTextView.setText(String.valueOf(animalList.get(position-1).getPeso()));
                    ageTextView.setText(String.valueOf(animalList.get(position-1).getIdade()));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO Auto-genarated method stub
            }
        });

        addAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnimal();
            }
        });

    }

    public void getAnimals(){
        String url = getString(R.string.ip_adress)+"API_Func/get_animals_by_userid.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJson(response,1);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyVetFragment.this.getContext(),"ERRO ",Toast.LENGTH_SHORT);
            }
        }){@Override
        protected Map<String, String> getParams(){
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid",String.valueOf(userSP.getInt("id",0)));
            return params;
        }};
        RequestQueue mQueue = Volley.newRequestQueue(MyVetFragment.this.getContext());
        mQueue.add(stringRequest);
    }

    public void getFreeDevices(){
        String url = getString(R.string.ip_adress)+"API_Func/get_free_devices_by_userid_animal.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJson(response,2);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyVetFragment.this.getContext(),"ERRO ",Toast.LENGTH_SHORT);
            }
        }){@Override
        protected Map<String, String> getParams(){
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid",String.valueOf(userSP.getInt("id",0)));
            return params;
        }};
        RequestQueue mQueue = Volley.newRequestQueue(MyVetFragment.this.getContext());
        mQueue.add(stringRequest);
    }

    public void addAnimal() {
        String url = getString(R.string.ip_adress)+"API_Func/add_animal_by_deviceid.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        freeComedouroIdList.clear();
                        freeComedouroName.clear();
                        animalsNamesList.clear();
                        animalList.clear();
                        comedouroIdList.clear();
                        getAnimals();
                        getFreeDevices();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyVetFragment.this.getContext(),"ERRO ",Toast.LENGTH_SHORT);
            }
        }){@Override
        protected Map<String, String> getParams(){
            Map<String, String> params = new HashMap<String, String>();
            params.put("animal_name",nameInput.getText().toString());
            params.put("animal_type",typeInput.getText().toString());
            params.put("animal_age",String.valueOf(ageInput.getText().toString()));
            params.put("animal_wheight",String.valueOf(weightInput.getText().toString()));
            params.put("id_device",String.valueOf(freeComedouroIdList.get(spinnerFreeDevices.getSelectedItemPosition())));
            return params;
        }};
        RequestQueue mQueue = Volley.newRequestQueue(MyVetFragment.this.getContext());
        mQueue.add(stringRequest);
    }

    public void parseJson(String response, int o){
        try {
            switch (o) {
                case 1:
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("animais");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject animals = jsonArray.getJSONObject(i);
                    animalsNamesList.add(animals.getString("nome"));
                    animalList.add(new Animal(animals.getString("nome"),
                            animals.getString("tipo"),
                            Integer.valueOf(animals.getString("peso")),
                            Integer.valueOf(animals.getString("idade"))));
                    comedouroIdList.add(animals.getInt("ID_comedouro"));
                }
                updateSpinner(o);
                break;
                case 2:
                    JSONObject jsonObject2 = new JSONObject(response);
                    JSONArray jsonArray2 = jsonObject2.getJSONArray("free_devices");
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        JSONObject devices = jsonArray2.getJSONObject(i);
                        freeComedouroIdList.add(devices.getInt("ID_comedouro"));
                        freeComedouroName.add(devices.getString("nome"));
                    }
                    updateSpinner(o);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateSpinner(int o){
        switch (o) {
            case 1:
            animalsNamesList.add(0, "");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyVetFragment.this.getContext(), R.layout.list_selected_item, animalsNamesList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAnimal.setAdapter(adapter);
            break;
            case 2:
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MyVetFragment.this.getContext(), R.layout.list_selected_item, freeComedouroName);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFreeDevices.setAdapter(adapter2);
            break;
        }
    }

    public void setViews(View view){
        nameTextView=view.findViewById(R.id.nameTextView);
        ageTextView=view.findViewById(R.id.ageTextView);
        typeTextView=view.findViewById(R.id.typeTextView);
        weightTextView=view.findViewById(R.id.weightTextView);
        spinnerAnimal=view.findViewById(R.id.spinnerAnimal);
        nameInput=view.findViewById(R.id.editAnimalName);
        ageInput=view.findViewById(R.id.editAnimalAge);
        weightInput=view.findViewById(R.id.editAnimalWeight);
        typeInput=view.findViewById(R.id.editAnimalType);
        spinnerFreeDevices=view.findViewById(R.id.spinnerFreeDevices);
        addAnimal=view.findViewById(R.id.buttonAddAnimal);
    }
}