package com.kgaft.securemessengerappandroid.Files;



import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FilesNativeCalls {
    private String downloadUrl;
    private String uploadUrl;
    private String infoUrl;
    private String crlf = "\r\n";
    private String twoHyphens = "--";
    private String boundary =  "*****";
    public FilesNativeCalls(String downloadUrl, String uploadUrl, String infoUrl) {
        this.downloadUrl = downloadUrl;
        this.uploadUrl = uploadUrl;
        this.infoUrl = infoUrl;
    }
    public FilesNativeCalls(String baseUrl){
        this.downloadUrl = baseUrl+"getIcon";
        this.infoUrl = baseUrl+"getIconName";
        this.uploadUrl = baseUrl+"uploadIcon";
    }
    public File downloadFile(String fileName, String destination, String fileParamToRequest, long appId) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(downloadUrl+"?appId="+appId+"&"+fileParamToRequest).openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        File file = new File(destination+"/"+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        connection.connect();
        IOUtil.writeInputStreamToOutputStream(connection.getInputStream(), fos);
        fos.flush();
        return file;
    }
    public String getFileName(long appId, String fileRequestParam) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) new URL(infoUrl+"?appId="+appId+"&"+fileRequestParam).openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        return IOUtil.inputStreamToString(connection.getInputStream());
    }
    public String uploadFile(long appId, File file){
        try{
            HttpURLConnection httpUrlConnection = null;
            URL url = new URL(uploadUrl+"?appId="+appId);
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
                    file.getName() + "\"" + this.crlf);
            request.writeBytes(this.crlf);
            IOUtil.writeInputStreamToOutputStream(new FileInputStream(file), request);
            request.writeBytes(this.crlf);
            request.writeBytes(this.twoHyphens + this.boundary +
                    this.twoHyphens + this.crlf);

            request.flush();
            request.close();
            httpUrlConnection.connect();
            return IOUtil.inputStreamToString(httpUrlConnection.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
        return "False";
    }

    }
