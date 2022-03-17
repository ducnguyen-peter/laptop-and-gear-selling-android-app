package com.example.mobile.view.UserActivity.main.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mobile.view.UserActivity.main.MainActivity;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private MainActivity mainActivity;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        mainActivity = (MainActivity) fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeFragment(mainActivity);
            case 1:
                return new CartFragment();
            case 2:
                return new UserProfileFragment();
            default:
                return new HomeFragment(mainActivity);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
