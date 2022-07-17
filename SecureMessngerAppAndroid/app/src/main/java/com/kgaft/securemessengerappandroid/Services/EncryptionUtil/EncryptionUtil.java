package com.kgaft.securemessengerappandroid.Services.EncryptionUtil;



import android.os.Build;

import androidx.annotation.RequiresApi;

import com.kgaft.securemessengerappandroid.Database.TableColumn;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class EncryptionUtil {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static IEncrypted decrypt(IEncrypted objectToDecrypt, byte[] decryptionKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        List<TableColumn> decryptedFields = new ArrayList<>();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptionKey, "AES"));
        objectToDecrypt.getFieldsToEncryptDecrypt().forEach(field->{
            byte[] content = encryptedStringToByteArray(field.getColumnValue());
            try {
                content = cipher.doFinal(content, 0, content.length);
                field.setColumnValue(new String(content, StandardCharsets.UTF_8));
                decryptedFields.add(field);
            } catch (IllegalBlockSizeException | BadPaddingException e) {
                throw new RuntimeException(e);
            }
        });
        objectToDecrypt.initWithValues(decryptedFields);
        return objectToDecrypt;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static IEncrypted encrypt(IEncrypted objectToEncrypt, byte[] encryptionKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException{
        List<TableColumn> encryptedFields = new ArrayList<>();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptionKey, "AES"));
        objectToEncrypt.getFieldsToEncryptDecrypt().forEach(field->{
            byte[] content = field.getColumnValue().getBytes(StandardCharsets.UTF_8);
            try {
                content = cipher.doFinal(content, 0, content.length);
                field.setColumnValue(byteArrayToString(content));
                field.setColumnType("TEXT");
                encryptedFields.add(field);
            } catch (IllegalBlockSizeException | BadPaddingException e) {
               e.printStackTrace();
            }
        });
        objectToEncrypt.initWithValues(encryptedFields);
        return objectToEncrypt;
    }
    public static byte[] generateEncryptionKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        return keyGen.generateKey().getEncoded();
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
