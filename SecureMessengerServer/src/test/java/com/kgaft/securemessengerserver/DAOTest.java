package com.kgaft.securemessengerserver;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class DAOTest {
    public static void main(String[] args) throws IOException, SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        System.out.println(CryptoUtil.wrapString("helloworld!", "llop"));
    }
}
