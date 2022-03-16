package com.example.mobile.model.Item;

import java.io.Serializable;

public class CategoryElectronics implements Serializable {
    private Category category;
    private Electronics electronics;

    public CategoryElectronics() {
    }

    public CategoryElectronics(Category category, Electronics electronics) {
        this.category = category;
        this.electronics = electronics;
    }
}
