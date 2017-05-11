package service.auth;

import config.Config;
import org.junit.Test;
import util.HttpsUtil;
import util.PayUtil;
import util.XmlUtil;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Created by 87119 on 2017/5/11.
 */
public class BankBranch {

    @Test
    public void banckBranch() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.AUTH_BANK_BRANCH);
        param.put("province_name", "1");
        param.put("city_name", "马云");
        param.put("bank_name", "621481273457448");
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println(">>>>post xmlStr: " + xmlStr);
        String resText = HttpsUtil.post(Config.AUTH_BANK_BRANCH_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
            System.out.println("<<<<res map: " + result);
            if (PayUtil.verifyFulaParam(result)) {
                System.out.println("<<<<verify success-----------银行支行:" + result);
            } else {
                System.out.println("<<<<verify fail-----------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
