package com.kgaft.securemessengerserver.Controllers;

import com.kgaft.securemessengerserver.DataBase.Entities.FileEntity;
import com.kgaft.securemessengerserver.DataBase.Entities.ResponseEntity;
import com.kgaft.securemessengerserver.DataBase.JDBC.JDBCFileDB;
import com.kgaft.securemessengerserver.DataBase.Repositories.AuthorizedUsersRepo;
import com.kgaft.securemessengerserver.Service.TempFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;

@RestController
public class FileController {
    @Autowired
    private AuthorizedUsersRepo apps;
    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam MultipartFile file, @RequestParam(name="fileName") String fileName, @RequestParam(name="appId")String appId) throws IOException {
        if(authorizeByAppId(appId)){
            TempFileService fileService = new TempFileService(System.getenv("temp"));
            try{
                fileService.saveTempMultipartFile(file);
                FileEntity fileEntity = new FileEntity();
                fileEntity.setInputStream(fileService.getTempFileInputStream());
                fileEntity.setFileName(fileName);
                String fileId = String.valueOf(JDBCFileDB.saveFile(fileEntity));
                fileService.clearTempFile();
                return fileId;
            }catch (Exception e){
                fileService.clearTempFile();
                return "Failed!";
            }
        }
        return "Failed";
    }
    @GetMapping("/getFile")
    public void getFile(@RequestParam(name="appId") String appId, @RequestParam(name= "fileId")long fileId, HttpServletResponse response) throws SQLException, IOException {
        if(authorizeByAppId(appId)){
            FileEntity file = JDBCFileDB.getFileById(fileId);
            writeInputStreamToOutputStream(file.getInputStream(), response.getOutputStream());
        }

    }
    @GetMapping("/getFileName")
    public String getNameOfFile(@RequestParam(name="appId") String appId, @RequestParam(name="fileId")String fileId){
        if(authorizeByAppId(appId)){
            try {
                String fileName = JDBCFileDB.getNameOfFile(Long.parseLong(fileId));
                return fileName;
            } catch (SQLException e) {
                return "";
            }
        }
        return "";
    }
    private void writeInputStreamToOutputStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[4*1024];
        int read;
        while((read=inputStream.read(buffer, 0, buffer.length))!=-1){
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();
        inputStream.close();
    }
    private boolean authorizeByAppId(String appId){
        try{
            return apps.findById(Long.parseLong(appId)).get()!=null;
        }catch (Exception e){
            return false;
        }

    }
}
