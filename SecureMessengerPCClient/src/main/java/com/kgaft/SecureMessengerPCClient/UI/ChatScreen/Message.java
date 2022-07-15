package com.kgaft.SecureMessengerPCClient.UI.ChatScreen;

import com.kgaft.SecureMessengerPCClient.BackEnd.AppProperties.AppProperties;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationManager.Entities.UserEntity;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.FilesEncrypted.FilesEncryptedNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.MessageService.MessageReceiverManager.Entities.MessageEntity;
import com.kgaft.SecureMessengerPCClient.BackEnd.Utils.IOUtil;
import com.kgaft.SecureMessengerPCClient.Main;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Message extends JButton {
    private JPanel root;
    private JLabel senderLogin;
    private JLabel messageText;
    private JLabel timeOfSend;
    private JScrollPane contentsScroller;
    private JPanel contentContainer;
    public static Message createForMessageEntity(MessageEntity message, byte[] encryptionKey){
        if(message.getContentId()[0]!=0){
            return new Message(message.getSender(), message.getMessageText(), message.getContentId(), encryptionKey, message.getTime());
        }
        return new Message(message.getSender(), message.getMessageText(), message.getTime());
    }
    private Message(String senderLogin, String messageText, long[] contentsId, byte[] encryptionKey, long timeOfSend){
        initJFields();
        this.senderLogin.setText(senderLogin);
        this.messageText.setText(messageText);
        this.timeOfSend.setText(new Date(timeOfSend).toString());
        prepareContent(contentsId, encryptionKey).forEach((contentId, contentPreview)->{
            JLabel contentImage = new JLabel(new ImageIcon(contentPreview));
            contentImage.addMouseListener(createDownloadMenuForContentId(contentId, encryptionKey));
            contentContainer.add(contentImage);
        });
    }

    private Message(String senderLogin, String messageText, long timeOfSend){
        initJFields();
        this.senderLogin.setText(senderLogin);
        this.messageText.setText(messageText);
        this.timeOfSend.setText(new Date(timeOfSend).toString());
    }
    private void initJFields(){
        senderLogin = new JLabel();
        messageText = new JLabel();
        contentContainer = new JPanel(new GridLayout(0, 1));
        contentsScroller = new JScrollPane(contentContainer);
        timeOfSend = new JLabel();
        contentsScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentsScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        senderLogin.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        root = new JPanel(new GridLayout(0, 1));
        add(root);
        root.add(senderLogin);
        root.add(messageText);
        root.add(timeOfSend);
        root.add(contentsScroller);
    }
    private MouseListener createDownloadMenuForContentId(long contentId, byte[] encryptionKey){
        MouseListener onClick = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                JFileChooser directoryChooser = new JFileChooser();
                directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int chooseResult = directoryChooser.showOpenDialog(null);
                if(chooseResult == directoryChooser.APPROVE_OPTION){
                    String destinationFolder = directoryChooser.getSelectedFile().getAbsolutePath();
                    try {
                        String serverBaseUrl = AppProperties.getServerBaseUrl();
                        FilesEncryptedNativeCalls files = new FilesEncryptedNativeCalls(serverBaseUrl);
                        AuthorizationNativeCalls authorizationNativeCalls = new AuthorizationNativeCalls(serverBaseUrl);
                        files.downloadAndDecryptFile(authorizationNativeCalls.getCurrentAuthorizedUser().getAppId(), contentId, encryptionKey, destinationFolder);
                    } catch (Exception ignored) {

                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
        return onClick;
    }
    private HashMap<Long, BufferedImage> prepareContent(long[] content, byte[] encryptionKey){
        HashMap<Long, BufferedImage> contentPreviews = new HashMap<>();
        try {
            String serverBaseUrl = AppProperties.getServerBaseUrl();
            FilesEncryptedNativeCalls files = new FilesEncryptedNativeCalls(serverBaseUrl);
            UserEntity appUser = new AuthorizationNativeCalls(serverBaseUrl).getCurrentAuthorizedUser();
            for (long contentId : content) {
                String contentName = files.getFileName(appUser.getAppId(), contentId);
                String fileExtension = IOUtil.getFileExtension(contentName);
                if((fileExtension.equals("jpg")) || (fileExtension.equals("JPG")) || (fileExtension.equals("jpeg"))){
                    contentPreviews.put(contentId, downloadAndResizeImage(contentId, encryptionKey));
                }
                else{
                    contentPreviews.put(contentId, resizeImage(ImageIO.read(Main.class.getClassLoader().getResource("/resources/fileIcon")), 100, 100));
                }
            }
        } catch (Exception e) {

        }
        return contentPreviews;
    }
    private BufferedImage downloadAndResizeImage(long contentId, byte[] encryptionKey){
        try {
            String serverBaseUrl = AppProperties.getServerBaseUrl();
            FilesEncryptedNativeCalls files = new FilesEncryptedNativeCalls(serverBaseUrl);
            AuthorizationNativeCalls authorization = new AuthorizationNativeCalls(serverBaseUrl);
            File content = files.downloadAndDecryptFile(authorization.getCurrentAuthorizedUser().getAppId(), contentId, encryptionKey, System.getenv("TEMP"));
            return resizeImage(ImageIO.read(content), 100, 100);
        } catch (Exception e) {
            return null;
        }
    }
    private BufferedImage resizeImage(BufferedImage image, int targetWidth, int targetHeight){
        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resized.createGraphics();

        graphics.drawImage(image, 0, 0, targetWidth, targetHeight, null);
        graphics.dispose();
        return resized;
    }
}
