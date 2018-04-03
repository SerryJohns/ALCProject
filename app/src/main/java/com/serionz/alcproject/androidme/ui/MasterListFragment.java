package com.serionz.alcproject.androidme.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.serionz.alcproject.R;
import com.serionz.alcproject.androidme.data.AndroidImageAssets;

/**
 * Created by johnpaulseremba on 03/04/2018.
 */

public class MasterListFragment extends Fragment {
    private MasterListAdapter mMasterListAdapter;

    public MasterListFragment() {
        // Empty Fragment container
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master_list, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.grid_view_images);
        mMasterListAdapter = new MasterListAdapter(getContext(), AndroidImageAssets.getAll());
        gridView.setAdapter(mMasterListAdapter);
        return view;
    }
}