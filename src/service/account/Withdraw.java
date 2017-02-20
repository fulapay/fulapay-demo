package service.account;

import config.Config;
import org.junit.Test;
import util.HttpsUtil;
import util.PayUtil;
import util.RSA;
import util.XmlUtil;

import java.util.*;

/**
 * Created by wuming on 2017/2/17.
 * api手动代付
 */
public class Withdraw {

    @Test
    public void withdraw() throws Exception {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.PAY_ACCOUNT_WITHDRAW); // 固定值
        param.put("out_trade_no", UUID.randomUUID().toString().replaceAll("-", ""));
        param.put("total_fee", "600"); // 单位：分
        param.put("mobile", "15280533697");
        param.put("bank_account_no", RSA.encrypt("6225685866687388", Config.PUBLIC_KEY, Config.CHARSET));
        param.put("bank_account_name", RSA.encrypt("无名", Config.PUBLIC_KEY, Config.CHARSET));
        param.put("bank_mobile", "15280533697");
        param.put("is_company", "0"); // 0个人帐户 1对公帐户
        param.put("bank_subbranch_no", ""); // 联行号 选填
        param.put("remark", "提现测试"); // 备注 选填
        // 商户构建请求参数
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println("----request data: " + xmlStr);
        String resText = HttpsUtil.post(Config.ACCOUNT_WITHDRAW_URL, xmlStr, Config.CHARSET);
        System.out.println("-----resText: " + resText);
        // 验签后取得支付数据
        Map<String, Object> responseMap = new HashMap<>();
        try {
            SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
            if (PayUtil.verifyFulaParam(result)) {
                String resCode = result.get("res_code");
                String resMsg = result.get("res_msg");
                String resultCode = result.get("result_code");
                if("0000".equals(resCode) && "S".equals(resultCode)){
                    System.out.println("提现申请成功！");
                    responseMap.put("success", true);
                    // 具体到帐通知由notify信息通知
                }
                System.out.println("----操作结果：" + result);
            } else {
                responseMap.put("error", "数据验签失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("error", "代付请求返回数据解析异常");
        }
        System.out.println("响应结果: " + responseMap);
    }
}
