package com.kgaft.securemessengerserver.Controllers;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kgaft.securemessengerserver.DataBase.Entities.AuthorizedUserEntity;
import com.kgaft.securemessengerserver.DataBase.Entities.ResponseEntity;
import com.kgaft.securemessengerserver.DataBase.Entities.UserEntity;
import com.kgaft.securemessengerserver.DataBase.Repositories.AuthorizedUsersRepo;
import com.kgaft.securemessengerserver.DataBase.Repositories.UserLoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;

@RestController
public class AuthorizeController {
    @Autowired
    private UserLoginRepo users;
    @Autowired
    private AuthorizedUsersRepo apps;
    @GetMapping("/authorizeClient")
    public String authorizeEntity(@RequestParam(name="login", required = true) String login, @RequestParam(name="password", required = true) String password){
        ArrayList<UserEntity> findedUsers = new ArrayList<>();
        users.findByLogin(login).forEach(element->{
            if(element.getLogin().equals(login) & element.getPassword().equals(password)){
                findedUsers.add(element);
            }
        });
        if(findedUsers.size()==1){
            long appId = insertUserApp(findedUsers.get(0));
            JsonObject userResponse = new Gson().toJsonTree(findedUsers.get(0)).getAsJsonObject();
            userResponse.remove("password");
            userResponse.addProperty("appId", appId);
            return userResponse.toString();
        }
        else{
            return "Cannot find user, with this login";
        }
    }
    @PostMapping("/unAuthorizeClient")
    public String unAuthorize(@RequestParam(name="appId", required = true)String appId){
        apps.deleteById(Long.parseLong(appId));
        return new ResponseEntity("Success").toJson();
    }
    @PostMapping("/register")
    public String register(@RequestParam(name="login", required = true) String login, @RequestParam(name="password", required = true) String password, @RequestParam(name = "name")String name){
        ArrayList<UserEntity> results = new ArrayList<>();
        users.findByLogin(login).forEach(element->results.add(element));
        if(results.size()>0){
            return new ResponseEntity("Cannot create user with same login!").toJson();

        }
        else{
            users.save(new UserEntity(0, name, login, password));
            return new ResponseEntity("Success!").toJson();
        }
    }
    @GetMapping("/checkConnection")
    public String checkConnection(@RequestParam(name="appId")String appId){
        return new ResponseEntity(String.valueOf(authorizeByAppId(appId))).toJson();
    }

    private long insertUserApp(UserEntity user){
        try{
            apps.deleteByUserId(user.getUserId());
        }catch (Exception e){
            e.printStackTrace();
        }
        AuthorizedUserEntity userAuthorized = new AuthorizedUserEntity();
        userAuthorized.setUserId(user.getUserId());
        apps.save(userAuthorized);
        return apps.getUserByUserId(user.getUserId()).get().getAppId();
    }
    private boolean authorizeByAppId(String appId){
        try{
            return apps.findById(Long.parseLong(appId)).get()!=null;
        }catch (Exception e){
            return false;
        }

    }
}
