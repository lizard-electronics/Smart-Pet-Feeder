package com.example.spf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceSelectedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceSelectedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int depositLevel;

    String nome,local;

    int id, ref_agua, ref_comida;

    ProgressBar depositProgressBar;
    ProgressBar foodProgressBar;
    ProgressBar waterProgressBar;

    public DeviceSelectedFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DeviceSelectedFragment newInstance(String param1, String param2) {
        DeviceSelectedFragment fragment = new DeviceSelectedFragment();
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
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
            nome = getArguments().getString("name");
            local = getArguments().getString("local");
            id = getArguments().getInt("id");
            ref_agua = getArguments().getInt("ref_agua");
            ref_comida = getArguments().getInt("ref_comida");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_device_selected, container, false);
        //Variables from layout
        View view = inflater.inflate(R.layout.fragment_device_selected, container, false);
        Button btn = (Button) view.findViewById(R.id.button_device_selected);
        depositProgressBar = view.findViewById(R.id.depositLevelProgressBar);
        foodProgressBar = view.findViewById(R.id.foodLevelProgressBar);
        waterProgressBar = view.findViewById(R.id.waterLevelProgressBar);
        TextView deviceSelectedName = view.findViewById(R.id.deviceSelectedName);
        TextView deviceSelectedPlace = view.findViewById(R.id.textViewPlace);
        //Set vallues to the progress bars
        changeProgressBar(depositProgressBar, 100);
        changeProgressBar(foodProgressBar,ref_comida);
        changeProgressBar(waterProgressBar,ref_agua);
        //Variables witch came from the bundle
        if(nome.equals(null)||nome.equals(""))
        {}else {
            deviceSelectedName.setText(nome);
        }
        if(local.equals(null)||local.equals(""))
        {}else {
            deviceSelectedPlace.setText(local);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr=getFragmentManager().beginTransaction();
                fr.replace(R.id.navHostFragment,new FirstFragment());
                fr.commit();
            }
        });
        return view;
    }
    public void changeProgressBar(ProgressBar progressBar, int percentage){

        progressBar.setProgress(percentage);
        progressBar.setMax(100);
    }
}

