package com.kgaft.securemessengerserver.DataBase.DAO;

import com.kgaft.securemessengerserver.DataBase.Entities.FileEntity;

import java.sql.*;
import java.util.Random;

public class FileDAO {
    private static final String url = "jdbc:postgresql://localhost:5432/securemessenger";
    private static final String login = "KGAFTX";

    private static final String password = "123456";

    private static final String TABLE_NAME = "usersfiledata";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, login, password);
    }

    public static long saveFile(FileEntity file) throws SQLException {
        PreparedStatement pstmt = getConnection().prepareStatement("INSERT INTO "+TABLE_NAME+" (fileid, filecontent, filename) VALUES(?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
        Random random = new Random();
        long generatedId = random.nextLong();
        while(getNameOfFile(generatedId).length()>0) generatedId = random.nextLong();
        pstmt.setLong(1, generatedId);
        pstmt.setBinaryStream(2, file.getFileInput());
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
        PreparedStatement pstmt = getConnection().prepareStatement("SELECT filename FROM "+TABLE_NAME+" WHERE fileid=?");
        pstmt.setLong(1, id);
        ResultSet rs = pstmt.executeQuery();
        try{
            rs.next();
            return rs.getString("filename");
        }catch (Exception e){
            return "";
        }
    }
}
