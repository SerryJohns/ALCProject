package com.serionz.alcproject.androidme.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.serionz.alcproject.R;
import com.serionz.alcproject.androidme.data.AndroidImageAssets;

/**
 * Created by johnpaulseremba on 03/04/2018.
 */

public class MasterListFragment extends Fragment {
    private static final String TAG = MasterListFragment.class.getSimpleName();
    private MasterListAdapter mMasterListAdapter;
    public OnImageClickListener mCallback;

    public MasterListFragment() {
        // Empty Fragment container
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnImageClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master_list, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.grid_view_images);
        mMasterListAdapter = new MasterListAdapter(getContext(), AndroidImageAssets.getAll());
        gridView.setAdapter(mMasterListAdapter);

        // Set a click listener on the gridView and trigger the call back onImageSelected when an item is clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onImageSelected(position);
            }
        });
        return view;
    }

    public interface OnImageClickListener {
        void onImageSelected(int position);
    }
}