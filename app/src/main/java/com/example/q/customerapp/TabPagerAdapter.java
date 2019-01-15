package com.example.q.customerapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    //Count number of tabs
    private int tabCount;
    public Fragment mainTabFragment1;
    public Fragment mainTabFragment2;
    public Fragment mainTabFragment3;
    public Fragment mainTabFragment4;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        //Returning the current tabs
        switch (position){
            case 0:
                mainTabFragment1 = new MainTabFragment1();
                return mainTabFragment1;
            case 1:
                mainTabFragment2 = new MainTabFragment2();
                return mainTabFragment2;
            case 2:
                mainTabFragment3 = new MainTabFragment3();
                return mainTabFragment3;
            case 3:
                mainTabFragment4 = new MainTabFragment4();
                return mainTabFragment4;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
