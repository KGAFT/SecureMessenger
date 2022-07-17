package com.kgaft.securemessengerserver;

import com.kgaft.securemessengerserver.DataBase.JDBC.JDBCFileDB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;


@SpringBootApplication
public class SecureMessengerServerApplication {

    public static void main(String[] args) {
        System.setProperty("dataBaseUrl", "jdbc:postgresql://localhost:5432/securemessenger");
        System.setProperty("dataBaseLogin", "postgres");
        System.setProperty("dataBasePassword", "12345");
        try {
            JDBCFileDB.executeFilesTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SpringApplication.run(SecureMessengerServerApplication.class, args);
    }

}
