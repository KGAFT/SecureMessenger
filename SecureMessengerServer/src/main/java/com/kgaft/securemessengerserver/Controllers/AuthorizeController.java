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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class AuthorizeController {
    @Autowired
    private UserLoginRepo repo;

    @PostMapping("/authorizeClient")
    public String authorizeEntity(@RequestParam(name="login", required = true) String login, @RequestParam(name="password", required = true) String password, @RequestParam(name="appId", required = true) String appId){
        ArrayList<UserEntity> findedUsers = new ArrayList<>();
        repo.findByLogin(login).forEach(element->{
            if(element.getLogin().equals(login) & element.getPassword().equals(password)){
                findedUsers.add(element);
            }
        });
        if(findedUsers.size()==1){
            AuthorizedDevicesService.addDevice(appId, findedUsers.get(0));
            return "Success!";
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
    public String register(@RequestParam(name="login", required = true) String login, @RequestParam(name="password", required = true) String password, @RequestParam(name="appId", required = true) String appId, @RequestParam(name = "name")String name){
        ArrayList<UserEntity> results = new ArrayList<>();
        repo.findByLogin(login).forEach(element->results.add(element));
        if(results.size()>0){
            return "Cannot create user with same login";
        }
        else{
            repo.save(new UserEntity(0, name, login, password));
            return "Success!";
        }
    }
}
