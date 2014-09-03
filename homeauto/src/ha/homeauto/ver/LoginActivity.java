// ref to http://www.androidhive.info/2011/10/android-login-and-registration-screen-design/

package ha.homeauto.ver;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import db_util.DB_utils;
import ha.homeauto.ver.HomeautoActivity;
import ha.homeauto.ver.R;
import ha_util.HA_utils;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
 
public class LoginActivity extends Activity implements OnClickListener
{
	protected Button bExit;
	protected Button bRegister;
	protected Button bLogin ;
	
	protected EditText etPassword;
	protected EditText etLogin;
	
	protected TextView lLogin ;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
      
	    setContentView(R.layout.register);
	         
	    bExit = (Button) findViewById(R.id.btnExit);
	    bRegister = (Button) findViewById(R.id.btnRegister);
	    bLogin = (Button) findViewById(R.id.btnLogin);
	     
	    lLogin  = (TextView) findViewById(R.id.lableLogin);
	     
	    etPassword = (EditText) findViewById(R.id.textPassword);
	    etLogin    = (EditText) findViewById(R.id.textLogin);
	     
	    bExit.setOnClickListener (this);
	    bLogin.setOnClickListener( this);
	    bRegister.setOnClickListener( this);
   }

	public void onClick(View v) 
	{		
		if(v == bRegister) 
		{
		    DB_utils db = new DB_utils (this);
		      
	        SQLiteDatabase dbRW = db.getWritableDatabase();
	              
	        ContentValues values = new ContentValues();
	        values.put(DB_utils.KEY_TITLE, "foo");
	        values.put(DB_utils.VAL_TITLE, "bar");
	        dbRW.insert(DB_utils.TABLE_NAME, null, values);
	       		
	        JSONObject jsonObj = new JSONObject();
	        try {
				jsonObj.put("login", "foo");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Set the first name/pair
	 	    try {
				jsonObj.put("pass", "bar");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	    
	 	    String sBase64 =  Base64.encodeToString(jsonObj.toString().getBytes(), Base64.DEFAULT); 
	 	
	 	    
	 	    byte[] data = Base64.decode(sBase64, Base64.DEFAULT);
	 	    try 
	 	    {
			   String text = new String(data, "UTF-8");
			   Log.w (text, sBase64);
			   Log.w (text + "-> MD5", HA_utils.md5(text)); 	  
		    } catch (UnsupportedEncodingException e) 
		    {
			   e.printStackTrace();
		    } 	  
	 	  
	        String selectQuery = DB_utils.SQLselectQuery;
	        
	        // Cursor cursor = dbRW.rawQuery(selectQuery, null);        
	        Cursor cursor = dbRW.query(DB_utils.TABLE_NAME, 
	                        new String[] {DB_utils.VAL_TITLE }, 
	                        DB_utils.KEY_TITLE + "=" + DB_utils.KEY_ID_NAME,
	                        null, null, null, null, null);
	              
	        if (cursor.moveToFirst())        	           
	           Log.w (selectQuery, cursor.getString(0));             
	        else 
	           Log.w (selectQuery, "no match entry");                   
	        
	       // File f=new File("/data/data/your.app.package/databases/your_db.db3");
	       // FileInputStream fis=null;
	       // FileOutputStream fos=null;	
		
		}	
		
		if(v == bExit) 
		{
			Log.w ("Trying exit", " ");
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			super.onDestroy();    
			System.exit(1);
		}
		
		if(v == bLogin) 
		{
			String  userName = "foo";
			String  userPass = "bar";				
						
		    String logIn = etLogin.getText().toString();
		    String passWord = etPassword.getText().toString();
		        		        
		    if ( logIn.startsWith(userName) && passWord.startsWith(userPass) )
		    {	        
		        Intent i = new Intent(getApplicationContext(), HomeautoActivity.class);
                startActivity(i);
                finish();           		
		    } 
		    else 
		    {
		        lLogin.setText("Wrong username & password, tre again!");
		    }
        }		
	}
}