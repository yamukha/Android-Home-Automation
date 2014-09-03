package ha_util;

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
}


