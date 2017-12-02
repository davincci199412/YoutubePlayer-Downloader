package org.schabi.goldstar.fragments.search_fragment;

import android.support.v7.widget.SearchView;




public class SearchSuggestionListener implements SearchView.OnSuggestionListener{

    private final SearchView searchView;
    private final SuggestionListAdapter adapter;

    public SearchSuggestionListener(SearchView searchView, SuggestionListAdapter adapter) {
        this.searchView = searchView;
        this.adapter = adapter;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        String suggestion = adapter.getSuggestion(position);
        searchView.setQuery(suggestion,true);
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        String suggestion = adapter.getSuggestion(position);
        searchView.setQuery(suggestion,true);
        return false;
    }
}