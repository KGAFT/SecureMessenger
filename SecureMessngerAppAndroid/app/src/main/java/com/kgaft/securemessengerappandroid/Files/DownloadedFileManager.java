package com.kgaft.securemessengerappandroid.Files;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppPropertiesTable;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppProperty;

import java.io.File;

public class DownloadedFileManager {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static File getFile(Context context, String iconHolderLogin, FilesNativeCalls networkFiles, String filesDirectory){
        try{
            AppPropertiesTable appProperties = new AppPropertiesTable(context, null, AppProperty.class);
            AppProperty user = (AppProperty) appProperties.getProperties();
            String fileName = networkFiles.getFileName(user.getAppId(), "iconHolderLogin="+iconHolderLogin);
            return networkFiles.downloadFile(iconHolderLogin+"."+IOUtil.getFileExtension(fileName), filesDirectory, "iconHolderLogin="+iconHolderLogin, user.getAppId());
        }catch (Exception e){
            return null;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static File getEncryptedFile(Context context, long fileId, FilesEncryptedNativeCalls filesEncrypted, byte[] encryptionKey, String filesDirectory){
        try{
            File file = getFileById(fileId, filesDirectory);
            if(file!=null){
                return file;
            }
            else{
                AppProperty appUser = (AppProperty) new AppPropertiesTable(context, null, AppProperty.class).getProperties();
                return filesEncrypted.downloadAndDecryptFile(appUser.getAppId(), fileId, encryptionKey, filesDirectory);
            }

        }catch (Exception ignored){
            return null;
        }
    }
    private static File getFileById(long id, String directory) {
        File file = new File(directory);
        for (File listFile : file.listFiles()) {
            try {
                String fileName = IOUtil.getFileNameWithoutExtension(listFile.getName());
                if(Long.parseLong(fileName) == id){
                    return listFile;
                }
            } catch (Exception e) {

            }
        }
        return null;
    }

}
