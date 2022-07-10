package com.kgaft.SecureMessengerPCClient.BackEnd.FilesEncrypted;

import com.kgaft.SecureMessengerPCClient.BackEnd.Utils.IOUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class FilesEncryptedNativeCalls {
    private String downloadUrl;
    private String uploadUrl;
    private String infoUrl;
    private String crlf = "\r\n";
    private String twoHyphens = "--";
    private String boundary =  "*****";
    public FilesEncryptedNativeCalls(String downloadUrl, String uploadUrl, String infoUrl) {
        this.downloadUrl = downloadUrl;
        this.uploadUrl = uploadUrl;
        this.infoUrl = infoUrl;
    }
    public FilesEncryptedNativeCalls(String serverUrl){
        this.downloadUrl = serverUrl+"getFile";
        this.uploadUrl = serverUrl+"uploadFile";
        this.infoUrl = serverUrl+"getFileName";
    }

    public void downloadAndDecryptFile(long appId, long fileId, byte[] key, String destinationFolder) throws IOException {
        File tempFile = new File(generateFileName(destinationFolder));
        try{
            FileOutputStream fos = new FileOutputStream(tempFile);
            HttpURLConnection connection = (HttpURLConnection) new URL(downloadUrl+"?appId="+appId+"&fileId="+fileId).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            IOUtil.writeInputStreamToOutputStream(connection.getInputStream(), fos);
            fos.flush();
            IOUtil.decryptFile(key, tempFile.getAbsolutePath(), destinationFolder+"/"+getFileName(appId, fileId));
            tempFile.delete();
        }catch (Exception e){
            tempFile.delete();
        }

    }
    public long uploadFile(File file, byte[] encryptionKey, long appId){
        File tempFile = new File(generateFileName(file.getParent()));
        String[] fileDirs = file.getName().split("/");
        String fileName = fileDirs[fileDirs.length-1];
        try{
            IOUtil.saveTempEncryptedFile(file, encryptionKey, tempFile.getAbsolutePath());
            HttpURLConnection httpUrlConnection = null;
            URL url = new URL(uploadUrl+"?appId="+appId+"&fileName="+fileName);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + this.boundary);
            DataOutputStream request = new DataOutputStream(
                    httpUrlConnection.getOutputStream());
            request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    "file" + "\";filename=\"" +
                    tempFile.getName() + "\"" + this.crlf);
            request.writeBytes(this.crlf);
            IOUtil.writeInputStreamToOutputStream(new FileInputStream(tempFile), request);
            request.writeBytes(this.crlf);
            request.writeBytes(this.twoHyphens + this.boundary +
                    this.twoHyphens + this.crlf);

            request.flush();
            request.close();
            httpUrlConnection.connect();
            return Long.parseLong(IOUtil.inputStreamToString(httpUrlConnection.getInputStream()));
        }catch (Exception e){
            tempFile.delete();
            return 0;
        }
    }
    public String getFileName(long appId, long fileId) throws IOException {
        HttpURLConnection fileInfoConnection = (HttpURLConnection) new URL(infoUrl+"?appId="+appId+"&fileId="+fileId).openConnection();
        fileInfoConnection.setRequestMethod("GET");
        fileInfoConnection.setDoInput(true);
        fileInfoConnection.connect();
        return IOUtil.inputStreamToString(fileInfoConnection.getInputStream());
    }
    private String generateFileName(String path){
        Random random = new Random();
        File file = new File(path+"/tempFile"+random.nextInt()+".txt");
        while(file.exists())file = new File(path+"/tempFile"+random.nextInt()+".txt");
        return file.getAbsolutePath();
    }
    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }
}
