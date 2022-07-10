package com.kgaft.securemessengerserver.Controllers;

import com.google.gson.GsonBuilder;
import com.kgaft.securemessengerserver.DataBase.Entities.MessageEntity;
import com.kgaft.securemessengerserver.DataBase.Entities.ResponseEntity;

import com.kgaft.securemessengerserver.DataBase.Entities.UserEntity;
import com.kgaft.securemessengerserver.DataBase.Repositories.AuthorizedUsersRepo;
import com.kgaft.securemessengerserver.DataBase.Repositories.MessageRepo;
import com.kgaft.securemessengerserver.DataBase.Repositories.UserLoginRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
public class MessageController {
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private AuthorizedUsersRepo apps;
    @Autowired
    private UserLoginRepo users;
    private HashMap<String, String> waitingToJoin = new HashMap<>();
    @GetMapping("/startChat")
    public String startChat(@RequestParam(name="appId") String appId){
        if (authorizeByAppId(appId)) {
            try {
                UserEntity user = getUserByAppId(appId);
                String response = new ResponseEntity(waitingToJoin.get(user.getLogin())).toJson();
                waitingToJoin.remove(user.getLogin());
                return response;
            }catch (Exception e){
                return "Error";
            }
        }
        return "Error: cannot authorize";
    }
    @PostMapping("/joinChat")
    public String joinChat(@RequestParam(name = "appId")String appId, @RequestParam(name="receiver") String receiver){
        if(authorizeByAppId(appId)){
            waitingToJoin.put(receiver, getUserByAppId(appId).getLogin());
            return new ResponseEntity("true").toJson();
        }
        return new ResponseEntity("Error: cannot authorize").toJson();
    }
    @GetMapping("/getMessagesByTime")
    public String getCurrentMessages(@RequestParam(name="appId")String appId, @RequestParam(name="timeInMilliseconds") long time){
        if(authorizeByAppId(appId)){
            Iterable<MessageEntity> messages = messageRepo.findMessageByReceiverOrSender(getUserByAppId(appId).getLogin(), time);
            return new GsonBuilder().create().toJson(messages).toString();
        }
        return "Error";
    }
    @GetMapping("/getMessages")
    public String getAllMessages(@RequestParam(name="appId")String appId){
        if(authorizeByAppId(appId)){
            Iterable<MessageEntity> messages = messageRepo.findMessageByReceiverOrSender(getUserByAppId(appId).getLogin());
            return new GsonBuilder().create().toJson(messages).toString();
        }
        return "Error";
    }
    @PostMapping("/sendMessage")
    public String sendMessage(@RequestParam(name="appId")String appId,@RequestParam(name="receiver") String receiver,@RequestParam(name="text") String text,@RequestParam(name="files") String files){
        if(authorizeByAppId(appId)){
            MessageEntity message = new MessageEntity();
            message.setTime(System.currentTimeMillis());
            message.setText(text);
            message.setReceiver(receiver);
            message.setContentId(Arrays.stream(files.split(";")).mapToLong(num->Long.parseLong(num)).toArray());
            message.setSender(getUserByAppId(appId).getLogin());
            messageRepo.save(message);
            return new ResponseEntity("true").toJson();
        }
        else{
            return new ResponseEntity("false").toJson();
        }
    }
    private boolean authorizeByAppId(String appId){
        try{
            return apps.findById(Long.parseLong(appId)).get()!=null;
        }catch (Exception e){
            return false;
        }

    }
    private UserEntity getUserByAppId(String appId){
        return users.findById(apps.findById(Long.parseLong(appId)).get().getUserId()).get();
    }

}
