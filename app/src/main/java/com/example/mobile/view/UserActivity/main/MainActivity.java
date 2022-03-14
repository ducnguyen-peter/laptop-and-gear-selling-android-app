package com.example.mobile.view.UserActivity.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mobile.R;
import com.example.mobile.controller.ItemDAO.ItemDAOImpl;
import com.example.mobile.model.Item.Item;
import com.example.mobile.utils.PreferenceUtils;
import com.example.mobile.view.UserActivity.verifyuser.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private TextView txtWelcome;
    private ArrayList<Item> itemList;
    private GridView gridItem;
    private ItemDAOImpl itemDAOImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Intent intent = this.getIntent();
        if(intent.hasExtra("NAME")){
            String username = intent.getExtras().getString("NAME");
            txtWelcome.setText("Welcome " + username);
        } else{
            String username = PreferenceUtils.getUsername(this);
            txtWelcome.setText("Welcome " + username);
        }
    }

    private void init(){
        txtWelcome = findViewById(R.id.txt_welcome);
        gridItem = findViewById(R.id.grid_item);

        itemDAOImpl = new ItemDAOImpl(this);
        itemList = itemDAOImpl.getAllItems();
        ItemGridAdapter adapter = new ItemGridAdapter(this, itemList);
        gridItem.setAdapter(adapter);
        gridItem.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout){
            PreferenceUtils.savePassword(null, this);
            PreferenceUtils.saveUsername(null, this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Item item = itemList.get(i);

    }
}