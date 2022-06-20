package com.kgaft.SecureMessengerApiLibrary;



import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
    private long[] contentId;
    private long time;

    public MessageEntity(long messageid, String sender, String receiver, String messagetext, String contentId, long time) {
        this.messageid = messageid;
        this.sender = sender;
        this.receiver = receiver;
        this.messagetext = messagetext;
        this.time = time;
        try{
            String[] contents = contentId.split(";");
            this.contentId = new long[contents.length];
            for(int counter = 0; counter<contents.length; counter++){
                this.contentId[counter] = Long.parseLong(contents[counter]);
            }
        }catch (Exception e){

        }
    }

    public MessageEntity() {
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

    public long[] getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        try{
            String[] contents = contentId.split(";");
            this.contentId = new long[contents.length];
            for(int counter = 0; counter<contents.length; counter++){
                this.contentId[counter] = Long.parseLong(contents[counter]);
            }
        }catch (Exception e){

        }
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
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

    @Override
    public boolean equals(Object obj) {
        try{
           return ((MessageEntity)obj).getMessageid()==messageid;
        }catch (Exception e){
            return false;
        }
    }
}
