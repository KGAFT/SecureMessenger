package com.kgaft.securemessengerserver.DataBase.Entities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;

public class FileEntity {
    private long fileId;
    private String fileName;
    private InputStream inputStream;

    private Timestamp time;
    private FileInputStream fileInput;

    public FileEntity(long fileId, String fileName, InputStream inputStream, Timestamp time) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.inputStream = inputStream;
        this.time = time;
    }

    public FileEntity(long fileId, String fileName, FileInputStream fileInput) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileInput = fileInput;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public FileInputStream getFileInput() {
        return fileInput;
    }


    public void setFileInput(FileInputStream fileInput) {
        this.fileInput = fileInput;
    }

    public FileEntity() {
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
