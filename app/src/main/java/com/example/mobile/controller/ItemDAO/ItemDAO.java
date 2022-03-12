package com.example.mobile.controller.ItemDAO;

import com.example.mobile.model.Item.Item;

import java.util.ArrayList;

public interface ItemDAO {
    ArrayList<Item> searchItem(String input);
    ArrayList<Item> getAllItems();
}
