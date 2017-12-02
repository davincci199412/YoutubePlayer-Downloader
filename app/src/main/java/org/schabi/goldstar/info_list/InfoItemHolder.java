package org.schabi.goldstar.info_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.schabi.goldstar.extractor.InfoItem;



public abstract class InfoItemHolder extends RecyclerView.ViewHolder {
    public InfoItemHolder(View v) {
        super(v);
    }
    public abstract InfoItem.InfoType infoType();
}
