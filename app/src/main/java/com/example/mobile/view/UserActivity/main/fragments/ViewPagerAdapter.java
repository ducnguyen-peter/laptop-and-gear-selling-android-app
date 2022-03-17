package com.example.mobile.view.UserActivity.main.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mobile.model.cart.Cart;
import com.example.mobile.view.UserActivity.main.MainActivity;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private MainActivity mainActivity;
    private HomeFragment homeFragment;
    private CartFragment cartFragment;
    private UserProfileFragment userProfileFragment;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        mainActivity = (MainActivity) fragmentActivity;
        homeFragment = new HomeFragment(mainActivity);
        cartFragment = new CartFragment();
        userProfileFragment = new UserProfileFragment();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return homeFragment;
            case 1:
                return cartFragment;
            case 2:
                return userProfileFragment;
            default:
                return homeFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
