package org.schabi.goldstar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.schabi.goldstar.report.ErrorActivity;
import org.schabi.goldstar.extractor.NewPipe;


public class ImageErrorLoadingListener implements ImageLoadingListener {

    private int serviceId = -1;
    private Activity activity = null;
    private View rootView = null;

    public ImageErrorLoadingListener(Activity activity, View rootView, int serviceId) {
        this.activity = activity;
        this.serviceId= serviceId;
        this.rootView = rootView;
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {}

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        ErrorActivity.reportError(activity,
                failReason.getCause(), null, rootView,
                ErrorActivity.ErrorInfo.make(ErrorActivity.LOAD_IMAGE,
                        NewPipe.getNameOfService(serviceId), imageUri,
                        R.string.could_not_load_image));
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {}
}