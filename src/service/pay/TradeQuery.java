package service.pay;

import config.Config;
import util.HttpsUtil;
import util.PayUtil;
import util.XmlUtil;

import javax.servlet.http.HttpServlet;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 付啦 订单查询 接口测试
 * Created by wuming on 16/10/24.
 */
public class TradeQuery extends HttpServlet{

    public static void main(String[] agrs) {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.PAY_TRADE_QUERY);
        param.put("mch_id", Config.MCH_ID);
        param.put("out_trade_no", "a52fe90f220c4f60a4f3f9a1e7ded936");
        param.put("transaction_id", "201612011110471001639522197ce");

        // 商户构建请求参数
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println(">>>>post xmlStr: " + xmlStr);
        String resText = HttpsUtil.post(Config.QUERY_ORDER_URL, xmlStr, Config.CHARSET);
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
