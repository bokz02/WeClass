package com.example.weclass.ratings;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.ratings.fragments.RatingsViewPagerAdapter;
import com.example.weclass.studentlist.StudentItems;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;

public class Ratings extends Fragment {

    View view;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    RatingsViewPagerAdapter ratingsViewPagerAdapter;
    String parentId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ratings, container, false);

        initialize();
        getDataFromBottomNaviActivity();
        viewPagerFragmentManager();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void initialize(){
        tabLayout = view.findViewById(R.id.tabLayoutRatings);
        viewPager2 = view.findViewById(R.id.viewPagerRatings);

    }


    // GET DATA FROM BOTTOM NAVI THE NEEDS to DISPLAY SPECIFIC DATA FROM EACH SUBJECT
    public void getDataFromBottomNaviActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            parentId = bundle.getString("IDParent");

        }
    }

    public void viewPagerFragmentManager(){
        ratingsViewPagerAdapter = new RatingsViewPagerAdapter(getParentFragmentManager(),getLifecycle(),parentId);
        viewPager2.setAdapter(ratingsViewPagerAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

}