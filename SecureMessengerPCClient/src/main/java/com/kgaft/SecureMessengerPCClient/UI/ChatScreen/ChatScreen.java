package com.kgaft.SecureMessengerPCClient.UI.ChatScreen;

import com.kgaft.SecureMessengerPCClient.BackEnd.AppProperties.AppProperties;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationManager.Entities.UserEntity;
import com.kgaft.SecureMessengerPCClient.BackEnd.Authorization.AuthorizationNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.Database.Database;
import com.kgaft.SecureMessengerPCClient.BackEnd.Database.SQLTableEntityI;
import com.kgaft.SecureMessengerPCClient.BackEnd.EncryptionKeys.EncryptionNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.Files.FilesNativeCalls;
import com.kgaft.SecureMessengerPCClient.BackEnd.MessageService.MessageReceiverManager.Entities.MessageEntity;
import com.kgaft.SecureMessengerPCClient.BackEnd.MessageService.MessageReceiverManager.MessageManager;
import com.kgaft.SecureMessengerPCClient.BackEnd.Utils.EncryptionUtil;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatScreen extends JFrame {
    private JTextField messageTextInput;
    private JButton sendButton;
    private JButton addFileButton;
    private JScrollPane chatScroller;
    private JLabel receiverName;
    private JLabel receiverLogin;
    private JLabel receiverIcon;
    private JPanel root;
    private JPanel messageContainer;

    private String receiverLoginString;

    private String receiverNameString;

    private BufferedImage receiverIconIO;
    private Thread messageAdderThread;

    /*
        Warning make refresh via resize window!
         */
    public ChatScreen(String receiverLogin) throws SQLException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.receiverLoginString = receiverLogin;
        setContentPane(root);
        initNativeFields();
        initJFields();
        setTitle("Chat with: " + receiverNameString);
        setSize(500, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initJFields() {
        receiverLogin.setText(receiverLoginString);
        receiverName.setText(receiverNameString);
        receiverIcon.setIcon(new ImageIcon(receiverIconIO));
        initSendButton();
        chatScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messageContainer = new JPanel(new GridLayout(0, 1));
        chatScroller.add(messageContainer);
        List<SQLTableEntityI> messages = new ArrayList<>();
        MessageEntity example = new MessageEntity();
        try {
            Database.selectQuery("SELECT * FROM " + example.getTableName(), messages, example);
            EncryptionNativeCalls keys = new EncryptionNativeCalls();
            messageContainer.add(Message.createForMessageEntity((MessageEntity) messages.get(0), keys.getEncryptionKeyForReceiver(receiverLoginString).getEncryptionKey()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSendButton() {
        sendButton.addActionListener(event -> {
            try {
                String serverBaseUrl = AppProperties.getServerBaseUrl();
                AuthorizationNativeCalls authorization = new AuthorizationNativeCalls(serverBaseUrl);
                UserEntity appUser = authorization.getCurrentAuthorizedUser();
                MessageEntity message = new MessageEntity();
                message.setSender(appUser.getLogin());
                message.setReceiver(receiverLoginString);
                message.setMessageText(messageTextInput.getText());
                message.setContentId(new long[]{0});
                EncryptionNativeCalls encryptionNativeCalls = new EncryptionNativeCalls();
                message = (MessageEntity) EncryptionUtil.encrypt(message, encryptionNativeCalls.getEncryptionKeyForReceiver(receiverLoginString).getEncryptionKey());
                MessageManager messageManager = new MessageManager(serverBaseUrl);
                messageManager.sendMessage(appUser.getAppId(), message);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    private void initNativeFields() throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        MessageManager userInfo = new MessageManager(AppProperties.getServerBaseUrl());
        AuthorizationNativeCalls authorization = new AuthorizationNativeCalls(AppProperties.getServerBaseUrl());
        FilesNativeCalls files = new FilesNativeCalls(AppProperties.getServerBaseUrl());
        UserEntity appUser = authorization.getCurrentAuthorizedUser();
        this.receiverNameString = userInfo.getUserName(appUser.getAppId(), receiverLoginString);
        String fileName = files.getFileName(appUser.getAppId(), "iconHolderLogin=" + receiverLoginString);
        files.downloadFile(fileName, System.getenv("TEMP"), "iconHolderLogin=" + receiverLoginString, appUser.getAppId());
        this.receiverIconIO = resizeImage(ImageIO.read(new File(System.getenv("temp") + "/" + fileName)), 50, 50);


    }

    private BufferedImage resizeImage(BufferedImage image, int targetWidth, int targetHeight) {
        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resized.createGraphics();

        graphics.drawImage(image, 0, 0, targetWidth, targetHeight, null);
        graphics.dispose();
        return resized;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        root = new JPanel();
        root.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        root.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        receiverName = new JLabel();
        receiverName.setText("Label");
        panel1.add(receiverName, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        receiverLogin = new JLabel();
        receiverLogin.setText("Label");
        panel1.add(receiverLogin, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        receiverIcon = new JLabel();
        receiverIcon.setEnabled(true);
        receiverIcon.setText("");
        panel1.add(receiverIcon, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chatScroller = new JScrollPane();
        root.add(chatScroller, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        root.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        messageTextInput = new JTextField();
        panel2.add(messageTextInput, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        addFileButton = new JButton();
        addFileButton.setText("Add file");
        panel2.add(addFileButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sendButton = new JButton();
        sendButton.setText("Send");
        panel2.add(sendButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

}
