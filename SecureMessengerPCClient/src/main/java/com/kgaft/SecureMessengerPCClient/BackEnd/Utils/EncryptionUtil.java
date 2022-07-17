package com.kgaft.SecureMessengerPCClient.BackEnd.Utils;

import com.kgaft.SecureMessengerPCClient.BackEnd.Database.Value;
import com.kgaft.SecureMessengerPCClient.BackEnd.Utils.Interfaces.IEncrypted;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class EncryptionUtil {
    public static IEncrypted decrypt(IEncrypted objectToDecrypt, byte[] decryptionKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        List<Value> decryptedFields = new ArrayList<>();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptionKey, "AES"));
        objectToDecrypt.getFieldsToEncryptDecrypt().forEach(field->{
            byte[] content = encryptedStringToByteArray(field.getValue());
            try {
                content = cipher.doFinal(content, 0, content.length);
                field.setValue(new String(content, StandardCharsets.UTF_8));
                decryptedFields.add(field);
            } catch (IllegalBlockSizeException | BadPaddingException e) {
                throw new RuntimeException(e);
            }
        });
        objectToDecrypt.initWithValues(decryptedFields);
        return objectToDecrypt;
    }
    public static IEncrypted encrypt(IEncrypted objectToEncrypt, byte[] encryptionKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException{
        List<Value> encryptedFields = new ArrayList<>();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptionKey, "AES"));
        objectToEncrypt.getFieldsToEncryptDecrypt().forEach(field->{
            byte[] content = field.getValue().getBytes(StandardCharsets.UTF_8);
            try {
                content = cipher.doFinal(content, 0, content.length);
                field.setValue(byteArrayToString(content));
                field.setType(String.class);
                encryptedFields.add(field);
            } catch (IllegalBlockSizeException | BadPaddingException e) {
               e.printStackTrace();
            }
        });
        objectToEncrypt.initWithValues(encryptedFields);
        return objectToEncrypt;
    }
    public static String byteArrayToString(byte[] array){
        StringBuilder result = new StringBuilder();
        for (byte element : array) {
            result.append(element+";");
        }
        return result.toString();
    }
    public static byte[] encryptedStringToByteArray(String encrypted){
        String[] encryptedBytes = encrypted.split(";");
        byte[] result = new byte[encryptedBytes.length];
        for (int counter = 0; counter < encryptedBytes.length; counter++) {
            result[counter] = Byte.parseByte(encryptedBytes[counter]);
        }
        return result;
    }
    public static String keyToString(byte[] encryptionKey){
        StringBuilder keyResult = new StringBuilder();
        for (byte encryptionKeyByte : encryptionKey) {
            keyResult.append(encryptionKeyByte+";");
        }
        return keyResult.toString();
    }
}
