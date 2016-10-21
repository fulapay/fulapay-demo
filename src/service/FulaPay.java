package service;

import config.Config;
import util.HttpUtil;
import util.PayUtil;
import util.XmlUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by wuming on 16/10/20.
 */
@WebServlet("/qrcode")
public class FulaPay extends HttpServlet{

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SortedMap<String, String> param = new TreeMap();
        String service = Config.PAY_ALIPAY_QRCODE;
        param.put("service", service);
        // TODO wuming 16/10/21 如果 service为fula.xxx.scan 扫码支付需要authCode 参数（支付宝或者微信扫码支付的上显示的code）
        if(Config.PAY_WXPAY_SCAN.equals(service) || Config.PAY_ALIPAY_QRCODE.equals(service)){
            param.put("authCode", "");
        }
        param.put("mch_id", "8012667850604828");
        param.put("app_id", "1000000");
        param.put("out_trade_no", "RNl6u3xDGM5D2Ta73d6upAgnweWDNzEX");
        param.put("total_fee", "1");
        param.put("body", "支付测试");
        param.put("mch_create_ip", "127.0.0.1");
        param.put("nonce_str", "TzaETzfe4lgL2hOmfbx9XEttAEuZSuiE");
        param.put("notify_url", "https://121.43.161.81:8888/mchPay/notify");
        // 商户构建请求参数
        String xmlStr = PayUtil.unifiedOrderRequest(param);
        String resText = HttpUtil.post(Config.UNIFIED_ORDER_URL, xmlStr, Config.CHARSET);
        System.out.println("-----resText: " + resText);
        // 验签后取得支付数据
        if (resText != null) {
            try {
                SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
                if (PayUtil.verifyFulaParam(result)) {
                    String resCode = result.get("res_code");
                    String resMsg = result.get("res_msg");
                    String resultCode = result.get("result_code");
                    if ("0000".equals(resCode) && "S".equals(resultCode)) {
                        // TODO payInfo 及付啦支付返回的二维码数据,商户只需要利用这个数据生成二维码后即可使用支付宝扫码支付
                        // 二维码支付
                        String payInfo = result.get("pay_info");
                        System.out.println("支付二维码数据" + payInfo);
                    } else {

                    }
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
