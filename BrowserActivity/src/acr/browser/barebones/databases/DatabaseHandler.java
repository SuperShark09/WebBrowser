package acr.browser.barebones.databases;

import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "historyManager";
 
    // HistoryItems table name
    private static final String TABLE_HISTORY = "history";
 
    // HistoryItems Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_URL = "url";
    private static final String KEY_TITLE = "title";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_URL + " TEXT,"
                + KEY_TITLE + " TEXT" +")";
        db.execSQL(CREATE_HISTORY_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    
    public void delete(String url){
    	String n = getHistoryItem(url);
    	deleteHistoryItem(n);
    }
 
    // Adding new item
    public void addHistoryItem(HistoryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_URL, item.getUrl()); // HistoryItem Name
        values.put(KEY_TITLE, item.getTitle()); // HistoryItem Phone
        // Inserting Row
        db.insert(TABLE_HISTORY, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single item
    String getHistoryItem(String url) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_HISTORY, new String[] { KEY_ID,
                KEY_URL, KEY_TITLE}, KEY_URL + "=?",
                new String[] { url }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        String m = cursor.getString(0);
        cursor.close();
        db.close();
        // return item
        return  m;
    }
 
    // Getting All HistoryItems
    public List<HistoryItem> getAllHistoryItems() {
        List<HistoryItem> itemList = new ArrayList<HistoryItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_HISTORY;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HistoryItem item = new HistoryItem();
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.setUrl(cursor.getString(1));
                item.setTitle(cursor.getString(2));
                // Adding item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return item list
        return itemList;
    }
 
    // Updating single item
    public int updateHistoryItem(HistoryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_URL, item.getUrl());
        values.put(KEY_TITLE, item.getTitle());
        int n = db.update(TABLE_HISTORY, values, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getID()) });
        // updating row
        db.close();
        return n;
    }
 
    // Deleting single item
    public void deleteHistoryItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
 
    // Getting items Count
    public int getHistoryItemsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_HISTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
 
}
