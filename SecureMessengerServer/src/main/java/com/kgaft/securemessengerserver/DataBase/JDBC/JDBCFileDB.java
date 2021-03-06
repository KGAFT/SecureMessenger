package com.kgaft.securemessengerserver.DataBase.JDBC;

import com.kgaft.securemessengerserver.DataBase.Entities.FileEntity;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.util.Random;

/**
 * @author KGAFT
 * @version 1.0
 * This class helps to save files into postgresql
 */

public class JDBCFileDB {

    private static final String url;

    private static final String login;


    private static final String password;

    private static final String TABLE_NAME = "usersfiledata";
    static {
        url = System.getProperty("dataBaseUrl");
        login = System.getProperty("dataBaseLogin");
        password = System.getProperty("dataBasePassword");
    }

    public static void executeFilesTable() throws SQLException {
        getConnection().createStatement().execute("CREATE TABLE "+TABLE_NAME+"(" +
                "fileid BIGINT," +
                "filecontent bytea," +
                "filename TEXT);");
    }
    private static Connection getConnection() throws SQLException {


        return DriverManager.getConnection(url, login, password);
    }

    public static long saveFile(FileEntity file) throws SQLException {
        PreparedStatement pstmt = getConnection().prepareStatement("INSERT INTO "+TABLE_NAME+" (fileid, filecontent, filename) VALUES(?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
        Random random = new Random();
        long generatedId = random.nextLong();
        while(getNameOfFile(generatedId).length()>0) generatedId = random.nextLong();
        pstmt.setLong(1, generatedId);
        pstmt.setBinaryStream(2, file.getInputStream());
        pstmt.setString(3, file.getFileName());
        pstmt.execute();
        return generatedId;
    }
    public static FileEntity getFileById(long id) throws SQLException {
        PreparedStatement pstmt = getConnection().prepareStatement("SELECT * FROM "+TABLE_NAME+" WHERE fileid = ?;");
        pstmt.setLong(1, id);
        ResultSet results = pstmt.executeQuery();
        FileEntity file = new FileEntity();
        results.next();
        file.setFileId(results.getLong("fileid"));
        file.setInputStream(results.getBinaryStream("filecontent"));
        file.setFileName(results.getString("filename"));
        return file;
    }

    public static void deleteFileById(long id) throws SQLException {
        PreparedStatement pstmt = getConnection().prepareStatement("DELETE FROM "+TABLE_NAME+" WHERE fileid = ?;", PreparedStatement.RETURN_GENERATED_KEYS);
        pstmt.setLong(1, id);
        pstmt.execute();
    }
    public static String getNameOfFile(long id) throws SQLException {
        ResultSet rs = getConnection().createStatement().executeQuery("SELECT filename FROM "+TABLE_NAME+" WHERE fileid = "+id+";");
        try{
            rs.next();
            return rs.getString("filename");
        }catch (Exception e){
            return "";
        }
    }
}
