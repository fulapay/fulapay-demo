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
        param.put("tpl_id", "15"); // 测试环境无需修改，真实环境根据付啦开放后台申请的模板ID真实填写
        param.put("content", "我是短信测试"); // 短信内容
        param.put("mobile", "13123936686"); // 要发送到的手机号
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
                System.out.println("<<<<短信发送结果" + result);
            } else {
                System.out.println("<<<<verify fail-----------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
