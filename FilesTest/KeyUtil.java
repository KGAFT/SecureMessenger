

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class KeyUtil {
    public static byte[] generateKey() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256);
        return generator.generateKey().getEncoded();
    }
    public static String byteToString(byte[] key){
        String keyString = "";
        for(int counter = 0; counter< key.length; counter++){
            keyString+=key[counter]+";";
        }
        return keyString;
    }
    public static byte[] stringToByte(String key){
        String[] keyString = key.split(";");
        byte[] byteKey = new byte[keyString.length];
        for(int counter = 0; counter< keyString.length; counter++){
            byteKey[counter] = Byte.parseByte(keyString[counter]);
        }
        return byteKey;
    }
}
