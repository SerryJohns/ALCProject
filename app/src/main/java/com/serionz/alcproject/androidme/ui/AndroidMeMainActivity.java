package com.serionz.alcproject.androidme.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.serionz.alcproject.R;
import com.serionz.alcproject.androidme.data.AndroidImageAssets;

public class AndroidMeMainActivity extends AppCompatActivity implements MasterListFragment.OnImageClickListener {

    private int headIndex;
    private int bodyIndex;
    private int legIndex;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_me_main);

        if (findViewById(R.id.android_me_linear_layout) != null) {
            mTwoPane = true;
            GridView gridView = (GridView) findViewById(R.id.grid_view_images);
            gridView.setNumColumns(2);

            Button btnNext = (Button) findViewById(R.id.next_btn);
            btnNext.setVisibility(View.GONE);

            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                BodyPartFragment headFragment = new BodyPartFragment();
                headFragment.setImageIds(AndroidImageAssets.getHeads());
                fragmentManager.beginTransaction()
                        .add(R.id.container_head, headFragment)
                        .commit();

                BodyPartFragment bodyFragment = new BodyPartFragment();
                bodyFragment.setImageIds(AndroidImageAssets.getBodies());
                fragmentManager.beginTransaction()
                        .add(R.id.container_body, bodyFragment)
                        .commit();

                BodyPartFragment legFragment = new BodyPartFragment();
                legFragment.setImageIds(AndroidImageAssets.getLegs());
                fragmentManager.beginTransaction()
                        .add(R.id.container_leg, legFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        MasterListFragment masterListFragment = new MasterListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, masterListFragment)
                .commit();
    }

    @Override
    public void onImageSelected(int position) {
        Toast.makeText(this, "Clicked: " + position, Toast.LENGTH_SHORT).show();
        int bodyPartType = position / 12;
        int bodyPartIndex = position - bodyPartType * 12;

        if (mTwoPane) {
            BodyPartFragment newFragment = new BodyPartFragment();

            switch (bodyPartType) {
                case 0:
                    newFragment.setImageIds(AndroidImageAssets.getHeads());
                    newFragment.setImageIndex(bodyPartIndex);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container_head, newFragment)
                            .commit();
                    break;
                case 1:
                    newFragment.setImageIds(AndroidImageAssets.getBodies());
                    newFragment.setImageIndex(bodyPartIndex);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container_body, newFragment)
                            .commit();
                    break;
                case 2:
                    newFragment.setImageIds(AndroidImageAssets.getLegs());
                    newFragment.setImageIndex(bodyPartIndex);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container_leg, newFragment)
                            .commit();
                    break;
                default:
                    break;
            }
        } else {
            switch (bodyPartType) {
                case 0:
                    headIndex = bodyPartIndex;
                    break;
                case 1:
                    bodyIndex = bodyPartIndex;
                    break;
                case 2:
                    legIndex = bodyPartIndex;
                    break;
                default:
                    break;
            }

            Bundle bodyPartsBundle = new Bundle();
            bodyPartsBundle.putInt("headIndex", headIndex);
            bodyPartsBundle.putInt("bodyIndex", bodyIndex);
            bodyPartsBundle.putInt("legIndex", legIndex);

            final Intent intent = new Intent(this, AndroidMeActivity.class);
            intent.putExtras(bodyPartsBundle);

            Button nextBtn = (Button) findViewById(R.id.next_btn);
            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(intent);
                }
            });
        }
    }

}
