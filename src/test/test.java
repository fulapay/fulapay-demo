package test;

import org.junit.Test;
import util.PayUtil;
import util.XmlUtil;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by 87119 on 2017/5/23.
 */
public class test {

    @Test
    public void testSing() {
        SortedMap<String, String> param = new TreeMap();
        param.put("test", "支付测试");
        param.put("mch_create_ip", "112.224.33.55");
        param.put("today", "今天下雨");
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println("加密后==" + xmlStr);
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(xmlStr);
            if (PayUtil.verifyFulaParam(result)) {
                System.out.println("验证成功");
            } else {
                System.out.println("验证失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
