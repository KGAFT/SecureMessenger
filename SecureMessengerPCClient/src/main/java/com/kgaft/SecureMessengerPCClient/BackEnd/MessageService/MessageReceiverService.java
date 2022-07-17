package com.kgaft.SecureMessengerPCClient.BackEnd.MessageService;

import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationManager.Entities.UserEntity;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.Database.Database;
import com.kgaft.SecureMessengerPCClient.BackEnd.EncryptionKeys.EncryptionNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.MessageService.MessageReceiverManager.Entities.MessageEntity;
import com.kgaft.SecureMessengerPCClient.BackEnd.MessageService.MessageReceiverManager.MessageManager;
import com.kgaft.SecureMessengerPCClient.BackEnd.ServiceInterface;
import com.kgaft.SecureMessengerPCClient.BackEnd.Utils.EncryptionUtil;

import java.io.IOException;

public class MessageReceiverService implements ServiceInterface {
    private Thread thread;
    private MessageManager messageManager;
    private AuthorizationNativeCalls authorizationNativeCalls;
    private EncryptionNativeCalls keys;
    private long lastGettingMessagesTime;
    public MessageReceiverService(MessageManager messageManager, AuthorizationNativeCalls authorizationNativeCalls) {

        this.messageManager = messageManager;
        this.authorizationNativeCalls = authorizationNativeCalls;
        keys = new EncryptionNativeCalls();

    }

    private void saveAllMessages(long appId) throws IOException {
        messageManager.getAllMessages(appId).forEach(message -> {
            try {
                String encryptionKeyFor = encryptionKeyFor(message.getSender(), message.getReceiver());
                if (encryptionKeyFor != null) {
                    message = (MessageEntity) EncryptionUtil.decrypt(message, keys.getEncryptionKeyForReceiver(encryptionKeyFor).getEncryptionKey());
                    Database.insertEntity(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void saveMessagesByTime(long appId, long time) throws IOException {
        messageManager.getLastMessagesByTime(appId, time).forEach(message -> {
            try {
                String encryptionKeyFor = encryptionKeyFor(message.getSender(), message.getReceiver());
                if (encryptionKeyFor != null) {
                    message = (MessageEntity) EncryptionUtil.decrypt(message, keys.getEncryptionKeyForReceiver(encryptionKeyFor).getEncryptionKey());
                    Database.insertEntity(message);
                }
            } catch (Exception e) {

            }
        });
    }
        private String encryptionKeyFor (String sender, String receiver){
            try {
                return keys.isKeyExists(receiver) ? receiver : keys.isKeyExists(sender) ? sender : null;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public void start () {
            thread = new Thread(() -> {
                while (true) {
                    try {
                        if (authorizationNativeCalls.checkConnection()) {
                            UserEntity user = authorizationNativeCalls.getCurrentAuthorizedUser();
                            if (lastGettingMessagesTime == 0) {
                                saveAllMessages(user.getAppId());
                            } else {
                                saveMessagesByTime(user.getAppId(), lastGettingMessagesTime - 1000);
                            }
                            lastGettingMessagesTime = System.currentTimeMillis();
                        }
                    } catch (Exception e) {

                    }
                }
            });
            thread.start();
        }

        @Override
        public void stop () {
            thread.stop();
        }

        @Override
        public void pause () {
            thread.stop();
        }

        @Override
        public void resume () {
            thread.start();
        }
    }
