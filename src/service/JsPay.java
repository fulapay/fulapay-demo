package service;

import config.Config;
import util.PayUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * 支付宝或微信支付 接口测试
 * Created by wuming on 16/10/20.
 */
@WebServlet("/jspay")
public class JsPay extends HttpServlet{

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/jspay.jsp");
    }

    /**
     * 支付宝或微信支付 仅能线上测试
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SortedMap<String, String> param = new TreeMap();
        String service = req.getParameter("service");
        param.put("service", service);
        param.put("app_id", Config.APP_ID);
        param.put("mch_id", Config.MCH_ID);
        param.put("out_trade_no", UUID.randomUUID().toString().replaceAll("-", ""));
        param.put("total_fee", req.getParameter("totalFee"));
        param.put("body", "支付测试");
        param.put("mch_create_ip", "127.0.0.1");
        param.put("nonce_str", "TzaETzfe4lgL2hOmfbx9XEttAEuZSuiE");
        param.put("notify_url", Config.NOTIFY_URL);
        param.put("return_url", Config.RETURN_URL); // 支付宝或者微信支付时 同步跳转url 必填
        // 商户构建请求参数
        String queryStr = PayUtil.buildRequest(param);
        String payUrl = Config.JS_PAY_URL + "?" + URLEncoder.encode(queryStr, Config.CHARSET);
        System.out.println("-----pay url:" + URLEncoder.encode(queryStr, Config.CHARSET));
        resp.sendRedirect(payUrl);

    }

}
