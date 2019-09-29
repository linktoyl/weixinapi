package cn.vitco.security.aes;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * servyou
 *
 * @Title:
 * @Description:
 * @author pjw
 * @date 2018年12月29日 下午4:40:27
 */
public class AESSecurityUtil {
    private static final String UTF8 = "UTF-8";


    private AESSecurityUtil() {

    }

    /*
     * 转为十六进制
     */
    private static String asHex(byte[] buf) {
        StringBuilder sb = new StringBuilder(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if ((buf[i] & 0xff) < 0x10) {
                sb.append("0");
            }
            sb.append(Long.toString(buf[i] & 0xff, 16));
        }
        return sb.toString();
    }

    /*
     * 转为二进制
     */
    private static byte[] asBin(String src) {
        if (src.length() < 1) {
            return new byte[0];
        }
        byte[] encrypted = new byte[src.length() / 2];
        for (int i = 0; i < src.length() / 2; i++) {
            int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);
            encrypted[i] = (byte) (high * 16 + low);
        }
        return encrypted;
    }

    /*
     * 加密
     */
    public static String encryptAES(String data, String secretkey)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException {
        byte[] key = asBin(secretkey);
        SecretKeySpec sKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sKey);
        byte[] encrypted = cipher.doFinal(data.getBytes(UTF8));
        return asHex(encrypted);
    }



    /*
     * 解密
     */
    public static String decryptAES(String encData, String secretkey)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException {
        byte[] tmp = asBin(encData);
        byte[] key = asBin(secretkey);
        SecretKeySpec sKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sKey);
        byte[] decrypted = cipher.doFinal(tmp);
        return new String(decrypted, UTF8);
    }

    public static void main(String[] args) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException,
            UnsupportedEncodingException {
        String key = "11bb077cfee54958a9f369ae835be162";
        String token = "123214";
        System.out.println(encryptAES(token, key));
    }
}