package service.pay;

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

    /**
     * 微信公众号支付 仅能线上测试
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SortedMap<String, String> param = new TreeMap();
        String merchant_no="";      //商户号
        String total_fee="";        //支付金额
        String out_trade_no="";     //商户系统内部的订单号
        String notify_url="";       //回调路径
        String description="";      //订单描述
        param.put("merchantNo",merchant_no);
        param.put("total_fee",total_fee);
        param.put("out_trade_no",out_trade_no);
        param.put("notify_url",notify_url);
        param.put("description",description);
        //构成商户请求数据
        String queryStr = PayUtil.buildRequestToString(param);
        String payUrl = Config.JS_SHOU_KUAN_URL + "?" + URLEncoder.encode(queryStr, Config.CHARSET);
        System.out.println("-----pay url:" + payUrl);
        resp.sendRedirect(payUrl);
    }

    /**
     * TODO
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
        // TODO js支付仅能线上测试，以下两个参数要填写线上的
        param.put("app_id", Config.APP_ID); // 线上环境付啦开放平台申请的appId
        param.put("mch_id", Config.MCH_ID); // 线上环境付啦开放平台分发的商户号（通过商户进件接口获取）
        param.put("out_trade_no", UUID.randomUUID().toString().replaceAll("-", ""));
        param.put("total_fee", req.getParameter("totalFee"));
        param.put("body", "支付测试");
        param.put("mch_create_ip", "112.224.33.55"); // 真实填写服务器端ip
        param.put("nonce_str", "TzaETzfe4lgL2hOmfbx9XEttAEuZSuiE");
        param.put("notify_url", Config.NOTIFY_URL);
        param.put("return_url", Config.RETURN_URL); // 支付宝或者微信JS支付成功时 同步跳转url 必填
        param.put("cancel_url", Config.CANCEL_URL); // 支付宝或者微信JS支付过程中，用户取消支付时 同步跳转url 选填
        param.put("error_url", Config.ERROR_URL); // 支付宝或者微信JS支付出错时 同步跳转url 选填
        // 商户构建请求参数
        String queryStr = PayUtil.buildRequest(param);
        // 拼接payUrl时queryStr需要URLEncode处理，否则sign字段会出现问题
        String payUrl = Config.JS_PAY_URL + "?" + URLEncoder.encode(queryStr, Config.CHARSET);
        System.out.println("-----pay url:" + payUrl);
        resp.sendRedirect(payUrl);

    }

}
