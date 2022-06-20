package com.kgaft.securemessengerserver.Controllers;

import com.kgaft.securemessengerserver.DataBase.Entities.ResponseEntity;
import com.kgaft.securemessengerserver.DataBase.Entities.UserEntity;
import com.kgaft.securemessengerserver.DataBase.Repositories.AuthorizedUsersRepo;
import com.kgaft.securemessengerserver.DataBase.Repositories.UserLoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class UsersController {
    @Autowired
    private UserLoginRepo usersRepo;
    @Autowired
    private AuthorizedUsersRepo apps;

    /**
     *
     * @param appId
     * @param login
     * @return username, of param login, need to get info about receiver
     */
    @GetMapping("/getUserName")
    public String getUserName(@RequestParam(name = "appId")String appId, @RequestParam(name="userLogin") String login){
        if(authorizeByAppId(appId)){
            ArrayList<UserEntity> users = new ArrayList<>();
            usersRepo.findByLogin(login).forEach(element->{
                users.add(element);
            });
            if(users.size()>0){
                return new ResponseEntity(users.get(0).getName()).toJson();
            }
            else{
                return "Cannot find user";
            }
        }
        return "Cannot authorize";
    }
    private boolean authorizeByAppId(String appId){
        try{
            return apps.findById(Long.parseLong(appId)).get()!=null;
        }catch (Exception e){
            return false;
        }

    }
}
