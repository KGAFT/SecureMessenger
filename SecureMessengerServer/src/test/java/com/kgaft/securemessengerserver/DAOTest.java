package com.kgaft.securemessengerserver;

import com.kgaft.securemessengerserver.DataBase.DAO.FileDAO;

import com.kgaft.securemessengerserver.DataBase.Entities.FileEntity;


import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class DAOTest {
    public static void main(String[] args) throws IOException, SQLException {
        FileInputStream inputStream = new FileInputStream(new File("/home/daniil/Изображения/owl1.jpg"));
        FileEntity file = new FileEntity();
        file.setFileName("owl1.jpg");
        file.setFileInput(inputStream);
        FileDAO.saveFile(file);
    }
}
