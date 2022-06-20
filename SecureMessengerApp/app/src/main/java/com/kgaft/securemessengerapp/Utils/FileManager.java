package com.kgaft.securemessengerapp.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

public class FileManager {
    private String baseDir;
    private EncryptedFileManager encryptedNetworkFiles;
    private HashMap<Long, File> files = new HashMap<>();
    public FileManager(String downloadUrl, String uploadUrl, String infoUrl, String appId, byte[] encryptionKey, String baseDir) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.baseDir = baseDir;
        encryptedNetworkFiles = new EncryptedFileManager(downloadUrl, uploadUrl, infoUrl, appId, encryptionKey, baseDir);
        initFilesTable();
    }
    public Long[] getAllFilesId(){
        return (Long[]) files.keySet().toArray();
    }
    public File getFileById(long id){
        return files.get(id);
    }
    public Bitmap getImageAsBitmap(long fileId){
        return BitmapFactory.decodeFile(files.get(fileId).getAbsolutePath());
    }
    public void downloadFile(long fileId) throws IOException, InvalidKeyException {
        encryptedNetworkFiles.downloadFileAndDecrypt(fileId);
        refreshFilesTable();
    }
    private void initFilesTable(){
        File file = new File(baseDir);
        File[] allDirectoryFiles = file.listFiles();
        for (File child : allDirectoryFiles) {
            String[] fileNameArgs = child.getName().split(":");
            if(fileNameArgs.length>1){
                try{
                    files.put(Long.parseLong(fileNameArgs[0]), child);
                }catch (Exception e){
                }
            }
        }
    }
    private void refreshFilesTable(){
        File file = new File(baseDir);
        File[] allDirectoriesFiles = file.listFiles();
        for (File child : allDirectoriesFiles) {
            String[] fileNameArgs = child.getName().split(":");
            if(fileNameArgs.length>1){
                try{
                    if(!files.keySet().contains(Long.parseLong(fileNameArgs[0]))){
                        files.put(Long.parseLong(fileNameArgs[0]), child);
                    }
                }catch (Exception e){

                }

            }
        }
    }



}
