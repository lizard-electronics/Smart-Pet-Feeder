package com.example.spf;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListDevices extends ArrayAdapter<Devices>{

    public ListDevices (Context context, ArrayList<Devices> devicesArrayList){
        super(context,R.layout.devices_list_item,devicesArrayList);
    }

}
