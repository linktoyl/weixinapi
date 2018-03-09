package cn.vitco.wx.aes;

import cn.vitco.wx.exception.WxException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

/**
 * Created by Sterling on 2018/2/7.
 */
public class WxBizDataCrypt {
    private static boolean initialized = false;
    // 算法名称
    static  final String KEY_ALGORITHM = "AES";
    // 加解密算法/模式/填充方式
    static  final String ALGORITHM_STR = "AES/CBC/PKCS7Padding";
    private static Key key;
    private static Cipher cipher;


    private String sessionKey;

    private static void initialize(){
        if (initialized) return;
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null){
            Security.addProvider(new BouncyCastleProvider());
        }
        initialized = true;
    }

    public WxBizDataCrypt() {
    }

    public WxBizDataCrypt(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    /**
     * 解密
     * @param encryptedData 带解密数据
     * @param iv            解密偏移向量
     * @return
     */
    public String decryptData(String encryptedData, String iv) throws WxException{
        initialize();

        try {
            byte[] sessionKeyBy = Base64.decode(sessionKey);
            byte[] encryptedDataBy = Base64.decode(encryptedData);
            byte[] ivByte = Base64.decode(iv);
            Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
            Key sKeySpec = new SecretKeySpec(sessionKeyBy, KEY_ALGORITHM);
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));
            byte[] result = cipher.doFinal(encryptedDataBy);
            return new String(result);
        } catch (Exception e) {
            throw new WxException("微信小程序数据解密失败!", e.getCause());
        }
    }

    /**
     * 生成iv
     * @param iv
     * @return
     * @throws Exception
     */
    private static AlgorithmParameters generateIV(byte[] iv) throws Exception{
        AlgorithmParameters params = AlgorithmParameters.getInstance(KEY_ALGORITHM);
        params.init(new IvParameterSpec(iv));
        return params;
    }
}
