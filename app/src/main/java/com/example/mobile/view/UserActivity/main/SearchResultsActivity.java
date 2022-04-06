package com.example.mobile.view.UserActivity.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.mobile.R;
import com.example.mobile.controller.ItemDAO.ItemDAOImpl;
import com.example.mobile.model.Item.Item;
import com.example.mobile.utils.PreferenceUtils;
import com.example.mobile.view.UserActivity.main.fragments.ItemGridAdapter;
import com.example.mobile.view.UserActivity.verifyuser.LoginActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private TextView txtQuery;
    private ArrayList<Item> itemList;
    private GridView gridItemSearchResults;
    private ItemDAOImpl itemDAOImpl;
    private ItemGridAdapter itemGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        init();
        Intent intent = getIntent();
        handleIntent(intent);
    }

    private void init(){
        //map view
        txtQuery = findViewById(R.id.txt_query);
        gridItemSearchResults = findViewById(R.id.grid_item_search_result);
        //init data for gridview
        itemDAOImpl = new ItemDAOImpl(this);
        itemList = new ArrayList<>();
        itemGridAdapter = new ItemGridAdapter(this, itemList);
        gridItemSearchResults.setAdapter(itemGridAdapter);
        gridItemSearchResults.setOnItemClickListener(this);

        //search for items for input from MainActivity
        if(getIntent().hasExtra("SEARCH_QUERY")){
            String input = getIntent().getStringExtra("SEARCH_QUERY");
            //update the item list
            itemList.clear();
            itemList.addAll(itemDAOImpl.searchItem(input));
            itemGridAdapter.notifyDataSetChanged();
            //show the query
            txtQuery.setText("Search results for keyword: " + input);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String input = intent.getStringExtra(SearchManager.QUERY);
            //save recent query
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, RecentQuerySuggestionProvider.AUTHORITY, RecentQuerySuggestionProvider.MODE);
            suggestions.saveRecentQuery(input, null);
            //update the item list
            itemList.clear();
            itemList.addAll(itemDAOImpl.searchItem(input));
            itemGridAdapter.notifyDataSetChanged();
            //show the query
            txtQuery.setText("Search results for keyword: " + input);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        //Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

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
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra("ITEM", item);
        startActivity(intent);
    }
}