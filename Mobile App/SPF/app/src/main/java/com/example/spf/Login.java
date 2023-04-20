package com.example.spf;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Login extends AppCompatActivity {
    EditText usernameInput;
    EditText passwordInput;
    TextInputLayout usernameBoxEdit;
    TextView txtSignUp;
    Button logInButton;
    SharedPreferences userSP;
    ArrayList<String> usernames = new ArrayList<String>();
    ArrayList<String> passwords = new ArrayList<String>();
    ArrayList<String> mails = new ArrayList<String>();
    ArrayList<String> phones = new ArrayList<String>();
    ArrayList<Integer> ids = new ArrayList<Integer>();

    String mail="";
    String phone="";
    int id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInButton=findViewById(R.id.logInButton);
        txtSignUp=findViewById(R.id.txtSignUp);
        usernameInput=findViewById(R.id.loginUsername);
        passwordInput=findViewById(R.id.logInPassword);
        usernameBoxEdit=findViewById(R.id.editLogInUsername);

        //Variable to store user data into the app for future use without Wi-Fi conection
        userSP = getSharedPreferences("User", Context.MODE_PRIVATE);
        getUsers();

        //Link para mudar para a pagina de registo
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Regist.class);
                startActivity(intent);
                finish();
            }
        });

        //Código para mudar para realizar o LogIn
        logInButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View view) {
                getUsers();
                System.out.println(usernameInput.getText());
                Intent intent = new Intent(Login.this, MainActivity.class);
                //Validação do formulário de LogIn
                if(usernameInput.getText().toString().equals("") || passwordInput.getText().toString().equals(""))
                {
                    passwordInput.setText("");usernameBoxEdit.setHint("Mandatory requirement!!");
                    usernameInput.setText("");usernameInput.setHint("Mandatory requirement!!");
                }else{
                    if(checkFormWithDB(usernameInput.getText().toString(),passwordInput.getText().toString()))
                    {
                        SharedPreferences.Editor userEditor = userSP.edit();
                        userEditor.putInt("id",id);
                        userEditor.putString("name",usernameInput.getText().toString());
                        userEditor.putString("password",passwordInput.getText().toString());
                        userEditor.putString("mail",mail);
                        userEditor.putString("phone",phone);
                        if(userEditor.commit())
                            Toast.makeText(Login.this,"User data saved!",Toast.LENGTH_SHORT).show();

                        startActivity(intent);
                        finish();
                    }
                    usernames.clear();
                    passwords.clear();
                    mails.clear();
                    phones.clear();
                }
            }
        });
    }
    public void getUsers(){
        String url = getString(R.string.ip_adress)+"API_Func/get_users.php";
        RequestQueue mQueue = Volley.newRequestQueue(Login.this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("users");
                            for(int i=0; i < jsonArray.length(); i++) {
                                JSONObject users = jsonArray.getJSONObject(i);
                                usernames.add(users.getString("nome"));
                                passwords.add(users.getString("password_utilizador"));
                                mails.add(users.getString("email"));
                                phones.add(users.getString("telefone"));
                                ids.add(users.getInt("ID_Utilizador"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this,"ERROR ",Toast.LENGTH_SHORT);
            }
        });
        mQueue.add(objectRequest);
    }

    public boolean checkFormWithDB(String username, String password){
        boolean result=false;
        for(int i=0;i<usernames.size();i++){
            if(username.equals(usernames.get(i))&&password.equals(passwords.get(i))) {
                result = true;
                phone=phones.get(i);
                mail=mails.get(i);
                id=ids.get(i);
            }
        }
        return result;
    }
}