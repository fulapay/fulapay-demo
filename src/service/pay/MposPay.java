package service.pay;

import config.Config;
import net.sf.json.JSONObject;
import util.HttpsUtil;
import util.PayUtil;

import javax.servlet.http.HttpServlet;
import java.util.*;

/**
 * mpos 大额转帐
 * JSON 格式请求
 * Created by wuming on 16/10/20.
 */
public class MposPay extends HttpServlet {


    public static void main(String[] args) {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.PAY_MPOS_SUPER_TRANSFER);
        param.put("mch_id", Config.MCH_ID);
        param.put("out_trade_no", UUID.randomUUID().toString().replaceAll("-", ""));
        param.put("total_fee", "600");
        param.put("body", "支付测试");
        param.put("mch_create_ip", "127.0.0.1");
        param.put("notify_url", Config.NOTIFY_URL);
        // 商户构建请求参数
        String jsonStr = PayUtil.buildRequestJson(param);
        String resText = HttpsUtil.post(Config.MPOS_PREPARE_URL, jsonStr, Config.CHARSET);
        System.out.println("-----resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = new TreeMap<>();
            JSONObject object = JSONObject.fromObject(resText);
            for (Object o : object.keySet()) {
                result.put((String)o, (String) object.get(o));
            }
            if (PayUtil.verifyFulaParam(result)) {
                String resCode = result.get("res_code");
                String resMsg = result.get("res_msg");
                String resultCode = result.get("result_code");
                if ("0000".equals(resCode) && "S".equals(resultCode)) {
                    String prepareId = result.get("prepare_id");
                    System.out.println("======prepareId: " + prepareId);
                } else {
                    System.out.println("error:" + resCode + ":" + resMsg);
                }
            } else {
                System.out.println("数据验签失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
