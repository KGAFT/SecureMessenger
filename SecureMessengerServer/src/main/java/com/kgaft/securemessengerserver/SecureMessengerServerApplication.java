package com.kgaft.securemessengerserver;

import com.kgaft.securemessengerserver.DataBase.DAO.FileDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.sql.SQLException;


@SpringBootApplication
public class SecureMessengerServerApplication {

    public static void main(String[] args) {
        try {
            FileDAO.executeFilesTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SpringApplication.run(SecureMessengerServerApplication.class, args);
    }

}
