package db_util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DB_utils extends SQLiteOpenHelper 
{
	private static final String TEXT_TYPE = " TEXT";
	public static final String TABLE_NAME = "entry";
	public static final String KEY_TITLE = "key_title";
	public static final String VAL_TITLE = "val_title";
	public static final String KEY_ID_NAME = "'Shaktimaan'";
	
	private static final String SQL_CREATE_ENTRIES =
	    "CREATE TABLE " + TABLE_NAME + " (" + KEY_TITLE + TEXT_TYPE + "," + VAL_TITLE + TEXT_TYPE +   " )";
 
	final static int DB_VERSION = 1;
    //final static String DB_NAME = "/mnt/sdcard//mydb.db"; // Environment.getExternalStorageDirectory().getPath()
	//final static String DB_PATH = "";
	final static String DB_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
	final static String DB_NAME = "my5.db";	
	
	Context context;
	     
	public DB_utils (Context context) 
	{
	    super(context, DB_PATH + DB_NAME, null, DB_VERSION);	  
	    this.context = context;	  // Store the context for later use                   
	}
	
	@Override	
	public void onCreate(SQLiteDatabase db) 
	{
	
	Log.w (this.getClass().getSimpleName(),"Trying to create new DB at onCreate ()!");  // MySQLiteHelper.class.getName(),		
	db.execSQL(SQL_CREATE_ENTRIES);	
	}
		
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {     
	if (newVersion > oldVersion) {
	    switch (oldVersion) {
	        case 1:
	        	break;
	        case 2:
	        	break;
	        }
	    }
	}	
	
	@Override
	public void onOpen (SQLiteDatabase db) 
	{
		Log.w (this.getClass().getSimpleName(), "was opened: " + DB_PATH + DB_NAME);
    }  
}
