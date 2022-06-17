

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class EncryptedFileManager {
    private String downloadUrl;
    private String uploadUrl;
    private byte[] encryptionKey;
    private String baseDir;
    private String infoUrl;
    private String appId;
    private Cipher cipher;
    public EncryptedFileManager(String downloadUrl, String uploadUrl, String infoUrl, String appId, byte[] encryptionKey, String baseDir) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.downloadUrl = downloadUrl;
        this.uploadUrl= uploadUrl;
        this.encryptionKey = encryptionKey;
        this.baseDir = baseDir;
        this.infoUrl = infoUrl;
        this.appId=appId;
        new File(baseDir).mkdirs();
        cipher = Cipher.getInstance("AES");
    }
    public void downloadFileAndDecrypt(long fileId) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        FileOutputStream fos = new FileOutputStream(baseDir+"/"+getFileName(fileId));
        HttpURLConnection connection = (HttpURLConnection) new URL(downloadUrl+"?"+"appId="+appId+"&"+"fileId="+String.valueOf(fileId)).openConnection();
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.connect();
        CipherInputStream cis = new CipherInputStream(connection.getInputStream(), cipher);
        byte[] buffer = new byte[4*1024];
        int read;
        while((read =cis.read(buffer, 0, buffer.length))!=-1){
            fos.write(buffer, 0, read);
        }
        fos.flush();
    }
    public long uploadFile(File file) throws IOException, InvalidKeyException {
        String post = "fileName="+file.getName()+"&"+"appId="+appId;
        File tempFile = saveTempEncryptedFile(file);
        String twoHyphens = "--";
        String boundary =  "*****"+Long.toString(System.currentTimeMillis())+"*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        int maxBufferSize = 1*1024*1024;

        FileInputStream fileInputStream = new FileInputStream(tempFile);

        URL url = new URL(uploadUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "file" + "\"; filename=\"" + tempFile.getName() +"\"" + lineEnd);
        outputStream.writeBytes("Content-Type: text/txt" + lineEnd);
        outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
        outputStream.writeBytes(lineEnd);

        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        while(bytesRead > 0) {
            outputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        outputStream.writeBytes(lineEnd);

        // Upload POST Data
        String[] posts = post.split("&");
        int max = posts.length;
        for(int i=0; i<max;i++) {
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            String[] kv = posts[i].split("=");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + kv[0] + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: text/plain"+lineEnd);
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(kv[1]);
            outputStream.writeBytes(lineEnd);
        }

        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

        InputStream inputStream = connection.getInputStream();
        result = this.convertStreamToString(inputStream);

        fileInputStream.close();
        inputStream.close();
        outputStream.flush();
        outputStream.close();
        return Long.parseLong(result);
    }
    public String getFileName(long fileId) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(infoUrl+"?appId="+appId+"&"+"fileId="+String.valueOf(fileId)).openConnection();
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.connect();
        InputStream is = connection.getInputStream();
        return convertStreamToString(is);
    }
    public String convertStreamToString(InputStream stream) throws IOException {
        byte[] string = new byte[stream.available()];
        stream.read(string, 0, string.length);
        return new String(string);
    }
    private byte[] decryptByteArray(byte[] data, int start, int len) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(encryptionKey, "AES"));
        return cipher.doFinal(data, start, len);
    }
    private byte[] encryptByteArray(byte[] data, int start, int len) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptionKey, "AES"));
        return cipher.doFinal(data, 0, len);
    }
    public File saveTempEncryptedFile(File file) throws InvalidKeyException, IOException {
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptionKey, "AES"));
        FileInputStream fis = new FileInputStream(file);
        File tempFile = new File(baseDir+"/"+"file001.txt");
        FileOutputStream fos = new FileOutputStream(tempFile);
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        byte[] buffer = new byte[4*1024];
        int read;
        while((read=fis.read(buffer, 0, buffer.length))!=-1){
            cos.write(buffer, 0, read);
        }
        cos.flush();
        cos.close();
        return tempFile;
    }
}
