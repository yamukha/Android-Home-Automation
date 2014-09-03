package ha_util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HA_utils 
{
    public String [] get_cmd_List (String inString, String delims)
    {        
        String[] tokens = inString.split(delims);
        return tokens;
    }

    public int get_List_entries_count (byte [] payload, byte j)
    {
        int entries_counter = 0; 	
        for (int i = 0 ; i < payload.length; i++)
            {
                if ( j == payload [i]) entries_counter++;
            }
        return entries_counter;
    }
    
    public String cutString (String inString)
    {   
        if ( inString.length () > 0)
        {	
            StringBuilder strB = new StringBuilder(inString);
            strB.deleteCharAt(inString.length() - 1);
            return strB.toString();
        }
        else  return "";
    }
    
    public static String md5(String string) 
    {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        return hex.toString();
    }


    
}


