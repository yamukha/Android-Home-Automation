package crypto_utils;

import java.io.UnsupportedEncodingException;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class Crypto_utils 
{	
	public static byte[] enCrypt(String input,Key pkey,Cipher c) 
			throws InvalidKeyException, BadPaddingException,IllegalBlockSizeException 
    {			
	  c.init(Cipher.ENCRYPT_MODE, pkey);
      byte[] inputBytes = input.getBytes();	
      byte[] encryptedText = c.doFinal(inputBytes);     
	  return encryptedText;		  
	}

	public static String deCrypt(byte[] encryptionBytes,Key pkey,Cipher c) 
		  throws InvalidKeyException,	BadPaddingException, IllegalBlockSizeException 
    {
	  c.init(Cipher.DECRYPT_MODE, pkey);
	  byte[] decrypt = c.doFinal(encryptionBytes);
	  String decrypted = new String(decrypt);
	  return decrypted;
	}

	public static String bytesToHex(byte[] data) 
	{
		if (data == null)
			return null;

		String str = "";

		for (int i = 0; i < data.length; i++) 
		{
			if ((data[i] & 0xFF) < 16)
				str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
			else
				str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
		}
		return str;
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
