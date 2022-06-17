import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {
    private static String downloadUrl = "http://192.168.1.66:45632/getFile";
    private static String infoUrl = "http://192.168.1.66:45632/getFileName";
    private static String uploadUrl = "http://192.168.1.66:45632/uploadFile";
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256);
        byte[] key = generator.generateKey().getEncoded();
        System.out.println();
        EncryptedFileManager manager = new EncryptedFileManager(downloadUrl, uploadUrl, infoUrl, "6653014072124009960", key, "C:/Users/Daniil");
        long fileId = manager.uploadFile(new File("E:/videos/Escape From Tarkov/tarkov_highlight.mp4"));
        manager.downloadFileAndDecrypt(fileId);
    }
}
