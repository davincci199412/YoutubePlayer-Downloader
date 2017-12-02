package org.schabi.goldstar;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.acra.ACRA;
import org.acra.config.ACRAConfiguration;
import org.acra.config.ACRAConfigurationException;
import org.acra.config.ConfigurationBuilder;
import org.acra.sender.ReportSenderFactory;
import org.schabi.goldstar.extractor.NewPipe;
import org.schabi.goldstar.report.AcraReportSenderFactory;
import org.schabi.goldstar.report.ErrorActivity;
import org.schabi.goldstar.settings.SettingsActivity;

import info.guardianproject.netcipher.NetCipher;
import info.guardianproject.netcipher.proxy.OrbotHelper;



public class App extends Application {
    private static final String TAG = App.class.toString();

    private static boolean useTor;

    final Class<? extends ReportSenderFactory>[] reportSenderFactoryClasses
            = new Class[]{AcraReportSenderFactory.class};

    @Override
    public void onCreate() {
        super.onCreate();

        // init crashreport
        try {
            final ACRAConfiguration acraConfig = new ConfigurationBuilder(this)
                    .setReportSenderFactoryClasses(reportSenderFactoryClasses)
                    .build();
            ACRA.init(this, acraConfig);
        } catch(ACRAConfigurationException ace) {
            ace.printStackTrace();
            ErrorActivity.reportError(this, ace, null, null,
                    ErrorActivity.ErrorInfo.make(ErrorActivity.SEARCHED,"none",
                            "Could not initialize ACRA crash report", R.string.app_ui_crash));
        }

        //init NewPipe
        NewPipe.init(Downloader.getInstance());

        // Initialize image loader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        /*
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean(getString(R.string.use_tor_key), false)) {
            OrbotHelper.requestStartTor(this);
            configureTor(true);
        } else {
            configureTor(false);
        }*/
        configureTor(false);

        // DO NOT REMOVE THIS FUNCTION!!!
        // Otherwise downloadPathPreference has invalid value.
        SettingsActivity.initSettings(this);
    }

    /**
     * Set the proxy settings based on whether Tor should be enabled or not.
     */
    public static void configureTor(boolean enabled) {
        useTor = enabled;
        if (useTor) {
            NetCipher.useTor();
        } else {
            NetCipher.setProxy(null);
        }
    }

    public static void checkStartTor(Context context) {
        if (useTor) {
            OrbotHelper.requestStartTor(context);
        }
    }

    public static boolean isUsingTor() {
        return useTor;
    }
}
