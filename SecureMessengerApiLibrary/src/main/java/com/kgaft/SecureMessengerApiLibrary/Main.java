package com.kgaft.SecureMessengerApiLibrary;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SecureMessenger messenger = new SecureMessenger("http://localhost:45632");
        messenger.login("Bebra228", "12345");
       messenger.getAllMessages().forEach(element->{
           System.out.println(element.getMessagetext());
       });
    }
}
