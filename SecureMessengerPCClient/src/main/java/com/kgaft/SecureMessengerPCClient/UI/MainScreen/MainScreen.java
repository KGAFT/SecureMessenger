package com.kgaft.SecureMessengerPCClient.UI.MainScreen;

import com.kgaft.SecureMessengerPCClient.BackEnd.EncryptionKeys.EncryptionNativeCalls;
import com.kgaft.SecureMessengerPCClient.Main;
import com.kgaft.SecureMessengerPCClient.UI.ScreenInterface;
import com.kgaft.SecureMessengerPCClient.UI.ScreenManager;
import com.kgaft.SecureMessengerPCClient.UI.StartChatScreen.StartChatScreen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

public class MainScreen extends JFrame implements ScreenInterface {
    private JPanel root;
    private JLabel screenLabel;
    private JButton startNewChatButton;
    private JPanel chatsContainer;
    private JScrollPane chatsScroller;
    private JButton refreshButton;
    public MainScreen() {
        root = new JPanel();
        root.setLayout(new BorderLayout());
        setContentPane(root);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Main Screen");
        setSize(500, 500);
        init();

    }
    private void init(){
        initJFields();
        root.add(screenLabel, BorderLayout.NORTH);
        root.add(startNewChatButton, BorderLayout.NORTH);
        root.add(refreshButton, BorderLayout.SOUTH);
        root.add(chatsScroller);
        showAvailableChats();
    }
    private void initJFields() {
        screenLabel = new JLabel("Secure messenger");
        startNewChatButton = new JButton("Start new chat");
        chatsContainer = new JPanel();
        screenLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        chatsContainer.setLayout(new GridLayout(0, 1));
        chatsScroller = new JScrollPane(chatsContainer);
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(event->{
            root.removeAll();
            init();
            repaintUI();
        });
        chatsScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatsScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        startNewChatButton.addActionListener(event->{
            ScreenManager.getAllScreens().forEach(screen -> {
                if(screen.getClass() == StartChatScreen.class){
                    screen.showScreen();
                }
            });
        });
    }
    private void repaintUI(){
        Dimension size = getSize();
        size.height+=1;
        size.width-=1;
        setSize(size);
    }
    private void showAvailableChats(){
        EncryptionNativeCalls keys = new EncryptionNativeCalls();
        try{
            keys.getAvailableEncryptionKeysForReceivers().forEach(receiver->{
                try {
                    chatsContainer.add(ChatPreview.getInstanceForReceiverLogin(receiver, System.getenv("TEMP")));
                } catch (Exception e) {
                }
            });
        }catch (Exception e){

        }
    }
    @Override
    public void showScreen() {
        setVisible(true);
    }

    @Override
    public void hideScreen() {
        setVisible(false);
    }

    @Override
    public void closeScreen() {
        hideScreen();
        dispose();
    }
}
