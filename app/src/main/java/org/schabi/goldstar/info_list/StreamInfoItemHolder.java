package org.schabi.goldstar.info_list;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.schabi.goldstar.R;
import org.schabi.goldstar.extractor.InfoItem;


public class StreamInfoItemHolder extends InfoItemHolder {

    public final ImageView itemThumbnailView;
    public final TextView itemVideoTitleView,
            itemUploaderView,
            itemDurationView,
            itemUploadDateView,
            itemViewCountView;
    public final Button itemButton;
    public final CheckBox bfavouritebutton;

    public StreamInfoItemHolder(View v) {
        super(v);
        itemThumbnailView = (ImageView) v.findViewById(R.id.itemThumbnailView);
        itemVideoTitleView = (TextView) v.findViewById(R.id.itemVideoTitleView);
        itemUploaderView = (TextView) v.findViewById(R.id.itemUploaderView);
        itemDurationView = (TextView) v.findViewById(R.id.itemDurationView);
        itemUploadDateView = (TextView) v.findViewById(R.id.itemUploadDateView);
        itemViewCountView = (TextView) v.findViewById(R.id.itemViewCountView);
        itemButton = (Button) v.findViewById(R.id.item_button);
        bfavouritebutton = (CheckBox)v.findViewById(R.id.favoriteButton);
    }

    @Override
    public InfoItem.InfoType infoType() {
        return InfoItem.InfoType.STREAM;
    }
}
