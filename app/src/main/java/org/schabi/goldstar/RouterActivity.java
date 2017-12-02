package org.schabi.goldstar;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.schabi.goldstar.detail.VideoItemDetailActivity;
import org.schabi.goldstar.detail.VideoItemDetailFragment;
import org.schabi.goldstar.extractor.NewPipe;
import org.schabi.goldstar.extractor.StreamingService;
import org.schabi.goldstar.util.NavStack;

import java.util.Collection;
import java.util.HashSet;


public class RouterActivity extends Activity {
    private static final String TAG = RouterActivity.class.toString();

    /**
     * Removes invisible separators (\p{Z}) and punctuation characters including
     * brackets (\p{P}). See http://www.regular-expressions.info/unicode.html for
     * more details.
     */
    private final static String REGEX_REMOVE_FROM_URL = "[\\p{Z}\\p{P}]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        finish();
    }


    private static String removeHeadingGibberish(final String input) {
        int start = 0;
        for (int i = input.indexOf("://") - 1; i >= 0; i--) {
            if (!input.substring(i, i + 1).matches("\\p{L}")) {
                start = i + 1;
                break;
            }
        }
        return input.substring(start, input.length());
    }

    private static String trim(final String input) {
        if (input == null || input.length() < 1) {
            return input;
        } else {
            String output = input;
            while (output.length() > 0 && output.substring(0, 1).matches(REGEX_REMOVE_FROM_URL)) {
                output = output.substring(1);
            }
            while (output.length() > 0
                    && output.substring(output.length() - 1, output.length()).matches(REGEX_REMOVE_FROM_URL)) {
                output = output.substring(0, output.length() - 1);
            }
            return output;
        }
    }

    /**
     * Retrieves all Strings which look remotely like URLs from a text.
     * Used if NewPipe was called through share menu.
     *
     * @param sharedText text to scan for URLs.
     * @return potential URLs
     */
    private String[] getUris(final String sharedText) {
        final Collection<String> result = new HashSet<>();
        if (sharedText != null) {
            final String[] array = sharedText.split("\\p{Space}");
            for (String s : array) {
                s = trim(s);
                if (s.length() != 0) {
                    if (s.matches(".+://.+")) {
                        result.add(removeHeadingGibberish(s));
                    } else if (s.matches(".+\\..+")) {
                        result.add("http://" + s);
                    }
                }
            }
        }
        return result.toArray(new String[result.size()]);
    }

    private void handleIntent(Intent intent) {
        String videoUrl = "";
        StreamingService service = null;

        // first gather data and find service
        if (intent.getData() != null) {
            // this means the video was called though another app
            videoUrl = intent.getData().toString();
        } else if(intent.getStringExtra(Intent.EXTRA_TEXT) != null) {
            //this means that vidoe was called through share menu
            String extraText = intent.getStringExtra(Intent.EXTRA_TEXT);
            videoUrl = getUris(extraText)[0];
        }

        service = NewPipe.getServiceByUrl(videoUrl);
        if(service == null) {
            Toast.makeText(this, R.string.url_not_supported_toast, Toast.LENGTH_LONG)
                    .show();
            return;
        } else {
            Intent callIntent = new Intent();
            switch(service.getLinkTypeByUrl(videoUrl)) {
                case CHANNEL:
                    callIntent.setClass(this, ChannelActivity.class);
                    break;
                case STREAM:
                    callIntent.setClass(this, VideoItemDetailActivity.class);
                    callIntent.putExtra(VideoItemDetailFragment.AUTO_PLAY,
                            PreferenceManager.getDefaultSharedPreferences(this)
                                    .getBoolean(
                                            getString(R.string.autoplay_through_intent_key), false));
                    break;
                case PLAYLIST:
                    Log.e(TAG, "NOT YET DEFINED");
                    break;
                default:
                    Toast.makeText(this, R.string.url_not_supported_toast, Toast.LENGTH_LONG)
                            .show();
                    return;
            }

            callIntent.putExtra(NavStack.URL, videoUrl);
            callIntent.putExtra(NavStack.SERVICE_ID, service.getServiceId());
            startActivity(callIntent);
        }
    }
}
