package service.pay;

import config.Config;
import net.sf.json.JSONObject;
import org.junit.Test;
import util.HttpsUtil;
import util.PayUtil;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * mpos 大额转帐
 * JSON 格式请求
 * Created by wuming on 16/10/20.
 */
public class Mpos {

    /**
     * 预下单
     */
    @Test
    public void prepare() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.PAY_MPOS_PREPARE_SUPER_TRANSFER);
        param.put("mchId", Config.MCH_ID);
        param.put("outTradeNo", UUID.randomUUID().toString().replaceAll("-", ""));
        param.put("totalFee", "600");
        param.put("body", "支付测试");
        param.put("mchCreateIp", "127.0.0.1");
        param.put("notifyUrl", Config.NOTIFY_URL);
        // 商户构建请求参数
        String jsonStr = PayUtil.buildRequestJson(param);
        String resText = HttpsUtil.post(Config.MPOS_PREPARE_URL, jsonStr, Config.CHARSET);
        System.out.println("-----resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = new TreeMap<>();
            JSONObject object = JSONObject.fromObject(resText);
            for (Object o : object.keySet()) {
                result.put((String)o, String.valueOf(object.get(o)));
            }
            if (PayUtil.verifyFulaParam(result)) {
                String resCode = result.get("resCode");
                String resMsg = result.get("resMsg");
                String resultCode = result.get("resultCode");
                if ("0000".equals(resCode) && "S".equals(resultCode)) {
                    String orderId = result.get("orderId");
                    System.out.println("======prepareId: " + orderId);
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

    /**
     * MPOS 交易
     */
    @Test
    public void pay() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.PAY_MPOS_PAY);
        param.put("orderId", UUID.randomUUID().toString().replaceAll("-", ""));
        param.put("amount", "600");
        param.put("ksn", "");
        param.put("deviceName", "");
        param.put("track2Data", "");
        param.put("track3Data", ""); // 选填
        param.put("icData", "");
        param.put("serialNum", "");
        param.put("withdrawType", ""); // 选填
        param.put("accountNo", "");
        param.put("pinblock", "");
        param.put("validDate", "");
        param.put("mobile", "");
        param.put("recAccountNo", ""); // 选填
        // 商户构建请求参数
        String jsonStr = PayUtil.buildRequestJson(param);
        String resText = HttpsUtil.post(Config.MPOS_PAY_URL, jsonStr, Config.CHARSET);
        System.out.println("-----resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = new TreeMap<>();
            JSONObject object = JSONObject.fromObject(resText);
            for (Object o : object.keySet()) {
                result.put((String)o, String.valueOf(object.get(o)));
            }
            if (PayUtil.verifyFulaParam(result)) {
                String resCode = result.get("resCode");
                String resMsg = result.get("resMsg");
                String resultCode = result.get("resultCode");
                if ("0000".equals(resCode) && "S".equals(resultCode)) {
                    System.out.println("----交易成功----");
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

    /**
     * MPOS大额转帐
     */
    @Test
    public void transfer() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.PAY_MPOS_TRANSFER);
        param.put("orderId", UUID.randomUUID().toString().replaceAll("-", ""));
        param.put("amount", "600");
        param.put("ksn", "");
        param.put("deviceName", "");
        param.put("track2Data", "");
        param.put("track3Data", ""); // 选填
        param.put("icData", "");
        param.put("serialNum", "");
        param.put("withdrawType", ""); // 选填
        param.put("accountNo", "");
        param.put("pinblock", "");
        param.put("validDate", "");
        param.put("mobile", "");
        param.put("recAccountNo", ""); // 选填
        // 商户构建请求参数
        String jsonStr = PayUtil.buildRequestJson(param);
        String resText = HttpsUtil.post(Config.PAY_MPOS_TRANSFER, jsonStr, Config.CHARSET);
        System.out.println("-----resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = new TreeMap<>();
            JSONObject object = JSONObject.fromObject(resText);
            for (Object o : object.keySet()) {
                result.put((String)o, String.valueOf(object.get(o)));
            }
            if (PayUtil.verifyFulaParam(result)) {
                String resCode = result.get("resCode");
                String resMsg = result.get("resMsg");
                String resultCode = result.get("resultCode");
                if ("0000".equals(resCode) && "S".equals(resultCode)) {
                    System.out.println("----转帐成功----");
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

    /**
     * MPOS余额查询
     * 返回参数中 balance 为当前余额
     */
    @Test
    public void balanceQuery() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.PAY_MPOS_BALANCE_QUERY);
        param.put("mobile", "");
        param.put("ksn", "");
        param.put("deviceName", "");
        param.put("track2Data", "");
        param.put("track3Data", ""); // 选填
        param.put("icData", "");
        param.put("serialNum", "");
        param.put("accountNo", "");
        param.put("pinblock", "");
        param.put("validDate", "");
        // 商户构建请求参数
        String jsonStr = PayUtil.buildRequestJson(param);
        String resText = HttpsUtil.post(Config.PAY_MPOS_TRANSFER, jsonStr, Config.CHARSET);
        System.out.println("-----resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = new TreeMap<>();
            JSONObject object = JSONObject.fromObject(resText);
            for (Object o : object.keySet()) {
                result.put((String)o, String.valueOf(object.get(o)));
            }
            if (PayUtil.verifyFulaParam(result)) {
                String resCode = result.get("resCode");
                String resMsg = result.get("resMsg");
                String resultCode = result.get("resultCode");
                if ("0000".equals(resCode) && "S".equals(resultCode)) {
                    System.out.println("----余额----" + param.get("balance"));
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

    /**
     * MPOS状态检测
     * 返回值 binding=0（没有绑定）或bding=1（被当前手机号绑定）时可以正常交易
     * binding=2被其他人绑定，无法正常交易
     */
    @Test
    public void checkin() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.PAY_MPOS_CHECKIN);
        param.put("orderId", UUID.randomUUID().toString().replaceAll("-", ""));
        param.put("mobile", "");
        param.put("deviceId", "");
        param.put("sn", "");
        param.put("name", "");
        param.put("withdrawType", ""); // 选填
        // 商户构建请求参数
        String jsonStr = PayUtil.buildRequestJson(param);
        String resText = HttpsUtil.post(Config.MPOS_CHECKIN_URL, jsonStr, Config.CHARSET);
        System.out.println("-----resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = new TreeMap<>();
            JSONObject object = JSONObject.fromObject(resText);
            for (Object o : object.keySet()) {
                result.put((String)o, String.valueOf(object.get(o)));
            }
            if (PayUtil.verifyFulaParam(result)) {
                String resCode = result.get("resCode");
                String resMsg = result.get("resMsg");
                String resultCode = result.get("resultCode");
                if ("0000".equals(resCode) && "S".equals(resultCode)) {
                    System.out.println("----binding----" + param.get("binding"));
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

    /**
     * MPOS绑定
     */
    @Test
    public void binding() {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.PAY_MPOS_BINDING);
        param.put("mobile", "");
        param.put("deviceId", "");
        param.put("sn", "");
        param.put("name", ""); // 选填
        // 商户构建请求参数
        String jsonStr = PayUtil.buildRequestJson(param);
        String resText = HttpsUtil.post(Config.MPOS_CHECKIN_URL, jsonStr, Config.CHARSET);
        System.out.println("-----resText: " + resText);
        // 验签后取得支付数据
        try {
            SortedMap<String, String> result = new TreeMap<>();
            JSONObject object = JSONObject.fromObject(resText);
            for (Object o : object.keySet()) {
                result.put((String)o, String.valueOf(object.get(o)));
            }
            if (PayUtil.verifyFulaParam(result)) {
                String resCode = result.get("resCode");
                String resMsg = result.get("resMsg");
                String resultCode = result.get("resultCode");
                if ("0000".equals(resCode) && "S".equals(resultCode)) {
                    System.out.println("----处理成功----");
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
