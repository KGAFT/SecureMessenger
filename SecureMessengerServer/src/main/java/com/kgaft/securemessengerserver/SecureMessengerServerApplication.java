package com.kgaft.securemessengerserver;

import com.kgaft.securemessengerserver.DataBase.JDBCDB.JDBCFileDB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;


@SpringBootApplication
public class SecureMessengerServerApplication {

    public static void main(String[] args) {
        try {
            JDBCFileDB.executeFilesTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SpringApplication.run(SecureMessengerServerApplication.class, args);
    }

}
