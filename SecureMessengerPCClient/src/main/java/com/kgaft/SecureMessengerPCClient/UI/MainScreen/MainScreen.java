package com.kgaft.SecureMessengerPCClient.UI.MainScreen;

import com.kgaft.SecureMessengerPCClient.Main;
import com.kgaft.SecureMessengerPCClient.UI.ScreenInterface;

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

    public MainScreen() {
        root = new JPanel();
        root.setLayout(new BorderLayout());
        setContentPane(root);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 100);
        initJFields();
        root.add(screenLabel, BorderLayout.NORTH);
        root.add(startNewChatButton, BorderLayout.NORTH);
        root.add(chatsScroller);


    }

    private void initJFields() {
        screenLabel = new JLabel("Secure messenger");
        startNewChatButton = new JButton("Start new chat");
        chatsContainer = new JPanel();
        screenLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        chatsContainer.setLayout(new GridLayout(0, 1));
        chatsScroller = new JScrollPane(chatsContainer);
        chatsScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatsScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        try {
            chatsContainer.add(ChatPreview.getInstanceForReceiverLogin("Bebra228", "/Users/danil/Downloads"));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
