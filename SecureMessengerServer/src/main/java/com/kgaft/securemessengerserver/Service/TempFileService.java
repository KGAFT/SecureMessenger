package com.kgaft.securemessengerserver.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

/**
 * @author KGAFT
 * @version 1.0
 * This class created, for saving temp multipart files, before inserting it to SQL Database
 */
public class TempFileService {
    private String tempDirectory;
    private File tempFile;

    /**
     * @param tempDirectory this field is the path to folder for temp files
     */
    public TempFileService(String tempDirectory){
        this.tempDirectory = tempDirectory;
    }

    /**
     * This method saving temp files to given directory in the constructor
     * @param multipartFile
     * @throws IOException in the try catch block, you need to call method: clearTempFile()
     */
    public void saveTempMultipartFile(MultipartFile multipartFile) throws IOException {
        String fileName = generateRandomTempFileName();
        tempFile = new File(tempDirectory+"/"+fileName);
        multipartFile.transferTo(tempFile);
    }

    /**
     * Call this method, when you need to save the use file to SQL Database
     * @return fileInputStream to temp saved file
     * @throws FileNotFoundException
     */
    public FileInputStream getTempFileInputStream() throws FileNotFoundException {
        if(tempFile!=null){
            return new FileInputStream(tempFile);
        }
        else{
            return null;
        }
    }

    /**
     * This method deletes the temp file
     */
    public void clearTempFile(){
        tempFile.delete();
    }

    /**
     *
     * @return Generated id, that does not belong to any files instead, of current file. Warning this is not file id in Database
     */
    private String generateRandomTempFileName(){
        String newFileName = "tempFile";
        int randomId = new Random().nextInt();
        while(new File(tempDirectory+"/"+newFileName+randomId+".txt").exists()){
            randomId = new Random().nextInt();
        }
        newFileName+=randomId+".txt";
        return newFileName;
    }
}
