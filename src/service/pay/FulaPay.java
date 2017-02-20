package service.pay;

import config.Config;
import net.sf.json.JSONObject;
import util.HttpsUtil;
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
 * 付啦 统一下单 接口测试
 * Created by wuming on 16/10/20.
 */
@WebServlet("/fulaPay")
public class FulaPay extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/index.jsp");
    }

    /**
     * 统一下单接口
     * 接口 service 参数的值仅能为以下4个，每个参数对应一种支付方式
     * pay.alipay.qrcode 支付宝二维码支付
     * pay.wxpay.qrcode 微信二维码支付
     * pay.alipay.scan 支付宝扫码支付
     * pay.wxpay.scan 微信扫码支付
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        SortedMap<String, String> param = new TreeMap();
        String service = req.getParameter("service");
        param.put("service", service);
        // 如果 service为fula.xxx.scan 扫码支付需要authCode 参数（支付宝或者微信扫码支付的上显示的code）
        if (Config.PAY_WXPAY_SCAN.equals(service) || Config.PAY_ALIPAY_SCAN.equals(service)) {
            param.put("auth_code", req.getParameter("authCode"));
        }
        param.put("mch_id", Config.MCH_ID);
        param.put("out_trade_no", UUID.randomUUID().toString().replaceAll("-", ""));
        param.put("total_fee", req.getParameter("totalFee"));
        param.put("body", "支付测试");
        param.put("mch_create_ip", "127.0.0.1");
        param.put("notify_url", Config.NOTIFY_URL);
        // 商户构建请求参数
        String xmlStr = PayUtil.buildRequestXml(param);
        String resText = HttpsUtil.post(Config.UNIFIED_ORDER_URL, xmlStr, Config.CHARSET);
        System.out.println("-----resText: " + resText);
        // 验签后取得支付数据
        Map<String, Object> responseMap = new HashMap<>();
        PrintWriter out = null;
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
            if (PayUtil.verifyFulaParam(result)) {
                String resCode = result.get("res_code");
                String resMsg = result.get("res_msg");
                String resultCode = result.get("result_code");
                if ("0000".equals(resCode) && "S".equals(resultCode)) {
                    if (Config.PAY_WXPAY_QRCODE.equals(service) || Config.PAY_ALIPAY_QRCODE.equals(service)) {
                        // TODO payInfo 及付啦支付返回的二维码数据,商户只需要利用这个数据生成二维码后即可使用支付宝扫码支付
                        // 二维码支付
                        String payInfo = result.get("pay_info");
                        System.out.println("支付二维码数据" + payInfo);
                        responseMap.put("payInfo", payInfo);
                        responseMap.put("success", true);
                    } else if (Config.PAY_WXPAY_SCAN.equals(service) || Config.PAY_ALIPAY_SCAN.equals(service)) {
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
