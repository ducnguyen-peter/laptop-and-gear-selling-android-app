package com.example.mobile.view.UserActivity.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile.R;
import com.example.mobile.controller.ItemDAO.ItemDAOImpl;
import com.example.mobile.model.Item.Category;
import com.example.mobile.model.Item.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

public class ItemDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtItemTitle;
    private ImageView imgItem;

    private TextView txtPrice;
    private TextView txtDescription;
    private TextView txtCategory;
    private TextView txtAvailable;

    private Button btnAddToCart;
    private Button btnBuyNow;
    private ItemDAOImpl itemDAOImpl;

    private Item item;
    private int amount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Intent intent = getIntent();
        handleIntent(intent);
        init();
    }
    private void init(){
        //map the views
        txtItemTitle = findViewById(R.id.txt_item_name);
        imgItem = findViewById(R.id.img_item_detail);

        txtPrice = findViewById(R.id.txt_price);
        txtDescription = findViewById(R.id.txt_description);
        txtCategory = findViewById(R.id.txt_category);
        txtAvailable = findViewById(R.id.txt_available);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        btnBuyNow = findViewById(R.id.btn_buy);

        //init DAO class
        itemDAOImpl = new ItemDAOImpl(this);

        //get categories of current item and set categories to clickable
        ArrayList<Category> categories = itemDAOImpl.getItemCategory(item);
        String categoriesName = "";
        for(Category c : categories){
            categoriesName += (c.getName() + ", ");
        }
        SpannableString ss = new SpannableString(categoriesName);
        for(Category c : categories){
            String cName = c.getName();
            int index = categoriesName.indexOf(cName);
            ClickableSpan categoryClick = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    Toast.makeText(ItemDetailsActivity.this, cName, Toast.LENGTH_SHORT).show();
                }
            };
            ss.setSpan(categoryClick, index, cName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        txtCategory.setLinksClickable(true);
        txtCategory.setMovementMethod(LinkMovementMethod.getInstance());
        txtCategory.setText(ss, TextView.BufferType.SPANNABLE);

        //the rest of the item info
        txtItemTitle.setText(item.getElectronics().getName());
        txtPrice.setText(String.format(Locale.ENGLISH, "%.1fđ", item.getUnitPrice()));
        txtDescription.setText(item.getElectronics().getDescription());
        imgItem.setImageResource(getResources().getIdentifier(item.getElectronics().getImageLink().trim(), "drawable", getPackageName()));
        txtAvailable.setText(String.format(Locale.ENGLISH,"%d", item.getQuantity()));
        //set onclick for buttons
        btnAddToCart.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);
    }

    private void handleIntent(Intent intent){
        if(intent.hasExtra("ITEM")){
            item = (Item) intent.getSerializableExtra("ITEM");
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

    }
}