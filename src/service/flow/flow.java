package service.flow;

import config.Config;
import org.junit.Test;
import util.HttpsUtil;
import util.PayUtil;
import util.XmlUtil;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by 87119 on 2017/3/4.
 */
public class flow {
    @Test
    public void flow_buy() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.FLOW_BUY);
        param.put("mobile", "18658161306");
        param.put("flow_id", "500000020");
        param.put("notify_url", Config.FLOW_BUY_URL);
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println(">>>>post xmlStr: " + xmlStr);
        String resText = HttpsUtil.post(Config.FLOW_BUY_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<resText: " + resText);
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
            System.out.println("<<<<res map: " + result);
            if (PayUtil.verifyFulaParam(result)) {
                System.out.println("<<<<verify success-----------------");
                System.out.println("<<<<流量充值结果:" + result);
            } else {
                System.out.println("<<<<verify fail-----------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void flow_meal() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.FLOW_MEAL);
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println(">>>>post xmlStr: " + xmlStr);
        String resText = HttpsUtil.post(Config.FLOW_MEAL_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<resText: " + resText);
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
            System.out.println("<<<<res map: " + result);
            if (PayUtil.verifyFulaParam(result)) {
                System.out.println("<<<<verify success-----------------");
                System.out.println("<<<<流量套餐查询结果:" + result);
            } else {
                System.out.println("<<<<verify fail-----------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void flow_order() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.FLOW_ORDER);
        param.put("order_id", "201703231557181001639522fcb12");
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println(">>>>post xmlStr: " + xmlStr);
        String resText = HttpsUtil.post(Config.FLOW_ORDER_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<resText: " + resText);
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
            System.out.println("<<<<res map: " + result);
            if (PayUtil.verifyFulaParam(result)) {
                System.out.println("<<<<流量订单查询结果:" + result);
            } else {
                System.out.println("<<<<verify fail-----------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
