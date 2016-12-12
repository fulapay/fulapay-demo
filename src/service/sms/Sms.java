package service.sms;

import config.Config;
import util.HttpsUtil;
import util.PayUtil;
import util.XmlUtil;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by wuming on 16/12/12.
 */
public class Sms {

    public static void main(String[] args) {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.SMS_SEND);
        param.put("tpl_id", "");
        param.put("content", "");
        param.put("mobile", "");
        // 商户构建请求参数
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println(">>>>post xmlStr: " + xmlStr);
        String resText = HttpsUtil.post(Config.SEND_SMS_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
            System.out.println("<<<<res map: " + result);
            if (PayUtil.verifyFulaParam(result)) {
                System.out.println("<<<<verify success-----------------");
                // 商户逻辑 根据trade_state修改业务逻辑
                // ...
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
