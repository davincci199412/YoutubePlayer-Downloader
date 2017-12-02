package org.schabi.goldstar.info_list;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.mozilla.javascript.tools.debugger.Main;
import org.schabi.goldstar.ImageErrorLoadingListener;
import org.schabi.goldstar.MainActivity;
import org.schabi.goldstar.R;
import org.schabi.goldstar.extractor.AbstractStreamInfo;
import org.schabi.goldstar.extractor.InfoItem;
import org.schabi.goldstar.extractor.channel.ChannelInfoItem;
import org.schabi.goldstar.extractor.stream_info.StreamInfoItem;
import org.schabi.goldstar.fragments.search_fragment.SearchInfoItemFragment;
import org.schabi.goldstar.fragments.search_fragment.other_fragments.Favourite_Fragment;
import org.schabi.goldstar.fragments.search_fragment.other_fragments.Recent_Fragment;

/**
 * Created by Christian Schabesberger on 26.09.16.
 *
 * Copyright (C) Christian Schabesberger 2016 <chris.schabesberger@mailbox.org>
 * InfoItemBuilder.java is part of NewPipe.
 *
 * NewPipe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NewPipe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NewPipe.  If not, see <http://www.gnu.org/licenses/>.
 */

public class InfoItemBuilder {

    final String viewsS;
    final String videosS;
    final String subsS;

    final String thousand;
    final String million;
    final String billion;

    private static final String TAG = InfoItemBuilder.class.toString();
    public interface OnInfoItemSelectedListener {
        void selected(String url, int serviceId);
    }

    private Activity activity = null;
    private View rootView = null;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions displayImageOptions =
            new DisplayImageOptions.Builder().cacheInMemory(true).build();
    private OnInfoItemSelectedListener onStreamInfoItemSelectedListener;
    private OnInfoItemSelectedListener onChannelInfoItemSelectedListener;

    public InfoItemBuilder(Activity a, View rootView) {
        activity = a;
        this.rootView = rootView;
        viewsS = a.getString(R.string.views);
        videosS = a.getString(R.string.videos);
        subsS = a.getString(R.string.subscriber);
        thousand = a.getString(R.string.short_thousand);
        million = a.getString(R.string.short_million);
        billion = a.getString(R.string.short_billion);
    }

    public void setOnStreamInfoItemSelectedListener(
            OnInfoItemSelectedListener listener) {
        this.onStreamInfoItemSelectedListener = listener;
    }

    public void setOnChannelInfoItemSelectedListener(
            OnInfoItemSelectedListener listener) {
        this.onChannelInfoItemSelectedListener = listener;
    }

    public void buildByHolder(InfoItemHolder holder, final InfoItem i, int position) {
        if(i.infoType() != holder.infoType())
            return;
        switch(i.infoType()) {
            case STREAM:
                buildStreamInfoItem((StreamInfoItemHolder) holder, (StreamInfoItem) i, position);
                break;
            case CHANNEL:
                buildChannelInfoItem((ChannelInfoItemHolder) holder, (ChannelInfoItem) i);
                break;
            case PLAYLIST:
                Log.e(TAG, "Not yet implemented");
                break;
            default:
                Log.e(TAG, "Trollolo");
        }
    }

