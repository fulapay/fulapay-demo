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
        param.put("out_trade_no", UUID.randomUUID().toString().replaceAll("-", "")); // 商户提现订单号 保证唯一，不可重复
        param.put("total_fee", "600"); // 单位：分
        param.put("mobile", "15280533697");
        param.put("bank_account_no", RSA.encrypt("6225685866687388", Config.PUBLIC_KEY, Config.CHARSET)); // 使用付啦公钥对帐号信息加密
        param.put("bank_account_name", RSA.encrypt("无名", Config.PUBLIC_KEY, Config.CHARSET)); // 使用付啦公钥对户名信息加密
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

    /**
     * 提现订单结果接口
     * transaction_id 与 out_trade_no 不能同时为空
     *  1.如果仅有其一，则以此值查询订单返回
     *  2.如果两个值都不空，则以transaction_id优先查询订单详情，并验证订单数据是否与所传参数out_trade_no值一致
     */
    @Test
    public void query() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.PAY_ACCOUNT_WITHDRAW_QUERY);
        param.put("out_trade_no", "b6dd49c21bfc48f7a672d702c15f6a77"); // 商户平台订单号
        param.put("transaction_id", "20170218202902100163952279170"); // 付啦平台订单号
        // 商户构建请求参数
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println(">>>>post xmlStr: " + xmlStr);
        String resText = HttpsUtil.post(Config.ACCOUNT_WITHDRAW_QUERY_URL, xmlStr, Config.CHARSET);
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
