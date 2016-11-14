package service;

import config.Config;
import util.HttpsUtil;
import util.PayUtil;
import util.XmlUtil;

import javax.servlet.http.HttpServlet;
import java.io.PrintWriter;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 付啦 统一支付 接口测试
 * Created by wuming on 16/10/20.
 */
public class UnifiedPay extends HttpServlet{

    public static void main(String[] args) {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.PAY_UNIFIED_PAY);
//        param.put("service", Config.PAY_UNIFIED_QRCODE);
        param.put("app_id", Config.APP_ID); // 测试用app_id
        param.put("mch_id", Config.MCH_ID);// 商户号 可以不填写
        param.put("mobile", "18658103308"); // mobile必填 没有商户号则以mobile查询相关商户
        param.put("nonce_str", "TzaETzfe4lgL2hOmfbx9XEttAEuZSuiE");
        // 商户构建请求参数
        String xmlStr = PayUtil.buildRequestXml(param);
        String resText = HttpsUtil.post(Config.UNIFIED_PAY_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<-----resText: " + resText);
        // 验签后取得支付数据
        PrintWriter out = null;
        if (resText != null) {
            try {
                SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
                if (PayUtil.verifyFulaParam(result)) {
                    System.out.printf("<<<<----result: " + result);
                    String resCode = result.get("res_code");
                    String resMsg = result.get("res_msg");
                    String resultCode = result.get("result_code");
                    if ("0000".equals(resCode) && "S".equals(resultCode)) {
                        // 二维码支付
                        System.out.println("-----------success----------------");

                    } else {

                    }
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else{

        }
    }

}
