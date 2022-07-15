package com.kgaft.SecureMessengerPCClient.UI.StartChatScreen;

import com.kgaft.SecureMessengerPCClient.BackEnd.AppProperties.AppProperties;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationManager.Entities.UserEntity;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.EncryptionKeys.EncryptionNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.EncryptionKeys.Entities.EncryptionKeyEntity;
import com.kgaft.SecureMessengerPCClient.BackEnd.MessageService.MessageReceiverManager.MessageManager;

public class WaitTillJoinRunnable implements Runnable {
    volatile boolean run = true;
    private byte[] keyToSave;

    public byte[] getKeyToSave() {
        return keyToSave;
    }

    public void setKeyToSave(byte[] keyToSave) {
        this.keyToSave = keyToSave;
    }

    @Override
    public void run() {
        try {
            EncryptionNativeCalls encryptionKeys = new EncryptionNativeCalls();
            MessageManager messages = new MessageManager(AppProperties.getServerBaseUrl());
            AuthorizationNativeCalls authorization = new AuthorizationNativeCalls(AppProperties.getServerBaseUrl());
            UserEntity appUser = authorization.getCurrentAuthorizedUser();
            String response = "Error";
            while (run) {
                try {
                    if(!response.equals("Error")){
                        break;
                    }
                    response = messages.startChat(appUser.getAppId());
                } catch (Exception e) {
                }
            }
            encryptionKeys.insertEncryptionKey(new EncryptionKeyEntity(keyToSave, response));
        }catch (Exception e){

        }
    }
}
