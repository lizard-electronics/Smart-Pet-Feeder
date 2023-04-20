package com.example.spf;


import okhttp3.OkHttpClient;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;



import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    int count=0;

    SharedPreferences userSP;
    ImageView notificationImage;

    ArrayList<Notificacao> notifications = new ArrayList<Notificacao>();
    ArrayList<Integer> comedouroIdList = new ArrayList<Integer>();

    Timer timer = new Timer();
    final Handler handler = new Handler();
    TimerTask doAsynchronousTask;

    int startTimer=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        final OkHttpClient client = new OkHttpClient();

        notificationImage = findViewById(R.id.notificationImage);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        userSP=this.getSharedPreferences("User", Context.MODE_PRIVATE);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        NavController navController = Navigation.findNavController(this,R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        //----------------------------------------------------------------------------------------
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView)headerView.findViewById(R.id.usernamePlaceHolder);
        navUsername.setText(userSP.getString("name","Not Found"));
        //-----------------------------------------------------------------------------------------
        startTimer();
        notificationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delayTimer();
                startTimer =notifications.size();
                for(int i=0; i<notifications.size();i++){
                count++;
                AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle(notifications.get(i).getName());
                String crit;
                switch (notifications.get(i).getCritical()){
                    case 1: crit="Low";break;
                    case 2: crit="Medium";break;
                    case 3: crit="High";break;
                    default: crit="Unknown";break;
                }
                builder.setMessage(notifications.get(i).getDescrition()+". Device: "+String.valueOf(comedouroIdList.get(i))+". Priority: "+crit);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        count--;
                    }
                });
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNotification(notifications.get(count-1).getId(),comedouroIdList.get(count-1));
                    }
                });
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            startTimer--;
                            if(startTimer==0)
                                startTimer();
                        }
                    });
                builder.show();

            }
                }
        });

    }
    private void delayTimer() {
        doAsynchronousTask.cancel();
        timer.purge();
    }
    private void startTimer() {
        doAsynchronousTask= new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            getNotifications();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 30000);
    }
    private void getNotifications(){
        String url = getString(R.string.ip_adress)+"API_Func/get_mensages_by_userid.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJson(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"ERRO ",Toast.LENGTH_SHORT);
            }
        }){@Override
        protected Map<String, String> getParams(){
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid",String.valueOf(userSP.getInt("id",0)));
            return params;
        }};
        RequestQueue mQueue = Volley.newRequestQueue(MainActivity.this);
        mQueue.add(stringRequest);
    }

    public void parseJson(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("notifications");
            notifications.clear();
            comedouroIdList.clear();
            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject notification = jsonArray.getJSONObject(i);
                notifications.add(new Notificacao(notification.getInt("ID_mensagens"),
                        notification.getInt("criticidade"),
                        notification.getString("nome"),
                        notification.getString("descricao")));
                comedouroIdList.add(notification.getInt("ID_comedouro"));
                notificationImage.setImageDrawable(getResources().getDrawable(R.drawable.notification_icon_any));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteNotification(int id, int device){
        String url = getString(R.string.ip_adress)+"API_Func/delete_mensage_by_id.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        notifications.remove(count-1);
                        comedouroIdList.remove(count-1);
                        count--;
                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();
                        if(notifications.size()==0)
                            notificationImage.setImageDrawable(getResources().getDrawable(R.drawable.notification_icon_none));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){@Override
        protected Map<String, String> getParams(){
            Map<String, String> params = new HashMap<String, String>();
            params.put("mensageid",String.valueOf(id));
            params.put("deviceid",String.valueOf(device));
            return params;
        }};
        RequestQueue mQueue = Volley.newRequestQueue(MainActivity.this);
        mQueue.add(stringRequest);
    }
}