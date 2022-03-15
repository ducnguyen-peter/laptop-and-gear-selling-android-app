package com.example.mobile.view.UserActivity.main;

import android.content.SearchRecentSuggestionsProvider;

public class RecentQuerySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.RecentQuerySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public RecentQuerySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
