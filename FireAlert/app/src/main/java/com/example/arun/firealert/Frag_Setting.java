package com.example.arun.firealert;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Frag_Setting extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_layout2,container,false);
        tabLayout=view.findViewById(R.id.tablayout);
        viewPager=view.findViewById(R.id.viewpager);
        Fragment_Adapter fragment_adapter=new Fragment_Adapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(fragment_adapter);
        tabLayout.setupWithViewPager(viewPager);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.viewpager,new Frag_Dashboard()).commit();
        return view;
    }
}
