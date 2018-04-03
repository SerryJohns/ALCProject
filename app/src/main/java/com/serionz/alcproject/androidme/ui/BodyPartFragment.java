package com.serionz.alcproject.androidme.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.serionz.alcproject.R;

import java.util.ArrayList;
import java.util.List;

public class BodyPartFragment extends Fragment {

    private static final String TAG = BodyPartFragment.class.getSimpleName();
    private static final String IMAGE_IDS = "image_ids_list";
    private static final String LIST_INDEX = "list_index";
    private List<Integer> mImageIds;
    private int mListIndex;

    public BodyPartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ImageView mImageView;

        if (savedInstanceState != null) {
            mImageIds = savedInstanceState.getIntegerArrayList(IMAGE_IDS);
            mListIndex = savedInstanceState.getInt(LIST_INDEX);
        }

        View view = inflater.inflate(R.layout.fragment_body_part, container, false);
        mImageView = (ImageView) view.findViewById(R.id.img_head);

        if (mImageIds != null) {
            mImageView.setImageResource(mImageIds.get(mListIndex));
        } else {
            Log.e(TAG, "Image List is empty!");
        }

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListIndex < mImageIds.size() - 1) {
                    mListIndex++;
                } else {
                    mListIndex = 0;
                }
                mImageView.setImageResource(mImageIds.get(mListIndex));
            }
        });

        return view;
    }

    public void setImageIds(List<Integer> imageIds) {
        this.mImageIds = imageIds;
    }

    public void setImageIndex(int listIndex) {
        this.mListIndex = listIndex;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(IMAGE_IDS, (ArrayList<Integer>) mImageIds);
        outState.putInt(LIST_INDEX, mListIndex);
    }

}
