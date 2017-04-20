package service.pay;

import config.Config;
import util.HttpsUtil;
import util.PayUtil;
import util.XmlUtil;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * 银联支付接口
 * Created by 87119 on 2017/3/7.
 */
public class Unionpay {

    public static void main(String[] args) {
        pre();
    }

    /**
     * 银联预支付接口
     */
    public static void pre() {
        SortedMap<String, String> param = new TreeMap();
        param.put("mch_create_ip", "112.224.33.55");
        param.put("out_trade_no", UUID.randomUUID().toString().replaceAll("-", ""));
        param.put("service", Config.PAY_UNIONPAY_PRE);
        param.put("trans_date", "20170406115201");
        param.put("total_fee", "1");
        param.put("card_by_name", "李云标");
        param.put("card_by_no", "6214835492105448");
        param.put("card_type", "01");
        param.put("cer_type", "01");
        param.put("cer_number", "341227198710121517");
        param.put("mobile", "18658161306");
        param.put("mch_id", Config.MCH_ID);
        param.put("cvv", null);
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println(">>>>post xmlStr: " + xmlStr);
        String resText = HttpsUtil.post(Config.UNIONPAY_PRE_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
            System.out.println("<<<<res map: " + result);
            if (PayUtil.verifyFulaParam(result)) {
                System.out.println("<<<<verify success-----------银联预支付:" + result);
            } else {
                System.out.println("<<<<verify fail-----------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 银联支付验证接口
     */
    public static void pay() {
        SortedMap<String, String> param = new TreeMap();
        param.put("mch_create_ip", "112.224.33.55");
        param.put("service", Config.PAY_UNIONPAY_PAY);
        param.put("mch_id", Config.MCH_ID);
        param.put("order_id", "2017040814001410016395179ae75");
        param.put("ks_pay_order_id", "2017040814611108");
        param.put("yzm", "531461");
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println(">>>>post xmlStr: " + xmlStr);
        String resText = HttpsUtil.post(Config.UNIONPAY_PAY_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
            System.out.println("<<<<res map: " + result);
            if (PayUtil.verifyFulaParam(result)) {
                System.out.println("<<<<verify success-----------------");
                System.out.println("<<<<银联支付:" + result);
            } else {
                System.out.println("<<<<verify fail-----------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
