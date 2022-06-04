package com.kgaft.securemessengerapp.Utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {

    //Warning this method encrypts String by my own algorythm, and other parts out of CryptoUtil can be decrypt it wrong
    public static String wrapString(String content, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, "RawBytes"));
        byte[] bContent = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return bContent.toString();
    }
    public static long wrapLong(long content, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, "RawBytes"));
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(content);
        byte[] bContent = cipher.doFinal(buffer.array());
        buffer.clear();
        buffer.put(bContent);
        buffer.flip();
        return buffer.getLong();
    }
    public static String decryptString(String msg, String key){
        String[] sBytes = msg.split(" ");
        return null;
    }
}
