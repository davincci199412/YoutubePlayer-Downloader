package org.schabi.goldstar.info_list;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.schabi.goldstar.R;
import org.schabi.goldstar.extractor.InfoItem;

import de.hdodenhof.circleimageview.CircleImageView;



public class ChannelInfoItemHolder extends InfoItemHolder {
    public final CircleImageView itemThumbnailView;
    public final TextView itemChannelTitleView;
    public final TextView itemSubscriberCountView;
    public final TextView itemVideoCountView;
    public final TextView itemChannelDescriptionView;
    public final Button itemButton;
    public final CheckBox favouriteButton;

    ChannelInfoItemHolder(View v) {
        super(v);
        itemThumbnailView = (CircleImageView) v.findViewById(R.id.itemThumbnailView);
        itemChannelTitleView = (TextView) v.findViewById(R.id.itemChannelTitleView);
        itemSubscriberCountView = (TextView) v.findViewById(R.id.itemSubscriberCountView);
        itemVideoCountView = (TextView) v.findViewById(R.id.itemVideoCountView);
        itemChannelDescriptionView = (TextView) v.findViewById(R.id.itemChannelDescriptionView);
        itemButton = (Button) v.findViewById(R.id.item_button);
        favouriteButton = (CheckBox)v.findViewById(R.id.favoriteButton);
    }

    @Override
    public InfoItem.InfoType infoType() {
        return InfoItem.InfoType.CHANNEL;
    }
}
