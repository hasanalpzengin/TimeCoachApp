package com.hasanalpzengin.lifeorganizator;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by hasan alp zengin on 28-Jan-18.
 */

public class ConfigurationAdapter extends FragmentPagerAdapter{
    ArrayList<Fragment> pages = new ArrayList<>();

    public ConfigurationAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    public void addFragment(Fragment fragment){
        pages.add(fragment);
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
