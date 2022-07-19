package com.kgaft.securemessengerappandroid.Activities.MainActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kgaft.securemessengerappandroid.Activities.AuthorizeActivity.AuthorizeActivity;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppPropertiesTable;
import com.kgaft.securemessengerappandroid.Database.AppPropertiesTable.AppProperty;
import com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable.EncryptionKey;
import com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable.EncryptionKeysTable;
import com.kgaft.securemessengerappandroid.Database.MessagesTable.MessageEntity;
import com.kgaft.securemessengerappandroid.Database.MessagesTable.MessagesTable;
import com.kgaft.securemessengerappandroid.Files.FilesNativeCalls;
import com.kgaft.securemessengerappandroid.Files.IOUtil;
import com.kgaft.securemessengerappandroid.Network.SecureMessenger;
import com.kgaft.securemessengerappandroid.R;
import com.kgaft.securemessengerappandroid.Services.MessageService;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


@RequiresApi(api = Build.VERSION_CODES.N)
public class SettingsFragment extends Fragment {
    private ImageButton changeIconButton;
    private TextView userName;
    private Button clearCacheButton;
    private Button exportEncryptionKeysButton;
    private Button importEncryptionKeysButton;
    private Button exitButton;
    private AppProperty currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeIconButton = view.findViewById(R.id.userIcon);
        userName = view.findViewById(R.id.userName);
        clearCacheButton = view.findViewById(R.id.clearCacheButton);
        exportEncryptionKeysButton = view.findViewById(R.id.exportEncryptionKeysButton);
        importEncryptionKeysButton = view.findViewById(R.id.importEncryptionKeys);
        exitButton = view.findViewById(R.id.exitButton);
        try {
            currentUser = (AppProperty) new AppPropertiesTable(getContext(), null, AppProperty.class).getProperties();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        startInfoThreads();
        initButtons();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initButtons() {
        clearCacheButton.setOnClickListener(event -> {
            new Thread(() -> {
                for (File file : getActivity().getCacheDir().listFiles()) {
                    file.delete();
                }
                getActivity().runOnUiThread(() -> startActivity(new Intent(getContext(), MainActivity.class)));
            }).start();
        });
        exportEncryptionKeysButton.setOnClickListener(event -> {
            new Thread(() -> {
                String documentsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
                EncryptionKeysTable keys = new EncryptionKeysTable(getContext(), null, EncryptionKey.class);
                try {
                    File file = IOUtil.transferKeysToDirectory(keys.getAllKeys(), documentsPath);
                    getActivity().runOnUiThread(() -> {
                        Toast toast = Toast.makeText(getContext(), "Keys saved to: " + file.getAbsolutePath(), Toast.LENGTH_LONG);
                        toast.show();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        });
        importEncryptionKeysButton.setOnClickListener(event -> {
            getKeys.launch("*/*");
        });
        changeIconButton.setOnClickListener(event->getImage.launch("image/*"));
        exitButton.setOnClickListener(event->{
            prepareDeleteAlertDialog().show();
        });

    }
    private AlertDialog prepareDeleteAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure, do you wanna exit, this action will wipe all data of your app");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            getActivity().stopService(new Intent(getContext(), MessageService.class));
            AppPropertiesTable appPropertiesTable = new AppPropertiesTable(getContext(), null, AppProperty.class);
            EncryptionKeysTable encryptionKeysTable = new EncryptionKeysTable(getContext(), null, EncryptionKey.class);
            MessagesTable messagesTable = new MessagesTable(getContext(), null, MessageEntity.class);
            appPropertiesTable.deleteAppProperties();
            encryptionKeysTable.deleteAllKeys();
            messagesTable.deleteAll();
            for (File file : getActivity().getCacheDir().listFiles()) {
                file.delete();
            }
            startActivity(new Intent(getContext(), AuthorizeActivity.class));
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {

        });
        return builder.create();
    }
    private ActivityResultLauncher getImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result->{
        new Thread(()->{
            try{
                File icon = IOUtil.transferUriToCacheDir(result, getActivity().getContentResolver(), getActivity().getCacheDir().getAbsolutePath());
                FilesNativeCalls files = new FilesNativeCalls(currentUser.getServerBaseUrl());
                files.uploadFile(currentUser.getAppId(), icon);
                getActivity().runOnUiThread(()->{
                    startActivity(new Intent(getContext(), MainActivity.class));
                });
            }catch (Exception e){

            }

        }).start();
    });
    private ActivityResultLauncher getKeys = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
        new Thread(() -> {
            File resolvedKeys = IOUtil.transferUriToCacheDir(result, getActivity().getContentResolver(), getActivity().getCacheDir().getAbsolutePath());
            EncryptionKeysTable keys = new EncryptionKeysTable(getContext(), null, EncryptionKey.class);
            try {
                AtomicInteger insertedKeys = new AtomicInteger(0);
                IOUtil.readKeysFromFile(resolvedKeys).forEach(element->{
                    keys.insertKey(element);
                    insertedKeys.getAndDecrement();
                });
                getActivity().runOnUiThread(()->{
                    Toast toast = Toast.makeText(getContext(), "Saved "+insertedKeys.get()+" keys", Toast.LENGTH_SHORT);
                    toast.show();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    });

    private void startInfoThreads() {
        Thread userInfoThread = new Thread(() -> {
            FilesNativeCalls files = new FilesNativeCalls(currentUser.getServerBaseUrl());
            try {
                File icon = files.downloadFile(files.getFileName(currentUser.getAppId(), "iconHolderLogin=" + currentUser.getLogin()), getActivity().getCacheDir().getAbsolutePath(), "iconHolderLogin=" + currentUser.getLogin(), currentUser.getAppId());
                String userName = new SecureMessenger(currentUser.getServerBaseUrl()).getUserName(currentUser.getAppId(), currentUser.getLogin());
                getActivity().runOnUiThread(() -> {
                    this.userName.setText(userName);
                    changeIconButton.setImageBitmap(BitmapFactory.decodeFile(icon.getAbsolutePath()));
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        userInfoThread.start();
        Thread calculateCacheDirSize = new Thread(() -> {
            long cacheDirectorySize = 0;
            for (File file : getActivity().getCacheDir().listFiles()) {
                cacheDirectorySize += file.length();
            }
            final long finalCacheDirectorySize = cacheDirectorySize / 1024 / 1024;
            getActivity().runOnUiThread(() -> clearCacheButton.setText(clearCacheButton.getText() + " " + finalCacheDirectorySize + " MB"));
        });
        calculateCacheDirSize.start();
    }
}