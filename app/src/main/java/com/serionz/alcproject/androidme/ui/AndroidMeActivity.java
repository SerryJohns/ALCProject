package com.serionz.alcproject.androidme.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.serionz.alcproject.R;
import com.serionz.alcproject.androidme.data.AndroidImageAssets;

public class AndroidMeActivity extends AppCompatActivity {
    private static final String TAG = AndroidMeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_me);

        if (savedInstanceState == null) {
            BodyPartFragment headFragment = new BodyPartFragment();
            headFragment.setImageIds(AndroidImageAssets.getHeads());
            headFragment.setImageIndex(1);

            FragmentManager fragmentManager = getSupportFragmentManager();

            // Display Head Fragment
            fragmentManager.beginTransaction()
                    .add(R.id.container_head, headFragment)
                    .commit();

            // Display Body Fragment
            BodyPartFragment bodyFragment = new BodyPartFragment();
            bodyFragment.setImageIds(AndroidImageAssets.getBodies());
            fragmentManager.beginTransaction()
                    .add(R.id.container_body, bodyFragment)
                    .commit();

            // Display Leg Fragment
            BodyPartFragment legFragment = new BodyPartFragment();
            legFragment.setImageIds(AndroidImageAssets.getLegs());
            fragmentManager.beginTransaction()
                    .add(R.id.container_leg, legFragment)
                    .commit();
        }
    }

}
