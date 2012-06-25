import java.io.*;
import javax.xml.bind.DatatypeConverter;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UDP_to_send {

public static int UDPlistenPort = 14666 ;
public static int UDPoutputPort = 14667 ;

public static void main(String[] args) throws IOException
{
    boolean set_type = true;
    DatagramSocket serverSocket = null;
    String XMLfileName = "commands.xml";
    String CMDfile = null;
    String CMDpath = null;
    long XMLsize; 
    String[] cmd_List ={"ping"};
    int cmd_count = 0;

    System.out.println("Home Automation Server. Version 1.0");
    System.out.println("By yamukha@ukr.net. All right reserved.");
    
    System.out.println ("Try to read file [" + XMLfileName + "] in current folder.");
    CMDpath = System.getProperty("user.dir").toString(); 
    CMDfile = CMDpath + File.separator + XMLfileName; 
    XMLsize = getFileSize (CMDfile);
    
    if (0 >= XMLsize )
    {
        System.out.println(String.valueOf (XMLsize));
        System.out.println("File read Error!");
        return;
    }
    else System.out.println("File read OK!");
    
    System.out.println("Try to parse XML!");
    Path XMLpath = Paths.get(CMDfile);
        
    byte [] XMLraw = Files.readAllBytes((Path) XMLpath);
    String XMLsentence = new String(XMLraw);
    
    System.out.println(XMLsentence);
    
    // parse TXT/XML
    cmd_count = get_List_entries_count (XMLraw, (byte) '|');
    cmd_List = get_cmd_List(XMLsentence, "[|]");
    System.out.println("Has " +  String.valueOf(cmd_count) + " commands");
    
    System.out.println("Waiting for data");
    
    try
    {
    serverSocket = new DatagramSocket(UDPlistenPort);
    serverSocket.setBroadcast(true);
    }
    catch (SocketException ex)
   	{
        System.out.println("Can not open port " + String.valueOf(UDPlistenPort));    
    };
   	try {        
        InetAddress IPAddress = null;
            
        byte[] receiveData = new byte[16];
        int port = 0;
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        // listen and answer
        while (true) 
        {
            serverSocket.receive(receivePacket);
            
            byte [] payload = receivePacket.getData();
            for (int i = 0 ; i < payload.length; i++)
            {
                if ( 0 == payload [i]) payload[i] = 0x20;
            }	 
            
            String sentence = new String(payload);
            System.out.println("RECEIVED: " + sentence );
            IPAddress = receivePacket.getAddress();
            port = receivePacket.getPort();
            int index = payload [0] - '0';
            byte[] query2sent = null;
            String query_x = new String ("not supported");
            
            if ( 's' == payload [2] )
            {
               set_type = true; 
            }
            else // set command
            {
               set_type = false;  
            }    
            
            if (set_type)
            {
               	switch (index)
                {
                    case 0 : query2sent = XMLraw;
                        break;
                        
                    default :
                    {	
                        if ( cmd_count > index) 
                        {
                            try
                            {
                                Process p = null;	
                                p = Runtime.getRuntime().exec(CMDpath + File.separator+ cmd_List[index]);
                                System.out.println("Command " + cmd_List[index] + " was executed");
                            }
                            catch (IOException ex)
                            {
                                System.out.println(ex);
                            }
                            query2sent = cmd_List[index].getBytes();
                        }
                        
                        else
                            query2sent = query_x.getBytes();
                    } 
                }
                
            }
            else 
            {    
                switch (index)
                {
                    case 0 : query2sent = XMLraw;
                        break;
                        
                    default :
                    {	
                        if ( cmd_count > index) 
                            query2sent = cmd_List[index].getBytes();
                        else 
                            query2sent = query_x.getBytes();
                        break;
                    } 
                }
            }     
            	
            DatagramPacket sendResponse =
            new DatagramPacket(query2sent, query2sent.length, IPAddress, port);
            
            serverSocket.send(sendResponse);
            String datasent = new String(query2sent);
            System.out.println("Try to sent:" + " [" +  datasent + "]");
        }// while
        }
        catch (SocketException ex)
   	    {
            System.out.println("Data not sent!");    
        };
   } 	

    public static String toHexString(byte[] array) 
    {
        return DatatypeConverter.printHexBinary(array);
    }

    public static byte[] toByteArray(String s) 
    {
        return DatatypeConverter.parseHexBinary(s);
    }
    
    public static long getFileSize(String filename) 
    {
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
          System.out.println("File [" + filename + "] doesn\'t exist");
          return -1;
        }
        return file.length();
    }
    
    public static String [] get_cmd_List (String inString, String delims)
    {        
        String[] tokens = inString.split(delims);
        return tokens;
    }
    
    public static int get_List_entries_count (byte [] payload, byte j)
    {
        int entries_counter = 0; 	
        for (int i = 0 ; i < payload.length; i++)
            {
                if ( j == payload [i]) entries_counter++;
            }
        return entries_counter;
    }
}