package service;

import config.Config;
import net.sf.json.JSONObject;
import util.HttpUtil;
import util.PayUtil;
import util.XmlUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by wuming on 16/10/20.
 */
@WebServlet("/fulaPay")
public class FulaPay extends HttpServlet{

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/index.jsp");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        SortedMap<String, String> param = new TreeMap();
//        String service = Config.PAY_ALIPAY_QRCODE;
        String service = req.getParameter("service");
        param.put("service", service);
        // TODO wuming 16/10/21 如果 service为fula.xxx.scan 扫码支付需要authCode 参数（支付宝或者微信扫码支付的上显示的code）
        if(Config.PAY_WXPAY_SCAN.equals(service) || Config.PAY_ALIPAY_SCAN.equals(service)){
            param.put("authCode", req.getParameter("authCode"));
        }
        param.put("mch_id", "8012667850604828");
        param.put("app_id", "1000000");
        param.put("out_trade_no", UUID.randomUUID().toString());
        param.put("total_fee", req.getParameter("totalFee"));
        param.put("body", "支付测试");
        param.put("mch_create_ip", "127.0.0.1");
        param.put("nonce_str", "TzaETzfe4lgL2hOmfbx9XEttAEuZSuiE");
        param.put("notify_url", "https://121.43.161.81:8888/mchPay/notify");
        // 商户构建请求参数
        String xmlStr = PayUtil.unifiedOrderRequest(param);
        String resText = HttpUtil.post(Config.UNIFIED_ORDER_URL, xmlStr, Config.CHARSET);
        System.out.println("-----resText: " + resText);
        // 验签后取得支付数据
        Map<String, Object> responseMap = new HashMap<>();
        PrintWriter out = null;
        if (resText != null) {
            try {
                SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
                if (PayUtil.verifyFulaParam(result)) {
                    String resCode = result.get("res_code");
                    String resMsg = result.get("res_msg");
                    String resultCode = result.get("result_code");
                    if ("0000".equals(resCode) && "S".equals(resultCode)) {
                        if(Config.PAY_WXPAY_QRCODE.equals(service) || Config.PAY_ALIPAY_QRCODE.equals(service)){
                            // TODO payInfo 及付啦支付返回的二维码数据,商户只需要利用这个数据生成二维码后即可使用支付宝扫码支付
                            // 二维码支付
                            String payInfo = result.get("pay_info");
                            System.out.println("支付二维码数据" + payInfo);
                            responseMap.put("payInfo", payInfo);
                            responseMap.put("success", true);
                        } else if(Config.PAY_WXPAY_SCAN.equals(service) || Config.PAY_ALIPAY_SCAN.equals(service)) {
                            // 扫码支付 同步返回成功
                            responseMap.put("success", true);
                        }
                    } else {
                        responseMap.put("error", resCode + ":" + resMsg);
                    }
                } else {
                    responseMap.put("error", "数据验签失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                responseMap.put("error", "代付请求返回数据解析异常");
            }
        } else{
            responseMap.put("error", "代付请求响应为null");
        }
        System.out.println("响应结果: " + responseMap);

        try {
            out = resp.getWriter();
            JSONObject jsonObject = JSONObject.fromObject(responseMap);
            System.out.println("json string: " + jsonObject.toString());
            out.write(jsonObject.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
