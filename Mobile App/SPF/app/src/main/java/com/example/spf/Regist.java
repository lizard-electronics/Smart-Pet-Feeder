package com.example.spf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Regist extends AppCompatActivity {

    TextView txtSignIn;
    EditText usernameInput;
    EditText passwordInput;
    EditText passwordConfirmInput;
    EditText emailInput;
    EditText adressInput;
    EditText phoneInput;

    Button registButton;

    TextInputLayout userInputLayout;
    TextInputLayout addressInputLayout;
    TextInputLayout passwordInputLayout;
    TextInputLayout emailInputLayout;
    TextInputLayout phoneInputLayout;

    boolean valid=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        getSupportActionBar().hide();

        txtSignIn=findViewById(R.id.txtSignin);
        usernameInput=findViewById(R.id.registUsername);
        emailInput=findViewById(R.id.registEmail);
        adressInput=findViewById(R.id.registAdress);
        phoneInput=findViewById(R.id.registPhone);
        registButton=findViewById(R.id.registButton);
        passwordInput=findViewById(R.id.registPassword);
        passwordConfirmInput=findViewById(R.id.registPasswordConfirm);

        userInputLayout=findViewById(R.id.editRegistUsername);
        addressInputLayout=findViewById(R.id.editRegistAdress);
        emailInputLayout=findViewById(R.id.editRegistEmail);
        emailInputLayout=findViewById(R.id.editRegistEmail);
        phoneInputLayout=findViewById(R.id.editRegistPhone);
        passwordInputLayout=findViewById(R.id.editRegistPassword);

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Regist.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validação caso não tenha algo escrito
                if(usernameInput.getText().toString().equals("")){
                    userInputLayout.setDefaultHintTextColor(ColorStateList.valueOf(getColor(R.color.red)));
                }else
                if(emailInput.getText().toString().equals("")){
                    emailInputLayout.setDefaultHintTextColor(ColorStateList.valueOf(getColor(R.color.red)));
                }else
                if(phoneInput.getText().toString().equals("")||phoneInput.getText().toString().length()!=9){
                    phoneInputLayout.setDefaultHintTextColor(ColorStateList.valueOf(getColor(R.color.red)));
                }else
                if(adressInput.getText().toString().equals("")){
                    addressInputLayout.setDefaultHintTextColor(ColorStateList.valueOf(getColor(R.color.red)));
                }else
                if(passwordInput.getText().toString().equals("")){
                    passwordInputLayout.setDefaultHintTextColor(ColorStateList.valueOf(getColor(R.color.red)));
                }else
                if(passwordInput.getText().toString().equals(passwordConfirmInput.getText().toString()))
                {
                    registUserDB();
                    Intent intent = new Intent(Regist.this, Login.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Password's must match!",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void registUserDB(){
        String apiUrl= getString(R.string.ip_adress)+"API_Func/insert_user.php";
        StringRequest objectRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Regist.this, "Welcome "+usernameInput.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Regist.this,"Failed", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("name",usernameInput.getText().toString());
                params.put("mail",emailInput.getText().toString());
                params.put("phone",phoneInput.getText().toString());
                params.put("adress",adressInput.getText().toString());
                params.put("password",passwordInput.getText().toString());
                return params;
            }
        };
        RequestQueue mQueue = Volley.newRequestQueue(Regist.this);
        mQueue.add(objectRequest);
    }
}