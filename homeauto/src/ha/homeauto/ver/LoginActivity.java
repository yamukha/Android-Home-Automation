// ref to http://www.androidhive.info/2011/10/android-login-and-registration-screen-design/

package ha.homeauto.ver;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.SecretKeyFactory;

import crypto_utils.Crypto_utils;

import db_util.DB_utils;
import ha.homeauto.ver.HomeautoActivity;
import ha.homeauto.ver.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
	protected TextView lPassword ;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
      
	    setContentView(R.layout.register);
	         
	    bExit = (Button) findViewById(R.id.btnExit);
	    bRegister = (Button) findViewById(R.id.btnRegister);
	    bLogin = (Button) findViewById(R.id.btnLogin);
	     
	    lLogin  = (TextView) findViewById(R.id.lableLogin);
	    lPassword  = (TextView) findViewById(R.id.lablePassword);
	     
	    etPassword = (EditText) findViewById(R.id.textPassword);
	    etLogin    = (EditText) findViewById(R.id.textLogin);
	     
	    bExit.setOnClickListener (this);
	    bLogin.setOnClickListener( this);
	    bRegister.setOnClickListener( this);
	    bLogin.requestFocus();
	    
	    DB_utils db = new DB_utils (this);
	    SQLiteDatabase dbRW = db.getWritableDatabase();
	   	    
	    Cursor dbCursor = dbRW.rawQuery(DB_utils.SQL_CHECK_ENTRIES,null);
	    
	    if (dbCursor .moveToFirst()) 
	    {
	    	bRegister.setVisibility(View.INVISIBLE);    
	    }
	    //else	           	
	    
		dbCursor.close();	
		db.close();
	    
   }

	public void onClick(View v) 
	{	
	    
		if(v == bRegister || v == bLogin)			
		{
			DB_utils db = new DB_utils (this);
		    SQLiteDatabase dbRW = db.getWritableDatabase();
		    
			String logIn = etLogin.getText().toString();
			String passWord = etPassword.getText().toString();
			
			String algorithm = "DES";
			Key symKey = null;
			Cipher c = null;
			byte[] encryptionBytes = null;
			String toEncrypt = "";
			String myEncryptKey = "";
			SecretKeyFactory mySecretKeyFactory = null ;
			KeySpec myKeySpec = null;
			byte[] keyAsBytes = null;
			
			if ( 8 > passWord.length () || ( 3 > logIn.length () ))
			{	
				lPassword.setText("Be at least 8 characters !");
				lLogin.setText("Be at least 3 characters !");	
			}	
			else 				
			{
				toEncrypt = logIn;
				myEncryptKey = passWord;
				lPassword.setText("Password");
				lLogin.setText("Login");
				keyAsBytes = myEncryptKey.getBytes();
				
				try { myKeySpec = new DESKeySpec(keyAsBytes);} 
				catch (InvalidKeyException e2) { e2.printStackTrace(); }
							
				try {  mySecretKeyFactory = SecretKeyFactory.getInstance(algorithm);}
				catch (NoSuchAlgorithmException e2) {e2.printStackTrace();	}
						
				try { c = Cipher.getInstance(algorithm);}
				catch (NoSuchAlgorithmException e1) {e1.printStackTrace();}
				catch (NoSuchPaddingException e1) {	e1.printStackTrace(); }
		
				try { symKey = mySecretKeyFactory.generateSecret(myKeySpec);}
				catch (InvalidKeySpecException e2) {e2.printStackTrace();	}  
				
//	      	try { symKey = KeyGenerator.getInstance(algorithm).generateKey(); }
//			catch (NoSuchAlgorithmException e1) {	e1.printStackTrace();		} 

				try { encryptionBytes = Crypto_utils.enCrypt( toEncrypt,symKey,c);}
				catch (InvalidKeyException e1)	{	e1.printStackTrace(); } 
				catch (BadPaddingException e1) 	{	e1.printStackTrace(); }
				catch (IllegalBlockSizeException e1) { 	e1.printStackTrace();}
				
				 String sBase64 =  Base64.encodeToString(encryptionBytes, Base64.DEFAULT);	 
			     Log.w (" crypted", sBase64);
				       
				if(v == bRegister )
				{	
				
		        ContentValues values = new ContentValues();
		        values.put(DB_utils.KEY_TITLE, logIn );
		        values.put(DB_utils.VAL_TITLE, sBase64);
		        dbRW.insert(DB_utils.TABLE_NAME, null, values);		      
				}
				
				if(v == bLogin) 
				{	            
					String decrypted ="";
					String  userPass = "";
					boolean isDB = false;
					 
					Cursor cursor = dbRW.query(DB_utils.TABLE_NAME,  new String[] {DB_utils.VAL_TITLE }, 
			                        DB_utils.KEY_TITLE + " = " + "'"+ logIn+"'",  null, null, null, null, null);
					 
					try { decrypted = Crypto_utils.deCrypt(encryptionBytes,symKey,c);} 
					catch (InvalidKeyException e1)	{e1.printStackTrace();	}
					catch (BadPaddingException e1) {e1.printStackTrace();} 
					catch (IllegalBlockSizeException e1) {e1.printStackTrace();	}
					  
					if (cursor.moveToFirst())     
					{	
					   userPass = cursor.getString(0);
					   isDB =true;
					}   
					else
					{
					   Log.w ("selectQuery", "no match entry");
					}  
					
					if ( logIn.startsWith(decrypted) && sBase64.startsWith(userPass) &&  true==isDB)
					{	        
					    Intent i = new Intent(getApplicationContext(), HomeautoActivity.class);
			            startActivity(i);
			            finish();           		
					} 
					else 
					    lLogin.setText("Wrong username & password, tre again!");
					cursor.close();  
				}	
		       
		        /*		
		        JSONObject jsonObj = new JSONObject();
		        try { jsonObj.put("login", logIn); 	} 
		        catch (JSONException e) {e.printStackTrace();} 
		 	    try { jsonObj.put("pass", sBase64);}
		 	    catch (JSONException e) {e.printStackTrace();}
		 	 	*/
				
		 	    // byte[] data = Base64.decode(sBase64, Base64.DEFAULT); 
		        // File f=new File("/data/data/your.app.package/databases/your_db.db3");
		        // FileInputStream fis=null;
		        // FileOutputStream fos=null;	
			}
		}	
		
		if(v == bExit) 
		{			
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			super.onDestroy();    
			System.exit(1);
		}   
        		
	}
}