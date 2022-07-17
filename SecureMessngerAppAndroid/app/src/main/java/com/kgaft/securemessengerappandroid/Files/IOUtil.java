package com.kgaft.securemessengerappandroid.Files;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class IOUtil {
    public static void writeArgumentsToOutputStream(String arguments, OutputStream outputStream) throws IOException {
        byte[] textContent = arguments.getBytes(StandardCharsets.UTF_8);
        outputStream.write(textContent, 0, textContent.length);
    }
    public static JsonObject inputStreamToJsonObject(InputStream inputStream) throws IOException {
        JsonParser stringParser = new JsonParser();
        return stringParser.parse(inputStreamToString(inputStream)).getAsJsonObject();
    }
    public static String inputStreamToString(InputStream stream) throws IOException {
        byte[] textContent = new byte[4*1024];
        int read = stream.read(textContent, 0, textContent.length);
        return new String(textContent, 0, read);
    }
    public static void writeInputStreamToOutputStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[4*1024];
        int read;
        while((read = inputStream.read(buffer, 0, buffer.length))!=-1){
            outputStream.write(buffer, 0, read);
        }
    }
    @SuppressLint("Range")
    public static String getUriFileName(Uri file, ContentResolver contentResolver){
        Cursor fileInfo = contentResolver.query(file, null, null, null, null);
        fileInfo.moveToFirst();
        return fileInfo.getString(fileInfo.getColumnIndex(OpenableColumns.DISPLAY_NAME));
    }
    @SuppressLint("Range")
    public static long getUriFileSize(Uri file, ContentResolver contentResolver){
        Cursor cursor = contentResolver.query(file, null, null, null, null);
        if(cursor.moveToFirst()){
            return cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
        }
        return 0;
    }
    public static File transferUriToCacheDir(Uri file, ContentResolver contentResolver, String cacheDirectory){
        File fileCache = new File(cacheDirectory+"/"+getUriFileName(file, contentResolver));
        try {
            FileOutputStream fos = new FileOutputStream(fileCache);
            InputStream uriIs = contentResolver.openInputStream(file);
            writeInputStreamToOutputStream(uriIs, fos);
            fos.flush();
            uriIs.close();
            fos.close();
            return fileCache;
        } catch (IOException e) {
            return null;
        }

    }
    public static void decryptFile(byte[] encryptionKey, String inputDestination, String outputDestination) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(encryptionKey, "AES"));
        CipherInputStream cis = new CipherInputStream(new FileInputStream(inputDestination), cipher);
        FileOutputStream fos = new FileOutputStream(outputDestination);
        writeInputStreamToOutputStream(cis, fos);
        fos.flush();
    }
    public static void saveTempEncryptedFile(File file, byte[] encryptionKey, String destinationWithFileName) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        FileInputStream fis = new FileInputStream(file);
        File tempEncryptedFile = new File(destinationWithFileName);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptionKey, "AES"));
        CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(destinationWithFileName), cipher);
        writeInputStreamToOutputStream(fis, cos);
        cos.flush();

    }

    public static String getFileNameWithoutExtension(String fileName){
        String newFileName = "";
        for (char c : fileName.toCharArray()) {
            if (c!='.'){
                newFileName+=c;
            }
            else{
                break;
            }

        }
        return newFileName;
    }
    public static String getFileExtension(String fileName){
        boolean isExtension = false;
        String extension = "";
        for (char c : fileName.toCharArray()) {
            if(isExtension){
                extension+=c;
            }
            if(!isExtension){
                isExtension = c=='.';
            }
        }
        return extension;
    }

}
