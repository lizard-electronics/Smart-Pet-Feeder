package com.example.spf;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SharedPreferences userSP;
    //SharedPreferences.Editor userUpdateSP= userSP.edit();
    EditText userEditText;
    EditText mailEditText;
    EditText phoneEditText;
    Button updateButton;
    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userSP=this.getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        View view=inflater.inflate(R.layout.fragment_my_profile,container,false);
        findViews(view);

        userEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(userEditText.getText().toString().equals("")&&mailEditText.getText().toString().equals("")&&phoneEditText.getText().toString().equals("")){
                    updateButton.setVisibility(View.GONE);
                }else{
                    updateButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(userEditText.getText().toString().equals("")&&mailEditText.getText().toString().equals("")&&phoneEditText.getText().toString().equals("")){
                    updateButton.setVisibility(View.GONE);
                }else{
                    updateButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(userEditText.getText().toString().equals("")&&mailEditText.getText().toString().equals("")&&phoneEditText.getText().toString().equals("")){
                    updateButton.setVisibility(View.GONE);
                }else{
                    updateButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences.Editor userUpdateSP= userSP.edit();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userEditText.getText().toString().equals("")){}else{
                    userUpdateSP.putString("name",userEditText.getText().toString());}
                if(mailEditText.getText().toString().equals("")){}else{
                    userUpdateSP.putString("mail",mailEditText.getText().toString());}
                if(phoneEditText.getText().toString().equals("")){}else{
                userUpdateSP.putString("phone",phoneEditText.getText().toString());}
                if(userUpdateSP.commit()) {
                    Toast.makeText(view.getContext(), "User Updated!", Toast.LENGTH_SHORT).show();
                    updateUserDB();
                    setHints();
                }
            }
        });
    }
    public void updateUserDB(){
        String url =getString(R.string.ip_adress)+"API_Func/update_user_by_id.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MyProfileFragment.this.getContext(),"User Info Updated",Toast.LENGTH_SHORT);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyProfileFragment.this.getContext(),"ERRO ",Toast.LENGTH_SHORT);
            }
        }){@Override
        protected Map<String, String> getParams(){
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", String.valueOf(userSP.getInt("id",0)));

            if(mailEditText.getText().toString().equals("")){
                params.put("mail",userSP.getString("mail","NULL"));
            }else {
            params.put("mail",mailEditText.getText().toString());}

            if(phoneEditText.getText().toString().equals("")){
                params.put("phone",userSP.getString("phone","NULL"));
            }else {
                params.put("phone",phoneEditText.getText().toString());}

            if(userEditText.getText().toString().equals("")){
                params.put("name",userSP.getString("name","NULL"));
            }else {
                params.put("name",userEditText.getText().toString());}

            return params;
        }};
        RequestQueue mQueue = Volley.newRequestQueue(MyProfileFragment.this.getContext());
        mQueue.add(stringRequest);
    }

    public void findViews(View view){
        userEditText = view.findViewById(R.id.editMyProfileUsername);
        mailEditText = view.findViewById(R.id.editMyProfileEmail);
        phoneEditText = view.findViewById(R.id.editMyProfilePhone);
        updateButton = view.findViewById(R.id.updateButton);
        setHints();
    }

    public void setHints(){
        userEditText.setHint(userSP.getString("name",getString(R.string.user)));
        mailEditText.setHint(userSP.getString("mail","NOT FOUND"));
        phoneEditText.setHint(userSP.getString("phone","0"));
    }
}