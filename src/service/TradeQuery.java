package service;

import config.Config;
import util.HttpsUtil;
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
 * 付啦 订单查询 接口测试
 * Created by wuming on 16/10/24.
 */
@WebServlet("/trade/query")
public class TradeQuery extends HttpServlet{

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.PAY_TRADE_QUERY);
        param.put("app_id", Config.APP_ID);
        param.put("mch_id", Config.MCH_ID);
        param.put("out_trade_no", "5467e445db7348ce9d4232ff8ce32dbf");
        param.put("transaction_id", "20161102183126700df");
        param.put("nonce_str", "TzaETzfe4lgL2hOmfbx9XEttAEuZSuiE");

        // 商户构建请求参数
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println(">>>>post xmlStr: " + xmlStr);
        String resText = HttpsUtil.post(Config.QUERY_ORDER_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<merchant resText: " + resText);
        // 验签后取得支付数据
        if (resText != null) {
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

}
