package com.serionz.alcproject.androidme.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.serionz.alcproject.R;

public class AndroidMeMainActivity extends AppCompatActivity implements MasterListFragment.OnImageClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_me_main);
        MasterListFragment masterListFragment = new MasterListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, masterListFragment)
                .commit();
    }

    @Override
    public void onImageSelected(int position) {
        Toast.makeText(this, "Clicked: " + position, Toast.LENGTH_SHORT).show();
    }
}
