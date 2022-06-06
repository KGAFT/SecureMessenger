package com.kgaft.securemessengerserver.Controllers;
import com.kgaft.securemessengerserver.DataBase.Entities.UserEntity;
import com.kgaft.securemessengerserver.DataBase.Repositories.UserLoginRepo;
import com.kgaft.securemessengerserver.Service.AuthorizedDevicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class AuthorizeController {
    @Autowired
    private UserLoginRepo repo;

    @GetMapping("/authorizeClient")
    public String authorizeEntity(@RequestParam(name="login", required = true) String login, @RequestParam(name="password", required = true) String password){
        ArrayList<UserEntity> findedUsers = new ArrayList<>();
        repo.findByLogin(login).forEach(element->{
            if(element.getLogin().equals(login) & element.getPassword().equals(password)){
                findedUsers.add(element);
            }
        });
        if(findedUsers.size()==1){
            Random random = new Random();
            long generatedAppId = random.nextLong();
            while(AuthorizedDevicesService.authorize(String.valueOf(generatedAppId))) generatedAppId = random.nextLong();
            AuthorizedDevicesService.addDevice(String.valueOf(generatedAppId), findedUsers.get(0));
            String response = findedUsers.get(0).toJson();
            response = response.split(",")[0]+","+response.split(",")[1]+","+response.split(",")[2]+","+Character.toString(34)+"appId"+Character.toString(34)+":"+Character.toString(34)+String.valueOf(generatedAppId)+Character.toString(34)+"}";
            return response;
        }
        else{
            return "Failed";
        }

    }
    @PostMapping("/unAuthorizeClient")
    public String unAuthorize(@RequestParam(name="appId", required = true)String appId){
        AuthorizedDevicesService.unAuthorize(appId);
        return "Success";
    }
    @PostMapping("/register")
    public String register(@RequestParam(name="login", required = true) String login, @RequestParam(name="password", required = true) String password, @RequestParam(name = "name")String name){
        ArrayList<UserEntity> results = new ArrayList<>();
        repo.findByLogin(login).forEach(element->results.add(element));
        if(results.size()>0){
            return "{"+Character.toString(34)+"response"+Character.toString(34)+":"+Character.toString(34)+"Cannot create user with same login!"+Character.toString(34)+"}";
        }
        else{
            repo.save(new UserEntity(0, name, login, password));
            return "{"+Character.toString(34)+"response"+Character.toString(34)+":"+Character.toString(34)+"Success!"+Character.toString(34)+"}";
        }
    }
    @GetMapping("/checkConnection")
    public String checkConnection(@RequestParam(name="appId")String appId){
       return  "{"+Character.toString(34)+"response"+Character.toString(34)+":"+Character.toString(34)+AuthorizedDevicesService.authorize(appId)+Character.toString(34)+"}";
    }
}
