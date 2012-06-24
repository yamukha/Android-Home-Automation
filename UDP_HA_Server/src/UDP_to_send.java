import java.io.*;
import javax.xml.bind.DatatypeConverter;
import java.net.*;

public class UDP_to_send {

public static int UDPlistenPort = 14666 ;
public static int UDPoutputPort = 14667 ;

public static void main(String[] args) throws IOException
{
    boolean set_type = true;
    DatagramSocket serverSocket = null;
    
    System.out.println("Home Automation Server. Version 1.0");
    System.out.println("By yamukha@ukr.net. All right reserved.");
    System.out.println("Waiting for data");
    
    try
    {
    serverSocket = new DatagramSocket(UDPlistenPort);
    serverSocket.setBroadcast(true);
    }
    catch (SocketException ex)
   	{
        System.out.println("Data not sent!");    
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

            int queries_count = 5;
            String query   = "";
            String query_x = new String ("not supported");
            
            if ( 's' == payload [2] )
            {
               set_type = true; 
            }
            else // set command
            {
               set_type = false;  
            }    
            
            String [] commands = new String []{"ping","reset","shout down",
                "mute on","mute off", "not supported"};
            
            // TODO rewrite with real commands
            if (set_type)
            {    
                switch (payload [0])
                {    
                     case '0': query = commands[0] + " Done"; break;
                     case '1': query = commands[1] + " Done"; break;
                     case '2': query = commands[2] + " Done"; break;
                     case '3': query = commands[3] + " Done"; break;
                     case '4': query = commands[4] + " Done"; break;
                     default : query = query_x; break; 
                }
            }
            else
            {
            	switch (payload [0])
                {
                     case '0': for (int j = 0 ; j < queries_count; j++)
                         {
                            query += commands[j] + "|" ;  
                         } break;
                     case '1': query = commands[1] ; break;
                     case '2': query = commands[2] ;  break;
                     case '3': query = commands[3] ; break;
                     case '4': query = commands[4] ; break;
                     default : query = query_x; break; 
                }
            }
            byte[] query2sent = query.getBytes();
            
            DatagramPacket sendResponse =
            new DatagramPacket(query2sent, query2sent.length, IPAddress, port);
            
            serverSocket.send(sendResponse);
            System.out.println("Try to sent:" + " [" + query + "]");
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
}