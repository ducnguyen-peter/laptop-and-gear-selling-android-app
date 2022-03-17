package com.example.mobile.view.UserActivity.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobile.R;
import com.example.mobile.controller.ItemDAO.ItemDAOImpl;
import com.example.mobile.model.Item.Item;
import com.example.mobile.view.UserActivity.main.ItemDetailsActivity;
import com.example.mobile.view.UserActivity.main.ItemGridAdapter;
import com.example.mobile.view.UserActivity.main.MainActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private TextView txtWelcome;
    private GridView gridItem;
    private ItemDAOImpl itemDAOImpl;
    private ItemGridAdapter itemGridAdapter;
    private ArrayList<Item> itemList;
    private MainActivity mainActivity;

    public HomeFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout, container, false);
        txtWelcome = view.findViewById(R.id.txt_welcome);
        txtWelcome.setText(R.string.welcome_text);
        gridItem = view.findViewById(R.id.grid_item);

        itemDAOImpl = new ItemDAOImpl(mainActivity);
        itemList = itemDAOImpl.getAllItems();
        itemGridAdapter = new ItemGridAdapter(mainActivity, itemList);
        gridItem.setAdapter(itemGridAdapter);
        gridItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = itemList.get(i);
                Intent intent = new Intent(mainActivity, ItemDetailsActivity.class);
                intent.putExtra("ITEM", item);
                startActivity(intent);
            }
        });
        return view;
    }
}
