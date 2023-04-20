package com.example.spf;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.spf.databinding.FragmentFirstBinding;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FirstFragment extends Fragment {
    private FragmentFirstBinding binding;

    private ProgressDialog progressDialog;
    public SharedPreferences userSP;

    private ActivityResultLauncher<ScanOptions> barLaucher;

    ArrayList<Devices> devicesArrayList=new ArrayList<>();
    ArrayList<String> devicesNames=new ArrayList<String>();
    ArrayList<String> devicesLocal=new ArrayList<String>();
    ArrayList<Integer> devicesId=new ArrayList<Integer>();
    ArrayList<Integer> devicesRef_agua=new ArrayList<Integer>();
    ArrayList<Integer> devicesRef_comida=new ArrayList<Integer>();

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSP = this.getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.fragment_first,container,false);
        getDevices();
        Button btn=(Button)view.findViewById(R.id.button_first);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

        //return binding.getRoot();
        //ActivityResultLauncher<ScanOptions> barLaucher
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = new Bundle();
        listView =view.findViewById(R.id.devicesList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeviceSelectedFragment deviceSelectedFragment = new DeviceSelectedFragment();
                deviceSelectedFragment.setArguments(args);
                FragmentTransaction fr=getFragmentManager().beginTransaction();
                args.putString("name",devicesArrayList.get(position).getNome());
                args.putString("local",devicesArrayList.get(position).getLocal());
                args.putInt("id",devicesArrayList.get(position).getId());
                args.putInt("ref_agua",devicesArrayList.get(position).getRef_agua());
                args.putInt("ref_comida",devicesArrayList.get(position).getRef_comida());
                fr.replace(R.id.navHostFragment,deviceSelectedFragment);
                fr.commit();
            }
        });
        barLaucher = registerForActivityResult(new ScanContract(), result -> {
            if(result.getContents() !=null)
            {
                addDevice(result.getContents());
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void clearArrayLists(){
        devicesArrayList.clear();
        devicesNames.clear();
        devicesLocal.clear();
        devicesId.clear();
        devicesRef_agua.clear();
        devicesRef_comida.clear();
    }

    //Code for a Select with params ---------------------------------------------------------------
    public void getDevices(){
        clearArrayLists();
        String url = getString(R.string.ip_adress)+"API_Func/get_devices_by_userid.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJson(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FirstFragment.this.getContext(),"ERRO ",Toast.LENGTH_SHORT);
            }
        }){@Override
        protected Map<String, String> getParams(){
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid",String.valueOf(userSP.getInt("id",0)));
            return params;
        }};
        RequestQueue mQueue = Volley.newRequestQueue(FirstFragment.this.getContext());
        mQueue.add(stringRequest);
    }

    public void parseJson(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("devices");
            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject devices = jsonArray.getJSONObject(i);
                devicesNames.add(devices.getString("Nome"));
                devicesId.add(devices.getInt("ID_comedouro"));
                devicesLocal.add(devices.getString("Local_comedouro"));
                devicesRef_agua.add(devices.getInt("Nivelref_agua"));
                devicesRef_comida.add(devices.getInt("Nivelref_racao"));
            }
            updateListView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------------------------------------------------------------------------------------

    public void updateListView(){
        for(int i=0;i<devicesNames.size();i++)
        {
            Devices device = new Devices(devicesNames.get(i),devicesId.get(i),devicesRef_agua.get(i),devicesLocal.get(i),devicesRef_comida.get(i));
            devicesArrayList.add(device);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(FirstFragment.this.getContext(), android.R.layout.simple_list_item_1,devicesNames);
        listView.setAdapter(adapter);
    }

    public void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Raised up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    public void addDevice(String qrcode){
        try {
            JSONObject jsonObject = new JSONObject(qrcode);
            AlertDialog.Builder builder = new AlertDialog.Builder(FirstFragment.this.getContext());
            builder.setTitle("ADD THIS DEVICE ?");
            builder.setMessage("Name: "+jsonObject.getString("nome")+", Local:"+jsonObject.getString("local"));
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clearArrayLists();
                    String url = getString(R.string.ip_adress)+"API_Func/add_devices_by_userid_deviceid.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    getDevices();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(FirstFragment.this.getContext(),"ERRO ",Toast.LENGTH_SHORT);
                        }
                    }){@Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("userid",String.valueOf(userSP.getInt("id",0)));
                        try {
                            params.put("deviceid",String.valueOf(jsonObject.getInt("ID_comedouro")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return params;
                    }};
                    RequestQueue mQueue = Volley.newRequestQueue(FirstFragment.this.getContext());
                    mQueue.add(stringRequest);
                    dialog.dismiss();
                }
            }).show();
            //updateListView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}