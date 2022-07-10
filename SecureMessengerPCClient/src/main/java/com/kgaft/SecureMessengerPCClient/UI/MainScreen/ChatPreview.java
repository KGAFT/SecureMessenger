package com.kgaft.SecureMessengerPCClient.UI.MainScreen;

import com.kgaft.SecureMessengerPCClient.BackEnd.AppProperties.AppProperties;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationManager.Entities.UserEntity;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.Files.FilesNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.MessageService.MessageReceiverManager.MessageManager;
import com.kgaft.SecureMessengerPCClient.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class ChatPreview extends JButton
{
    public static ChatPreview getInstanceForReceiverLogin(String receiverLogin, String tempDir) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        String serverBaseUrl = AppProperties.getServerBaseUrl();
        FilesNativeCalls files = new FilesNativeCalls(serverBaseUrl);
        UserEntity currentUser = new AuthorizationNativeCalls(serverBaseUrl).getCurrentAuthorizedUser();
        MessageManager otherUserInfo = new MessageManager(serverBaseUrl);
        BufferedImage image = null;
        try{
            String fileName = files.getFileName(currentUser.getAppId(), "iconHolderLogin="+receiverLogin);
            File tempFile = new File(tempDir+"/"+fileName);
            files.downloadFile(fileName, tempDir, "iconHolderLogin="+receiverLogin, currentUser.getAppId());
            image = ImageIO.read(tempFile);
        }catch (IOException e){
            image = ImageIO.read(Main.class.getClassLoader().getResource("receiverDefault.jpg"));
        }
        String chatName = otherUserInfo.getUserName(currentUser.getAppId(), receiverLogin);
        ChatPreview chat = new ChatPreview(chatName, receiverLogin, image);
        return chat;

    }
    private JLabel chatName;
    private JLabel chatInfo;
    private JLabel chatImage;
    private JPanel content;
    private ChatPreview(String chatName, String chatInfo, BufferedImage chatImage){
        initFields();
        setPreferredSize(new Dimension(500, 100));
        this.chatName.setText(chatName);
        this.chatInfo.setText(chatInfo);
        this.chatImage.setIcon(new ImageIcon(resizeImage(chatImage, 50, 50)));
    }
    private void initFields(){
        content = new JPanel();
        chatName = new JLabel();
        chatInfo = new JLabel();
        chatImage = new JLabel();
        content.setLayout(new BorderLayout());
        add(content);
        content.add(chatImage, BorderLayout.WEST);
        content.add(chatName, BorderLayout.CENTER);
        content.add(chatInfo, BorderLayout.SOUTH);
    }
    private BufferedImage resizeImage(BufferedImage image, int targetWidth, int targetHeight){
        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resized.createGraphics();

        graphics.drawImage(image, 0, 0, targetWidth, targetHeight, null);
        graphics.dispose();
        return resized;
    }
}
