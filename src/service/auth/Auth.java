package service.auth;

import config.Config;
import net.sf.json.JSONObject;
import org.junit.Test;
import util.HttpsUtil;
import util.PayUtil;
import util.XmlUtil;

import javax.management.AttributeList;
import java.util.*;

/**
 * Created by 87119 on 2017/4/20.
 */
public class Auth {
    @Test
    public void authBank() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.AUTH_BANK_SERVICE);
        param.put("name", "李云标");
        param.put("id_no", "341227198710121517");
        param.put("mobile", "18658161306");
        param.put("bank_no", "6214835492105448");
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        String resText = HttpsUtil.post(Config.AUTH_BANK_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
            System.out.println("<<<<res map: " + result);
            if (PayUtil.verifyFulaParam(result)) {
                System.out.println("<<<<银行卡四要素:" + result);
            } else {
                System.out.println("<<<<verify fail-----------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void authId() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.AUTH_ID_CARD_SERVICE);
        param.put("name", "王思聪");
        param.put("id_card", "341228199015121617");
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        String resText = HttpsUtil.post(Config.AUTH_ID_CARD_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
            System.out.println("<<<<res map: " + result);
            if (PayUtil.verifyFulaParam(result)) {
                System.out.println("<<<<身份证验证:" + result);
            } else {
                System.out.println("<<<<verify fail-----------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
