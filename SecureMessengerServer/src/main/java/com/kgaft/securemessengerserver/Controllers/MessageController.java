package com.kgaft.securemessengerserver.Controllers;

import com.google.gson.GsonBuilder;
import com.kgaft.securemessengerserver.DataBase.JDBCDB.JDBCFileDB;
import com.kgaft.securemessengerserver.DataBase.Entities.FileEntity;
import com.kgaft.securemessengerserver.DataBase.Entities.MessageEntity;
import com.kgaft.securemessengerserver.DataBase.Entities.ResponseEntity;
import com.kgaft.securemessengerserver.DataBase.Repositories.MessageRepo;
import com.kgaft.securemessengerserver.Service.AuthorizedDevicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

@RestController
public class MessageController {
    @Autowired
    private MessageRepo messageRepo;
    private HashMap<String, String> waitingToJoin = new HashMap<>();
    @GetMapping("/startChat")
    public String startChat(@RequestParam(name="appId") String appId){
        if (AuthorizedDevicesService.authorize(appId)) {
            try {
                String response = new ResponseEntity(waitingToJoin.get(AuthorizedDevicesService.getUser(appId).getLogin())).toJson();
                waitingToJoin.remove(AuthorizedDevicesService.getUser(appId).getLogin());
                return response;
            }catch (Exception e){
                return "Error";
            }
        }
        return "Error: cannot authorize";
    }
    @PostMapping("/joinChat")
    public String joinChat(@RequestParam(name = "appId")String appId, @RequestParam(name="receiver") String receiver){
        if(AuthorizedDevicesService.authorize(appId)){
            waitingToJoin.put(receiver, AuthorizedDevicesService.getUser(appId).getLogin());
            return new ResponseEntity("true").toJson();
        }
        return new ResponseEntity("Error: cannot authorize").toJson();
    }
    @GetMapping("/getMessagesByTime")
    public String getCurrentMessages(@RequestParam(name="appId")String appId, @RequestParam(name="timeInMilliseconds") long time){
        if(AuthorizedDevicesService.authorize(appId)){
            Iterable<MessageEntity> messages = messageRepo.findMessageByReceiverOrSender(AuthorizedDevicesService.getUser(appId).getLogin(), time);
            return new GsonBuilder().create().toJson(messages).toString();
        }
        return "Error";
    }
    @GetMapping("/getMessages")
    public String getAllMessages(@RequestParam(name="appId")String appId){
        if(AuthorizedDevicesService.authorize(appId)){
            Iterable<MessageEntity> messages = messageRepo.findMessageByReceiverOrSender(AuthorizedDevicesService.getUser(appId).getLogin());
            return new GsonBuilder().create().toJson(messages).toString();
        }
        return "Error";
    }
    @PostMapping("/sendMessage")
    public String sendMessage(@RequestParam(name="appId")String appId,@RequestParam(name="receiver") String receiver,@RequestParam(name="text") String text,@RequestParam(name="files") String files){
        if(AuthorizedDevicesService.authorize(appId)){
            MessageEntity message = new MessageEntity();
            message.setTime(System.currentTimeMillis());
            message.setText(text);
            message.setReceiver(receiver);
            message.setContentId(Arrays.stream(files.split(";")).mapToLong(num->Long.parseLong(num)).toArray());
            message.setSender(AuthorizedDevicesService.getUser(appId).getLogin());
            messageRepo.save(message);
            return new ResponseEntity("true").toJson();
        }
        else{
            return new ResponseEntity("false").toJson();
        }
    }
    @PostMapping("/uploadFile")
    public String uploadFile(InputStream dataStream, @RequestParam(name="fileName") String fileName, @RequestParam(name="appId")String appId) throws IOException {
        if(AuthorizedDevicesService.authorize(appId)){
            FileEntity fileEntity = new FileEntity();
            fileEntity.setInputStream(dataStream);
            fileEntity.setFileName(fileName);
            try {
                return new ResponseEntity(String.valueOf(JDBCFileDB.saveFile(fileEntity))).toJson();
            } catch (SQLException e) {
                return "Failed";
            }
        }
        return "Failed";
    }
    @GetMapping("/getFile")
    public void getFile(@RequestParam(name="appId") String appId, @RequestParam(name= "fileId")long fileId, HttpServletResponse response) throws SQLException, IOException {
        if(AuthorizedDevicesService.authorize(appId)){
            int canRead;
            byte[] buffer = new byte[8*1024];
            FileEntity file = JDBCFileDB.getFileById(fileId);
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
                return JDBCFileDB.getNameOfFile(Long.parseLong(fileId));
            } catch (SQLException e) {
                return "";
            }
        }
        return "";
    }

}
