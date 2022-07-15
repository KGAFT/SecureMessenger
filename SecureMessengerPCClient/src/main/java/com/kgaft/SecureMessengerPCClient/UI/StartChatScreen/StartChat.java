package com.kgaft.SecureMessengerPCClient.UI.StartChatScreen;

import com.kgaft.SecureMessengerPCClient.BackEnd.AppProperties.AppProperties;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationManager.Entities.UserEntity;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.EncryptionKeys.EncryptionNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.EncryptionKeys.Entities.EncryptionKeyEntity;
import com.kgaft.SecureMessengerPCClient.BackEnd.MessageService.MessageReceiverManager.MessageManager;




public class StartChat {
    private static Thread waitingThread;
    private static WaitTillJoinRunnable threadLogic;
    public static void waitTillJoin(byte[] keyToSave) {
        try {
            threadLogic.run=false;
            waitingThread.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
        threadLogic = new WaitTillJoinRunnable();
        threadLogic.setKeyToSave(keyToSave);
        waitingThread = new Thread(threadLogic);
        waitingThread.start();
    }

    public static boolean joinChat(String receiver, byte[] encryptionKey) {
        try {
            AuthorizationNativeCalls authorization = new AuthorizationNativeCalls(AppProperties.getServerBaseUrl());
            MessageManager joinChatManager = new MessageManager(AppProperties.getServerBaseUrl());
            UserEntity appUser = authorization.getCurrentAuthorizedUser();
            EncryptionNativeCalls keys = new EncryptionNativeCalls();
            if (joinChatManager.joinChat(appUser.getAppId(), receiver)) {
                keys.insertEncryptionKey(new EncryptionKeyEntity(encryptionKey, receiver));
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
