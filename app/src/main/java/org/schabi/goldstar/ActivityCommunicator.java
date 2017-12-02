package org.schabi.goldstar;


import android.graphics.Bitmap;

/**
 * Singleton:
 * Used to send data between certain Activity/Services within the same process.
 * This can be considered as an ugly hack inside the Android universe. **/
public class ActivityCommunicator {

    private static ActivityCommunicator activityCommunicator;

    public static ActivityCommunicator getCommunicator() {
        if(activityCommunicator == null) {
            activityCommunicator = new ActivityCommunicator();
        }
        return activityCommunicator;
    }

    // Thumbnail send from VideoItemDetailFragment to BackgroundPlayer
    public volatile Bitmap backgroundPlayerThumbnail;

    public volatile Class returnActivity;
}
