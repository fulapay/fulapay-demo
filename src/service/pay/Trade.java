package service.pay;

import config.Config;
import org.junit.Test;
import util.HttpsUtil;
import util.PayUtil;
import util.XmlUtil;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 付啦 订单查询
 * Created by wuming on 16/10/24.
 */
public class Trade {

    /**
     * 订单查询接口
     * 根据订单号查询订单交易状态
     */
    @Test
    public void query() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.TRADE_QUERY);
        param.put("mch_id", Config.MCH_ID);
        param.put("out_trade_no", "a52fe90f220c4f60a4f3f9a1e7ded936");
        param.put("transaction_id", "201612011110471001639522197ce");

        // 商户构建请求参数
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println(">>>>post xmlStr: " + xmlStr);
        String resText = HttpsUtil.post(Config.TRADE_QUERY_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<merchant resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
            System.out.println("<<<<merchant res map: " + result);
            if (PayUtil.verifyFulaParam(result)) {
                System.out.println("<<<<merchant res verrify success-----------------");
                // 商户逻辑 根据trade_state修改业务逻辑
                // ...
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 订单分页查询
     * 根据商户号(mch_id)分页查询该商户下的订单列表
     */
    @Test
    public void queryPage() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.TRADE_QUERY_PAGE);
        // mch_id与mobile不能同时为空，优先根据mch_id查询
        param.put("mch_id", Config.MCH_ID); // 商户号
        param.put("mobile", Config.MOBILE); // 商户手机号
        // 条件查询参数 非必填
        param.put("trade_state", "SUCCESS"); // 订单状态 参考在线文档中的订单状态值
        // 时间段[)前开后闭
        param.put("start_date", ""); // 开始日期 yyyyMMddHHmmss
        param.put("end_date", ""); // 结束日期 yyyyMMddHHmmss
        // 默认page_size=20
        param.put("page_index", "1");
        param.put("page_size", "2"); // 最大1000

        // 商户构建请求参数
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println(">>>>post xmlStr: " + xmlStr);
        String resText = HttpsUtil.post(Config.TRADE_QUERY_PAGE_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<merchant resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
            System.out.println("<<<<merchant res map: " + result);
            if (PayUtil.verifyFulaParam(result)) {
                System.out.println("<<<<merchant res verrify success-----------------");
                // 商户逻辑 根据trade_state修改业务逻辑
                // ...
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
