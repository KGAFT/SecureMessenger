package com.kgaft.securemessengerserver.Controllers;

import com.kgaft.securemessengerserver.DataBase.Entities.ResponseEntity;
import com.kgaft.securemessengerserver.DataBase.Entities.UserEntity;
import com.kgaft.securemessengerserver.DataBase.Repositories.UserLoginRepo;
import com.kgaft.securemessengerserver.Service.AuthorizedDevicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class UsersController {
    @Autowired
    private UserLoginRepo usersRepo;
    @GetMapping("/getUserName")
    public String getUserName(@RequestParam(name = "appId")String appId, @RequestParam(name="userLogin") String login){
        if(AuthorizedDevicesService.authorize(appId)){
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
}