    public View buildView(ViewGroup parent, final InfoItem info) {
        View itemView = null;
        InfoItemHolder holder = null;
        switch(info.infoType()) {
            case STREAM:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.stream_item, parent, false);
                holder = new StreamInfoItemHolder(itemView);
                break;
            case CHANNEL:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.channel_item, parent, false);
                holder = new ChannelInfoItemHolder(itemView);
                break;
            case PLAYLIST:
                Log.e(TAG, "Not yet implemented");
            default:
                Log.e(TAG, "Trollolo");
        }
        buildByHolder(holder, info, 0);
        return itemView;
    }

    private void buildStreamInfoItem(StreamInfoItemHolder holder, final StreamInfoItem info, final int position) {

        if(info.infoType() != InfoItem.InfoType.STREAM) {
            Log.e("InfoItemBuilder", "Info type not yet supported");
        }
        // fill holder with information
        holder.itemVideoTitleView.setText(info.title);
        if(info.uploader != null && !info.uploader.isEmpty()) {
            holder.itemUploaderView.setText(info.uploader);
        } else {
            holder.itemUploaderView.setVisibility(View.INVISIBLE);
        }
        if(info.duration > 0) {
            holder.itemDurationView.setText(getDurationString(info.duration));
        } else {
            if(info.stream_type == AbstractStreamInfo.StreamType.LIVE_STREAM) {
                holder.itemDurationView.setText(R.string.duration_live);
            } else {
                holder.itemDurationView.setVisibility(View.GONE);
            }
        }
        if(info.view_count >= 0) {
            holder.itemViewCountView.setText(shortViewCount(info.view_count));
        } else {
            holder.itemViewCountView.setVisibility(View.GONE);
        }
        if(info.upload_date != null && !info.upload_date.isEmpty()) {
            holder.itemUploadDateView.setText(info.upload_date + " • ");
        }

        holder.itemThumbnailView.setImageResource(R.drawable.dummy_thumbnail);
        if(info.thumbnail_url != null && !info.thumbnail_url.isEmpty()) {
            imageLoader.displayImage(info.thumbnail_url,
                    holder.itemThumbnailView,
                    displayImageOptions,
                    new ImageErrorLoadingListener(activity, rootView, info.service_id));
        }

        holder.itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStreamInfoItemSelectedListener.selected(info.webpage_url, info.service_id);
                MainActivity.database_class.addRecentItem(info);
                MainActivity.count_video ++;
                if(MainActivity.count_video == 5)
                {
                    ((MainActivity)MainActivity.context).showAdmobInerstials();
                    MainActivity.count_video = 0;
                }
            }
        });

        holder.bfavouritebutton.setChecked(info.bfavourite);
        holder.bfavouritebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.context, "clicked " + position, Toast.LENGTH_LONG).show();
                info.bfavourite = !info.bfavourite;


                switch (MainActivity.nfragmentstate)
                {
                    case 0:
                        if(!info.bfavourite)
                        {

                            MainActivity.database_class.removeFavouriteItem(position);
                            Favourite_Fragment.favourite_list.remove(position);
                            Favourite_Fragment.infoListAdapter.clearSteamItemList();
                            Favourite_Fragment.infoListAdapter.addInfoItemList(Favourite_Fragment.favourite_list);


                        }
                        break;
                    case 1:
                        MainActivity.database_class.addFavouriteItem(info);
                        Recent_Fragment.recent_list.remove(position);
                        Recent_Fragment.infoListAdapter.clearSteamItemList();
                        Recent_Fragment.infoListAdapter.addInfoItemList(Recent_Fragment.recent_list);

                        break;
                    case 2:
                        if(info.bfavourite)
                        {
                            MainActivity.database_class.addFavouriteItem(info);
                            SearchInfoItemFragment.search_list.remove(position);
                            SearchInfoItemFragment.infoListAdapter.clearSteamItemList();
                            SearchInfoItemFragment.infoListAdapter.addInfoItemList(SearchInfoItemFragment.search_list);

                        }

                        break;
                }
            }
        });

    }

    private void buildChannelInfoItem(ChannelInfoItemHolder holder, final ChannelInfoItem info) {
        holder.itemChannelTitleView.setText(info.getTitle());
        holder.itemSubscriberCountView.setText(shortSubscriber(info.subscriberCount) + " • ");
        holder.itemVideoCountView.setText(info.videoAmount + " " + videosS);
        holder.itemChannelDescriptionView.setText(info.description);
        holder.favouriteButton.setChecked(info.bfavourite);
        holder.itemThumbnailView.setImageResource(R.drawable.buddy_channel_item);
        if(info.thumbnailUrl != null && !info.thumbnailUrl.isEmpty()) {
            imageLoader.displayImage(info.thumbnailUrl,
                    holder.itemThumbnailView,
                    displayImageOptions,
                    new ImageErrorLoadingListener(activity, rootView, info.serviceId));
        }

        holder.itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChannelInfoItemSelectedListener.selected(info.getLink(), info.serviceId);
            }
        });
        holder.favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CheckBox)v).setChecked(!((CheckBox)v).isChecked());
                info.bfavourite = ((CheckBox)v).isChecked();
                Toast.makeText(MainActivity.context, "clicked", Toast.LENGTH_LONG).show();
            }
        });
    }


    public String shortViewCount(Long viewCount){
        if(viewCount >= 1000000000){
            return Long.toString(viewCount/1000000000)+ billion + " " + viewsS;
        }else if(viewCount>=1000000){
            return Long.toString(viewCount/1000000)+ million + " " + viewsS;
        }else if(viewCount>=1000){
            return Long.toString(viewCount/1000)+ thousand + " " + viewsS;
        }else {
            return Long.toString(viewCount)+ " " + viewsS;
        }
    }

    public String shortSubscriber(Long count){
        if(count >= 1000000000){
            return Long.toString(count/1000000000)+ billion + " " + subsS;
        }else if(count>=1000000){
            return Long.toString(count/1000000)+ million + " " + subsS;
        }else if(count>=1000){
            return Long.toString(count/1000)+ thousand + " " + subsS;
        }else {
            return Long.toString(count)+ " " + subsS;
        }
    }

    public static String getDurationString(int duration) {
        String output = "";
        int days = duration / (24 * 60 * 60); /* greater than a day */
        duration %= (24 * 60 * 60);
        int hours = duration / (60 * 60); /* greater than an hour */
        duration %= (60 * 60);
        int minutes = duration / 60;
        int seconds = duration % 60;

        //handle days
        if(days > 0) {
            output = Integer.toString(days) + ":";
        }
        // handle hours
        if(hours > 0 || !output.isEmpty()) {
            if(hours > 0) {
                if(hours >= 10 || output.isEmpty()) {
                    output += Integer.toString(hours);
                } else {
                    output += "0" + Integer.toString(hours);
                }
            } else {
                output += "00";
            }
            output += ":";
        }
        //handle minutes
        if(minutes > 0 || !output.isEmpty()) {
            if(minutes > 0) {
                if(minutes >= 10 || output.isEmpty()) {
                    output += Integer.toString(minutes);
                } else {
                    output += "0" + Integer.toString(minutes);
                }
            } else {
                output += "00";
            }
            output += ":";
        }

        //handle seconds
        if(output.isEmpty()) {
            output += "0:";
        }

        if(seconds >= 10) {
            output += Integer.toString(seconds);
        } else {
            output += "0" + Integer.toString(seconds);
        }

        return output;
    }
}
