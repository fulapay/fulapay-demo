package service.auth;

import config.Config;
import net.sf.json.JSONArray;
import org.junit.Test;
import util.HttpsUtil;
import util.PayUtil;
import util.XmlUtil;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by 87119 on 2017/5/11.
 */
public class BankBranch {

    @Test
    public void banckBranch() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.AUTH_BANK_BRANCH);
        param.put("province_name", "浙江省");
        param.put("city_name", "杭州市");
        param.put("bank_name", "中国银行");
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println(">>>>post xmlStr: " + xmlStr);
        String resText = HttpsUtil.post(Config.AUTH_BANK_BRANCH_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
            JSONArray bankBranch = JSONArray.fromObject(result.get("data"));
            System.out.println("<<<<res data: " + bankBranch);
            Object[] list = bankBranch.toArray();
            for (Object s : list) {
                System.out.println(s);
            }
            if (PayUtil.verifyFulaParam(result)) {
                System.out.println("<<<<verify success-----------银行支行:" + result);
            } else {
                System.out.println("<<<<verify fail-----------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void banckMsg() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.AUTH_BANK_MSG);
        param.put("card_no", "623668123456780856");
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println(">>>>post xmlStr: " + xmlStr);
        String resText = HttpsUtil.post(Config.AUTH_BANK_MSG_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
            if (PayUtil.verifyFulaParam(result)) {
                System.out.println("<<<<verify success----银行名称:" + result.get("bank_name") + "----卡名字：" + result.get("card_name") + "----卡类型:" + result.get("card_type"));
            } else {
                System.out.println("<<<<verify fail-----------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
