package util;


import config.Config;
import org.junit.Test;

import java.security.*;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by 87119 on 2017/6/9.
 */
public class secretKeyUtil {
    /**
     * 生成公私钥
     *
     * @throws Exception
     */
    @Test
    public void createKeyPairs() throws Exception {
        //Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        // create the keys
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        PublicKey pubKey = pair.getPublic();
        PrivateKey privKey = pair.getPrivate();
        System.out.println("格式----------" + privKey.getFormat());
        byte[] pk = pubKey.getEncoded();
        byte[] privk = privKey.getEncoded();
        String strpk = new String(Base64.encode(pk));
        String strprivk = new String(Base64.encode(privk));

        System.out.println("公钥:" + Arrays.toString(pk));
        System.out.println("私钥:" + Arrays.toString(privk));
        System.out.println("公钥Base64编码:" + strpk);
        System.out.println("私钥Base64编码:" + strprivk);
    }


    /**
     * 验证是否是同一对
     *
     * @throws Exception
     */
    @Test
    public void Test3() throws Exception {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.AUTH_BANK_SERVICE);
        param.put("name", "马云");
        param.put("id_no", "341227196710181719");
        param.put("mobile", "18868689900");
        param.put("bank_no", "12345565455454445");
        String xmlStr = PayUtil.buildRequestXml(param);
        SortedMap<String, String> result = XmlUtil.doXMLParse(xmlStr);
        if (PayUtil.verifyFulaParam(result)) {
            System.out.println("验签成功");
        } else {
            System.out.println("验签失败");
        }
    }
}
