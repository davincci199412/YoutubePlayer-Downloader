package org.schabi.goldstar.database;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import org.schabi.goldstar.MainActivity;
import org.schabi.goldstar.extractor.AbstractStreamInfo;
import org.schabi.goldstar.extractor.InfoItem;
import org.schabi.goldstar.extractor.stream_info.StreamInfoItem;

import java.util.List;
import java.util.Vector;


import static org.schabi.goldstar.database.DBHelper.DATABASE_ENTRY.BFAVOURITE;
import static org.schabi.goldstar.database.DBHelper.DATABASE_ENTRY.COLUMN_ID;
import static org.schabi.goldstar.database.DBHelper.DATABASE_ENTRY.COLUMN_TITLE;
import static org.schabi.goldstar.database.DBHelper.DATABASE_ENTRY.SERVER_ID;
import static org.schabi.goldstar.database.DBHelper.DATABASE_ENTRY.STREAM_TYPE;
import static org.schabi.goldstar.database.DBHelper.DATABASE_ENTRY.THUMBNAILS;
import static org.schabi.goldstar.database.DBHelper.DATABASE_ENTRY.UPLOADER;
import static org.schabi.goldstar.database.DBHelper.DATABASE_ENTRY.UPLOADER_DATE;
import static org.schabi.goldstar.database.DBHelper.DATABASE_ENTRY.VIEW_COUNT;
import static org.schabi.goldstar.database.DBHelper.DATABASE_ENTRY.WEB_URL;
import static org.schabi.goldstar.database.Database_Class.StreamType.AUDIO_LIVE_STREAM;
import static org.schabi.goldstar.database.Database_Class.StreamType.AUDIO_STREAM;
import static org.schabi.goldstar.database.Database_Class.StreamType.FILE;
import static org.schabi.goldstar.database.Database_Class.StreamType.LIVE_STREAM;
import static org.schabi.goldstar.database.Database_Class.StreamType.NONE;
import static org.schabi.goldstar.database.Database_Class.StreamType.VIDEO_STREAM;


public class Database_Class {
    public void removeFavouriteItem(int position) {
        favourite_database.deleteContact(position);
    }
    public void removeRecentItem(int position) {
        recent_database.deleteContact(position);
    }
    public enum StreamType {
        NONE,   // placeholder to check if stream type was checked or not
        VIDEO_STREAM,
        AUDIO_STREAM,
        LIVE_STREAM,
        AUDIO_LIVE_STREAM,
        FILE
    }
    public static String FAVOURITE_TABLE_NAME = "FAVOURITE_TABLE";
    public static String RECENT_TABLE_NAME = "RECENT_TABLE_NAME";
    private DBHelper favourite_database;
    private DBHelper recent_database;
    public Database_Class(Context context)
    {
        DBHelper.DATABASE_NAME = "MyFavouriteName.db";
        favourite_database = new DBHelper(context, FAVOURITE_TABLE_NAME, "MyFavouriteName.db");
        DBHelper.DATABASE_NAME = "MyRecentName.db";
        recent_database = new DBHelper(context, RECENT_TABLE_NAME, "MyRecentName.db");

    }
    public void addFavouriteItem(StreamInfoItem infoItem)
    {

        int type = 0;
        switch (infoItem.stream_type)
        {
            case NONE:
                type = 0;
                break;
            case VIDEO_STREAM:
                type = 1;
                break;
            case AUDIO_STREAM:
                type = 2;
                break;
            case LIVE_STREAM:
                type = 3;
                break;
            case AUDIO_LIVE_STREAM:
                type = 4;
                break;
            case FILE:
                break;

        }
        if(!favourite_database.insertMusicStreamItem(type, infoItem.service_id, infoItem.id, infoItem.title, infoItem.uploader,
                infoItem.thumbnail_url, infoItem.webpage_url, infoItem.upload_date, infoItem.view_count, infoItem.bfavourite))
        {
            Toast.makeText(MainActivity.context, "Insert Failed", Toast.LENGTH_LONG).show();
        }

    }
    private InfoItem getFavouriteItem(int id)
    {
        StreamInfoItem streamInfo = new StreamInfoItem();
        Cursor cursor = favourite_database.getData(id);
        if(cursor.getCount() < 0)
            return streamInfo;
        streamInfo.service_id = cursor.getInt(cursor.getColumnIndexOrThrow(SERVER_ID));
        int type = cursor.getInt(cursor.getColumnIndexOrThrow(STREAM_TYPE));
        switch (type)
        {
            case 0:
                streamInfo.stream_type = AbstractStreamInfo.StreamType.NONE;
                break;
            case 1:
                streamInfo.stream_type = AbstractStreamInfo.StreamType.VIDEO_STREAM;
                break;
            case 2:
                streamInfo.stream_type = AbstractStreamInfo.StreamType.AUDIO_STREAM;
                break;
            case 3:
                streamInfo.stream_type = AbstractStreamInfo.StreamType.LIVE_STREAM;
                break;
            case 4:
                streamInfo.stream_type = AbstractStreamInfo.StreamType.AUDIO_LIVE_STREAM;
                break;
            case 5:
                streamInfo.stream_type = AbstractStreamInfo.StreamType.FILE;
                break;

        }

        streamInfo.id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
        streamInfo.title= cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
        streamInfo.uploader = cursor.getString(cursor.getColumnIndexOrThrow(UPLOADER));
        streamInfo.thumbnail_url = cursor.getString(cursor.getColumnIndexOrThrow(THUMBNAILS));
        streamInfo.webpage_url = cursor.getString(cursor.getColumnIndexOrThrow(WEB_URL));
        streamInfo.upload_date = cursor.getString(cursor.getColumnIndexOrThrow(UPLOADER_DATE));
        streamInfo.view_count = cursor.getInt(cursor.getColumnIndexOrThrow(VIEW_COUNT));
        streamInfo.bfavourite = cursor.getInt(cursor.getColumnIndexOrThrow(BFAVOURITE)) > 0;
        return (InfoItem) streamInfo;
    }
    public List<InfoItem> getFavouriteList()
    {
        List<InfoItem> infolist = new Vector<>();
        if(favourite_database.numberOfRows() < 0)
            return infolist;
        int n = favourite_database.numberOfRows();
        for(int i = 0 ; i < n ; i ++)
        {
            infolist.add(getFavouriteItem(i));
        }
        return infolist;
    }

