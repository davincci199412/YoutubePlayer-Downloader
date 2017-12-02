package org.schabi.goldstar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.HashMap;

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

public class DBHelper extends SQLiteOpenHelper {

    public  static String DATABASE_NAME = "";
    public  String TABLE_NAME = "";

    private HashMap hp;

    public DBHelper(Context context, String name, String database_name) {
        super(context, DATABASE_NAME , null, 2);
        TABLE_NAME = name;
        DATABASE_NAME = database_name;


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE " + TABLE_NAME + DATABASE_ENTRY.DATABASE_TABLE_CREATE
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL(DATABASE_ENTRY.DROP_QUERY + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertMusicStreamItem(int type, int server_id,String id, String title, String uploader,String thumbnail_url,
                                         String webpage_url,  String upload_date ,long view_count ,boolean bfavourite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(STREAM_TYPE, type);
        contentValues.put(SERVER_ID , server_id);
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(UPLOADER, uploader);
        contentValues.put(THUMBNAILS, thumbnail_url);
        contentValues.put(WEB_URL, webpage_url);
        contentValues.put(UPLOADER_DATE, upload_date);
        contentValues.put(VIEW_COUNT, (int)view_count);
        contentValues.put(BFAVOURITE, bfavourite);

        return db.insert(TABLE_NAME, null, contentValues) > 0;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { "*" },
                null,
                null, null, null, null, null);
        cursor.moveToPosition(id);
        return cursor;
    }

    public int numberOfRows(){
        int numRows = -1;
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        }
        catch (SQLiteException sqLiteException)
        {
            return numRows;
        }


        return numRows;
    }

    public boolean updateContact (Integer number, int type, int server_id,String id, String title, String uploader,String thumbnail_url,
                                  String webpage_url,  String upload_date ,long view_count ,boolean bfavourite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STREAM_TYPE, type);
        contentValues.put(SERVER_ID , server_id);
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(UPLOADER, uploader);
        contentValues.put(THUMBNAILS, thumbnail_url);
        contentValues.put(WEB_URL, webpage_url);
        contentValues.put(UPLOADER_DATE, upload_date);
        contentValues.put(VIEW_COUNT, view_count);
        contentValues.put(BFAVOURITE, bfavourite);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(number) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public static abstract class DATABASE_ENTRY implements BaseColumns {
        public static String ROW_ENTRY_ID = "id";
        public static final String STREAM_TYPE = "STREAM_TYPE";
        public static final String SERVER_ID = "SERVER_ID";
        public static final String COLUMN_ID = "COLUMN_ID";
        public static final String COLUMN_TITLE = "COLUMN_TITLE";
        public static final String UPLOADER = "UPLOADER";
        public static final String THUMBNAILS = "THUMBNAILS";
        public static final String WEB_URL = "WEB_URL";
        public static final String UPLOADER_DATE = "UPLOADER_DATE";
        public static final String VIEW_COUNT = "VIEW_COUNT";
        public static final String BFAVOURITE = "BFAVOURITE";



        private static final String DATABASE_TABLE_CREATE =
                "(" +
                        ROW_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        STREAM_TYPE + " INTEGER, " +
                        SERVER_ID + " TEXT, " +
                        COLUMN_ID + " TEXT, " +
                        COLUMN_TITLE + " TEXT, " +
                        UPLOADER + " TEXT, " +
                        THUMBNAILS + " TEXT, " +
                        WEB_URL + " TEXT, " +
                        UPLOADER_DATE + " TEXT, " +
                        VIEW_COUNT + " INTEGER, " +
                        BFAVOURITE + " INTEGER)";


        public static final String DROP_QUERY = "DROP TABLE IF EXISTS";

    }
}
