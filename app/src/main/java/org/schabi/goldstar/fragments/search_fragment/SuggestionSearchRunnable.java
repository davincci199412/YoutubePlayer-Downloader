package org.schabi.goldstar.fragments.search_fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.schabi.goldstar.extractor.NewPipe;
import org.schabi.goldstar.extractor.exceptions.ExtractionException;
import org.schabi.goldstar.extractor.SuggestionExtractor;
import org.schabi.goldstar.report.ErrorActivity;
import org.schabi.goldstar.R;

import java.io.IOException;
import java.util.List;


public class SuggestionSearchRunnable implements Runnable{

    /**
     * Runnable to update a {@link SuggestionListAdapter}
     */
    private class SuggestionResultRunnable implements Runnable{

        private final List<String> suggestions;

        private SuggestionResultRunnable(List<String> suggestions) {
            this.suggestions = suggestions;
        }

        @Override
        public void run() {
            adapter.updateAdapter(suggestions);
        }
    }

    private final int serviceId;
    private final String query;
    private final Handler h = new Handler();
    private final Activity a;
    private final SuggestionListAdapter adapter;
    public SuggestionSearchRunnable(int serviceId, String query,
                                     Activity activity, SuggestionListAdapter adapter) {
        this.serviceId = serviceId;
        this.query = query;
        this.a = activity;
        this.adapter = adapter;
    }

    @Override
    public void run() {
        try {
            SuggestionExtractor se =
                    NewPipe.getService(serviceId).getSuggestionExtractorInstance();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(a);
            String searchLanguageKey = a.getString(R.string.search_language_key);
            String searchLanguage = sp.getString(searchLanguageKey,
                    a.getString(R.string.default_language_value));
            List<String> suggestions = se.suggestionList(query, searchLanguage);
            h.post(new SuggestionResultRunnable(suggestions));
        } catch (ExtractionException e) {
            ErrorActivity.reportError(h, a, e, null, a.findViewById(android.R.id.content),
                    ErrorActivity.ErrorInfo.make(ErrorActivity.SEARCHED,
                            NewPipe.getNameOfService(serviceId), query, R.string.parsing_error));
            e.printStackTrace();
        } catch (IOException e) {
            postNewErrorToast(h, R.string.network_error);
            e.printStackTrace();
        } catch (Exception e) {
            ErrorActivity.reportError(h, a, e, null, a.findViewById(android.R.id.content),
                    ErrorActivity.ErrorInfo.make(ErrorActivity.SEARCHED,
                            NewPipe.getNameOfService(serviceId), query, R.string.general_error));
        }
    }

    private void postNewErrorToast(Handler h, final int stringResource) {
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(a, a.getString(stringResource),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}