package com.appTec.RegistrateApp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Assistance;
import com.appTec.RegistrateApp.models.Permission;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AssistanceListAdapter extends BaseAdapter {
    private static LayoutInflater layoutInflater;
    Context context;
    ArrayList<Assistance> lstAssistances;
    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    public AssistanceListAdapter(Context context, ArrayList<Assistance> lstAssistances){
        this.context = context;
        this.lstAssistances = lstAssistances;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = layoutInflater.inflate(R.layout.assistance_element, null);
        TextView txtEventType = (TextView) view.findViewById(R.id.txtEventType);
        TextView txtAssistanceDate = (TextView) view.findViewById(R.id.txtAssistanceDate);
        TextView txtAssistanceTime = (TextView) view.findViewById(R.id.txtAssistanceTime);
        String strDateTime =  dateformat.format(lstAssistances.get(position).getFecha().getTime());
        txtEventType.setText(lstAssistances.get(position).getEvento());
        txtAssistanceDate.setText(strDateTime.split(" ")[0]);
        txtAssistanceTime.setText(strDateTime.split(" ")[1]);
        return view;
    }

    @Override
    public int getCount() {
        return lstAssistances.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
