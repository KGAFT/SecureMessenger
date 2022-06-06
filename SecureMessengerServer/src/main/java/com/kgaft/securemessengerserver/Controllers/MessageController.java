package com.kgaft.securemessengerserver.Controllers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.kgaft.securemessengerserver.DataBase.DAO.FileDAO;
import com.kgaft.securemessengerserver.DataBase.Entities.FileEntity;
import com.kgaft.securemessengerserver.DataBase.Entities.MessageEntity;
import com.kgaft.securemessengerserver.DataBase.Repositories.MessageRepo;
import com.kgaft.securemessengerserver.Service.AuthorizedDevicesService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

@RestController
public class MessageController {
    @Autowired
    private MessageRepo messageRepo;
    @GetMapping("/getMessages")
    public String getCurrentMessages(@RequestParam(name="appId")String appId){
        if(AuthorizedDevicesService.authorize(appId)){
            Iterable<MessageEntity> messages = messageRepo.findMessageByReceiverOrSender(AuthorizedDevicesService.getUser(appId).getLogin(), new Timestamp(System.currentTimeMillis()-132*60*60*1000));
            return new GsonBuilder().create().toJson(messages).toString();
        }
        return "Error";
    }
    @PostMapping("/sendMessage")
    public String sendMessage(@RequestParam(name="appId")String appId,@RequestParam(name="receiver") String receiver,@RequestParam(name="text") String text,@RequestParam(name="files") String files){
        if(AuthorizedDevicesService.authorize(appId)){
            MessageEntity message = new MessageEntity();
            message.setTime(new Timestamp(System.currentTimeMillis()));
            message.setText(text);
            message.setReceiver(receiver);
            message.setContentId(Arrays.stream(files.split(";")).mapToLong(num->Long.parseLong(num)).toArray());
            message.setSender(AuthorizedDevicesService.getUser(appId).getLogin());
            messageRepo.save(message);
            return "Success!";
        }
        else{
            return "Failed!";
        }
    }
    @PostMapping("/uploadFile")
    public String uploadFile(HttpServletRequest request, String fileName) throws IOException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setInputStream(request.getInputStream());
        fileEntity.setFileName(fileName);
        try {
            return String.valueOf(FileDAO.saveFile(fileEntity));
        } catch (SQLException e) {
            return "Failed";
        }
    }
    @GetMapping("/getFile")
    public void getFile(@RequestParam(name="appId") String appId, @RequestParam(name="fileId")long fileid, HttpServletResponse response) throws SQLException, IOException {
        if(AuthorizedDevicesService.authorize(appId)){
            int canRead;
            byte[] buffer = new byte[8*1024];
            FileEntity file = FileDAO.getFileById(fileid);
            InputStream is = file.getInputStream();
            while((canRead= is.read(buffer))!=-1){
                response.getOutputStream().write(buffer,0 , canRead);
            }
        }

    }
    @GetMapping("/getFileName")
    public String getNameOfFile(@RequestParam(name="appId") String appId, @RequestParam(name="fileId")String fileId){
        if(AuthorizedDevicesService.authorize(appId)){
            try {
                return FileDAO.getNameOfFile(Long.parseLong(fileId));
            } catch (SQLException e) {
                return "";
            }
        }
        return "";
    }

}
