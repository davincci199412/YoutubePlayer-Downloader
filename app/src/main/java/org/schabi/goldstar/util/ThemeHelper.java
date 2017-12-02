package org.schabi.goldstar.util;

import android.content.Context;
import android.preference.PreferenceManager;

import org.schabi.goldstar.R;

public class ThemeHelper {

    public static void setTheme(Context context, boolean mode) {
        // mode is true for normal theme, false for no action bar theme.

        String themeKey = context.getString(R.string.theme_key);
        //String lightTheme = context.getResources().getString(R.string.light_theme_title);
        String darkTheme = context.getResources().getString(R.string.dark_theme_title);
        String blackTheme = context.getResources().getString(R.string.black_theme_title);

        String sp = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(themeKey, context.getResources().getString(R.string.light_theme_title));

        if (mode) {
            if (sp.equals(darkTheme)) context.setTheme(R.style.DarkTheme);
            else if (sp.equals(blackTheme)) context.setTheme(R.style.BlackTheme);
            else context.setTheme(R.style.AppTheme);
        } else {
            if (sp.equals(darkTheme)) context.setTheme(R.style.DarkTheme_NoActionBar);
            else if (sp.equals(blackTheme)) context.setTheme(R.style.BlackTheme_NoActionBar);
            else context.setTheme(R.style.AppTheme_NoActionBar);
        }
    }
}
