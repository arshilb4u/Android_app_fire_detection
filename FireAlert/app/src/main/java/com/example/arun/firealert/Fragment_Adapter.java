package com.example.arun.firealert;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Fragment_Adapter extends FragmentPagerAdapter {
    public Fragment_Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment=null;
        switch (i)
        {
            case 0:
            {
                fragment=new Frag_Dashboard();
                break;
            }
            case 1:
            {
                fragment=new Frag_AboutUs();
                break;
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
            {
                return "DashBoard";
            }
            case 1:
            {
                return "About Us";
            }
        }
        return null;
    }
}
