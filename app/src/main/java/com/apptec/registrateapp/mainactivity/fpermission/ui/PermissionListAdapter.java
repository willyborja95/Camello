package com.apptec.registrateapp.mainactivity.fpermission.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.lifecycle.LiveData;

import com.apptec.registrateapp.models.PermissionModel;

import java.util.List;

@Deprecated
public class PermissionListAdapter extends BaseAdapter {
    /**
     * PermissionListAdapter
     * <p>
     * Is the adapter for each element in the permission list at the PermissionFragment
     *
     * @return
     */


    // Data source
    LiveData<List<PermissionModel>> listLiveData;


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
