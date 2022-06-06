package com.kgaft.securemessengerapp.Network.Entities;

import com.kgaft.securemessengerapp.Utils.Crypt;
import com.kgaft.securemessengerapp.Utils.KeyUtil;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MessageEntity implements Crypt {
    private long messageid;
    private String sender;
    private String receiver;
    private String messagetext;
    private long[] contentid;
    private String time;

    public MessageEntity(long messageid, String sender, String receiver, String messagetext, long[] contentid, String time) {
        this.messageid = messageid;
        this.sender = sender;
        this.receiver = receiver;
        this.messagetext = messagetext;
        this.contentid = contentid;
        this.time = time;
    }

    public long getMessageid() {
        return messageid;
    }

    public void setMessageid(long messageid) {
        this.messageid = messageid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessagetext() {
        return messagetext;
    }

    public void setMessagetext(String messagetext) {
        this.messagetext = messagetext;
    }

    public long[] getContentid() {
        return contentid;
    }

    public void setContentid(long[] contentid) {
        this.contentid = contentid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public void encrypt(String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] byteKey = KeyUtil.stringToByte(key);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(byteKey, "AES"));
        byte[] content = cipher.doFinal(messagetext.getBytes(StandardCharsets.UTF_8));
        messagetext = "";
        for(int counter = 0; counter< content.length; counter++){
            messagetext+=content[counter]+";";
        }
    }



    @Override
    public void encrypt(byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
        byte[] content = cipher.doFinal(messagetext.getBytes(StandardCharsets.UTF_8));
        messagetext = "";
        for(int counter = 0; counter< content.length; counter++){
            messagetext+=content[counter]+";";
        }
    }

    @Override
    public void decrypt(String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] keyByte = KeyUtil.stringToByte(key);
        String[] contentString = messagetext.split(";");
        byte[] content = new byte[contentString.length];
        for(int counter = 0; counter<content.length; counter++){
            content[counter] = Byte.parseByte(contentString[counter]);
        }
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyByte, "AES"));
        content = cipher.doFinal(content);
        messagetext = new String(content, StandardCharsets.UTF_8);
    }

    @Override
    public void decrypt(byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String[] contentString = messagetext.split(";");
        byte[] content = new byte[contentString.length];
        for(int counter = 0; counter<content.length; counter++){
            content[counter] = Byte.parseByte(contentString[counter]);
        }
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
        content = cipher.doFinal(content);
        messagetext = new String(content, StandardCharsets.UTF_8);
    }
}
