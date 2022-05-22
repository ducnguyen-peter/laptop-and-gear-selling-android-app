package com.example.mobile.view.UserActivity.main.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.provider.SearchRecentSuggestions;
import android.widget.CompoundButton;
import android.widget.SearchView;

import com.example.mobile.R;
import com.example.mobile.controller.CartDAO.CartDAOImpl;
import com.example.mobile.controller.ItemDAO.ItemDAOImpl;
import com.example.mobile.controller.UserDAO.UserDAOImpl;
import com.example.mobile.model.cart.Cart;
import com.example.mobile.model.cart.CartItem;
import com.example.mobile.model.user.User;
import com.example.mobile.utils.PreferenceUtils;
import com.example.mobile.view.UserActivity.main.fragments.CartFragment;
import com.example.mobile.view.UserActivity.main.fragments.HomeFragment;
import com.example.mobile.view.UserActivity.main.fragments.ISendData;
import com.example.mobile.view.UserActivity.main.fragments.OrderFragment;
import com.example.mobile.view.UserActivity.verifyuser.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, ISendData {
    private ViewPager2 viewPagerMain;
    private BottomNavigationView bottomNavigation;
    private ViewPagerAdapter viewPagerAdapter;

    private HomeFragment homeFragment;
    private CartFragment cartFragment;
    private OrderFragment orderFragment;

    private UserDAOImpl userDAOImpl;
    private CartDAOImpl cartDAOImpl;
    private ItemDAOImpl itemDAOImpl;

    private GoogleSignInClient gsc;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDAOImpl = new UserDAOImpl(this);
        cartDAOImpl = new CartDAOImpl(this);
        itemDAOImpl = new ItemDAOImpl(this);
        User user = userDAOImpl.getUser(PreferenceUtils.getUsername(this));

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        if (!cartDAOImpl.isCartExisted(user)) {
            Cart cart = cartDAOImpl.createCart(user);
            PreferenceUtils.saveCartId(cart.getId(), this);
            System.out.println("New cart of user " + user.getUsername() + " has Id: " + cart.getId());
        } else {
            PreferenceUtils.saveCartId(cartDAOImpl.getCartOfUser(user).getId(), this);
        }
        init();
        Intent intent = this.getIntent();
        handleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String input = intent.getStringExtra(SearchManager.QUERY);
            //save recent query
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, RecentQuerySuggestionProvider.AUTHORITY, RecentQuerySuggestionProvider.MODE);
            suggestions.saveRecentQuery(input, null);
            //call search result activity
            Intent searchResultIntent = new Intent(this, SearchResultsActivity.class);
            searchResultIntent.putExtra("SEARCH_QUERY", input);
            startActivity(searchResultIntent);
        }
    }

    private void init() {
        setupViewPager();
    }

    private void setupViewPager() {
        //init view pager and bottom navbar
        viewPagerMain = findViewById(R.id.view_pager_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        homeFragment = new HomeFragment(this);
        cartFragment = new CartFragment(this);
        orderFragment = new OrderFragment(this);

        viewPagerAdapter = new ViewPagerAdapter(this, homeFragment, cartFragment, orderFragment);
        viewPagerMain.setAdapter(viewPagerAdapter);

        //when clicking an item (a tab) of the bottom navigation bar
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_home) {
                viewPagerMain.setCurrentItem(0);
            } else if (item.getItemId() == R.id.action_cart) {
                viewPagerMain.setCurrentItem(1);
            } else if (item.getItemId() == R.id.action_user_profile) {
                viewPagerMain.setCurrentItem(2);
            }
            return true;
        });

        //when swiping the view pager, set the corresponding bottom navbar item to selected
        viewPagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigation.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigation.getMenu().findItem(R.id.action_cart).setChecked(true);
                        break;
                    case 2:
                        bottomNavigation.getMenu().findItem(R.id.action_user_profile).setChecked(true);
                        break;
                }
            }
        });
    }

    //action bar menu, search, login
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

    //when an item on the bar is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            PreferenceUtils.savePassword(null, this);
            PreferenceUtils.saveUsername(null, this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            gsc.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            });
            gsc.revokeAccess();
//            startActivity(intent);
//            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void updateCartData() {
        viewPagerAdapter.getCartFragment().updateCartData();
    }

    @Override
    public void addSelectedCartItems(CartItem cartItem) {
        viewPagerAdapter.getCartFragment().addSelectedCartItems(cartItem);
    }

    @Override
    public void removeSelectedCartItems(CartItem cartItem) {
        viewPagerAdapter.getCartFragment().removeSelectedCartItems(cartItem);
    }

    @Override
    public void updateSelectedCartItemsAmount(CartItem cartItem, int amount) {
        viewPagerAdapter.getCartFragment().updateSelectedCartItemsAmount(cartItem, amount);
    }

    @Override
    public void deleteCartItem(CartItem cartItem) {
        viewPagerAdapter.getCartFragment().deleteCartItem(cartItem);
    }

    @Override
    public void updateOrderList() {
        viewPagerAdapter.getOrderFragment().updateOrderList();
    }
}