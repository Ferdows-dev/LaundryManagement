package com.learning.laundrymanagement.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.learning.laundrymanagement.Fragments.Bookingstep1Fragment;
import com.learning.laundrymanagement.Fragments.Bookingstep2Fragment;
import com.learning.laundrymanagement.Fragments.Bookingstep3Fragment;
import com.learning.laundrymanagement.Fragments.Bookingstep4Fragment;

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    public MyViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return Bookingstep1Fragment.getInstance();
            case 1:
                return Bookingstep2Fragment.getInstance();
            case 2:
                return Bookingstep3Fragment.getInstance();
            case 3:
                return Bookingstep4Fragment.getInstance();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
