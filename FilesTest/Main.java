import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        String baseUrl = "http://192.168.1.68:45632/";
        byte[] key = KeyUtil.stringToByte("-47;116;-47;-88;85;20;-26;-4;28;91;93;54;13;-63;-57;-79;-62;109;124;24;-11;-62;-25;-68;50;-74;124;5;116;-45;43;51;");
        EncryptedFileManager manager = new EncryptedFileManager(baseUrl+"getFile", baseUrl+"uploadFile", baseUrl+"getFileName", "-7030637088824337927", key, "/Users/user/Documents");
        Cipher cipher =Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
        String text = "Hello my friend!";
        byte[] encryptedText = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        for (byte b : encryptedText) {
            System.out.print(b+";");
        }
        System.out.println(manager.uploadFile(new File("/Users/user/Documents/image.png")));
    }
}
