package com.example.mobile.view.UserActivity.main.fragments;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobile.R;
import com.example.mobile.controller.ItemDAO.ItemDAOImpl;
import com.example.mobile.model.Item.Item;
import com.example.mobile.view.UserActivity.main.ItemDetailsActivity;
import com.example.mobile.view.UserActivity.main.MainActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private TextView txtWelcome;
    private GridView gridItem;
    private ItemDAOImpl itemDAOImpl;
    private ItemGridAdapter itemGridAdapter;
    private ArrayList<Item> itemList;
    private MainActivity mainActivity;
    private Item item;

    ISendData iSendData;

    ActivityResultLauncher<Intent> activityLauncher;

    public HomeFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setISendData(ISendData iSendData) {
        this.iSendData = iSendData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate and get view
        View view = inflater.inflate(R.layout.home_fragment_layout, container, false);
        txtWelcome = view.findViewById(R.id.txt_welcome);
        txtWelcome.setText(R.string.welcome_text);
        gridItem = view.findViewById(R.id.grid_item);

        activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK) iSendData.updateCartData();
                        Log.d(TAG, "onActivityResult: Failed to update cart");
                    }
                }
        );

        itemDAOImpl = new ItemDAOImpl(mainActivity);
        itemList = itemDAOImpl.getAllItems();
        itemGridAdapter = new ItemGridAdapter(mainActivity, itemList);
        gridItem.setAdapter(itemGridAdapter);
        gridItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = itemList.get(i);
                Intent intent = new Intent(mainActivity, ItemDetailsActivity.class);
                intent.putExtra("ITEM", item);
                activityLauncher.launch(intent);
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ISendData){
            iSendData = (ISendData) context;
        }
        else {
            throw new ClassCastException();
        }
    }
}
