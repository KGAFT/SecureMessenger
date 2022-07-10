package com.kgaft.SecureMessengerPCClient.BackEnd.Utils;

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
        BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);
        byte[] textBytes = bufferedInputStream.readAllBytes();
        return new String(textBytes);
    }
    public static void writeInputStreamToOutputStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[4*1024];
        int read;
        while((read = inputStream.read(buffer, 0, buffer.length))!=-1){
            outputStream.write(buffer, 0, read);
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

}
