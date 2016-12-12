package util;


import config.Config;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.UUID;

/**
 * Created by wuming on 16/10/20.
 */
public class PayUtil {

    /**
     * 构建GET方法请求数据
     * 商家后台使用私钥签名后请求付啦
     * @param result
     * @return
     */
    public static String buildRequest(SortedMap<String, String> result) {
        result.put("charset", Config.CHARSET);
        result.put("sign_type", Config.SIGN_TYPE);
        StringBuffer sb = new StringBuffer();
        Iterator iterator = result.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            sb.append(key).append("=").append(result.get(key)).append("&");
        }
        String signStr = sb.substring(0, sb.length() - 1).toString();
        String sign = RSA.sign(signStr, Config.MCH_PRIVATE_KEY, Config.CHARSET);
        return signStr + "&sign=" + sign;
    }
    
    /**
     * 构建POST方法XML请求数据
     * 商家后台使用私钥签名后请求付啦
     * @param param
     * @return
     */
    public static String buildRequestXml(SortedMap<String, String> param) {
        param.put("app_id", Config.APP_ID);
        param.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", ""));
        param.put("charset", Config.CHARSET);
        param.put("sign_type", Config.SIGN_TYPE);
        StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><xml>");
        StringBuffer buffer = new StringBuffer();
        Iterator iterator = param.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            sb.append("<" + key + ">");
            sb.append(param.get(key));
            sb.append("</" + key + ">");
            buffer.append(key).append("=").append(param.get(key)).append("&");
        }
        String signStr = buffer.substring(0, buffer.length() - 1).toString();
        String sign = RSA.sign(signStr, Config.MCH_PRIVATE_KEY, Config.CHARSET);
        sb.append("<sign>" + sign + "</sign>");
        sb.append("</xml>"); // 根元素
        return sb.toString();
    }

    /**
     * 读取付啦异步通知notify回来的数据
     * @param request
     * @return
     * @throws Exception
     */
    public static SortedMap<String, String> readFulaNotify(HttpServletRequest request) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream(), Config.CHARSET));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        System.out.println("<<<<<<merchant notify string:" + sb.toString());
        SortedMap<String, String> param = XmlUtil.doXMLParse(sb.toString());
        System.out.println("<<<<<merchant notify param:" + param);
        return param;
    }

    /**
     * 验证付啦返回数据的合法性
     * @param param
     * @return
     */
    public static boolean verifyFulaParam(SortedMap<String, String> param) {
        StringBuffer sb = new StringBuffer();
        Iterator<String> iterator = param.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (!"sign".equals(key)) {
                sb.append(key).append("=").append(param.get(key)).append("&");
            }
        }
        String verifyString = sb.substring(0, sb.length() - 1).toString();
        // 使用我方公私钥进行验签
        if (RSA.verify(verifyString, param.get("sign"), Config.PUBLIC_KEY, Config.CHARSET)) {
            return true;
        }
        return false;

    }

}
