package com.example.draft_laundry.LaundryMain;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.draft_laundry.LaundryMain.dorm201.Laundry_Information201;
import com.example.draft_laundry.LaundryMain.dorm202.Laundry_Information202;
import com.example.draft_laundry.LaundryMain.dorm203.Laundry_Information203;
import com.example.draft_laundry.LaundryMain.dorm204.Laundry_Information204;
import com.example.draft_laundry.R;
import com.google.android.material.tabs.TabLayout;

public class LaundryMainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    String dorm_num_str;
    ViewpageAdapter adapter = new ViewpageAdapter(getSupportFragmentManager());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundrymain);

        Intent intent = getIntent();
        // dorm_num_str : 무슨 동인지
        dorm_num_str = intent.getExtras().getString("dorm_num");

        mViewPager = (ViewPager) findViewById(R.id.viewPager_laundry);
        setupViewPager(mViewPager);

        TabLayout tabLayout =(TabLayout) findViewById(R.id.tabLayout_laundry);
        tabLayout.setupWithViewPager(mViewPager);

    }
    public void setupViewPager(ViewPager viewPager) {
        switch (dorm_num_str) {
            case "201":
                adapter.addFragment(new Laundry_Information201(), "Laundry_Information201");
                break;
            case "202":
                adapter.addFragment(new Laundry_Information202(), "Laundry_Information202");
                break;
            case "203":
                adapter.addFragment(new Laundry_Information203(), "Laundry_Information203");
                break;
            case "204":
                adapter.addFragment(new Laundry_Information204(), "Laundry_Information204");
                break;
        }
        adapter.addFragment(new Disorder_Laundry(), "Disorder_Laundry");
        adapter.addFragment(new Disorder_App(), "Disorder_App");
        viewPager.setAdapter(adapter);
    }

}