    public void addRecentItem(StreamInfoItem infoItem)
    {
        int type = 0;
        switch (infoItem.stream_type)
        {
            case NONE:
                type = 0;
                break;
            case VIDEO_STREAM:
                type = 1;
                break;
            case AUDIO_STREAM:
                type = 2;
                break;
            case LIVE_STREAM:
                type = 3;
                break;
            case AUDIO_LIVE_STREAM:
                type = 4;
                break;
            case FILE:
                break;

        }
        if(!recent_database.insertMusicStreamItem(type, infoItem.service_id, infoItem.id, infoItem.title, infoItem.uploader,
                infoItem.thumbnail_url, infoItem.webpage_url, infoItem.upload_date, infoItem.view_count, infoItem.bfavourite))
        {
            Toast.makeText(MainActivity.context, "Insert Failed", Toast.LENGTH_LONG).show();
        }

    }
    private InfoItem getRecentItem(int id)
    {
        StreamInfoItem streamInfo = new StreamInfoItem();
        Cursor cursor = recent_database.getData(id);
        int type = cursor.getInt(cursor.getColumnIndexOrThrow(STREAM_TYPE));
        switch (type)
        {
            case 0:
                streamInfo.stream_type = AbstractStreamInfo.StreamType.NONE;
                break;
            case 1:
                streamInfo.stream_type = AbstractStreamInfo.StreamType.VIDEO_STREAM;
                break;
            case 2:
                streamInfo.stream_type = AbstractStreamInfo.StreamType.AUDIO_STREAM;
                break;
            case 3:
                streamInfo.stream_type = AbstractStreamInfo.StreamType.LIVE_STREAM;
                break;
            case 4:
                streamInfo.stream_type = AbstractStreamInfo.StreamType.AUDIO_LIVE_STREAM;
                break;
            case 5:
                streamInfo.stream_type = AbstractStreamInfo.StreamType.FILE;
                break;

        }
        streamInfo.service_id = cursor.getInt(cursor.getColumnIndexOrThrow(SERVER_ID));
        streamInfo.id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
        streamInfo.title= cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
        streamInfo.uploader = cursor.getString(cursor.getColumnIndexOrThrow(UPLOADER));
        streamInfo.thumbnail_url = cursor.getString(cursor.getColumnIndexOrThrow(THUMBNAILS));
        streamInfo.webpage_url = cursor.getString(cursor.getColumnIndexOrThrow(WEB_URL));
        streamInfo.upload_date = cursor.getString(cursor.getColumnIndexOrThrow(UPLOADER_DATE));
        streamInfo.view_count = cursor.getInt(cursor.getColumnIndexOrThrow(VIEW_COUNT));
        streamInfo.bfavourite = cursor.getInt(cursor.getColumnIndexOrThrow(BFAVOURITE)) > 0;
        return streamInfo;
    }
    public List<InfoItem> getRecentList()
    {
        List<InfoItem> infolist = new Vector<>();
        if(recent_database.numberOfRows() < 0)
            return infolist;
        for(int i = 0 ; i < recent_database.numberOfRows() ; i ++)
        {
            infolist.add(getRecentItem(i));
        }
        return infolist;
    }
}
