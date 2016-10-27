package util;


import config.Config;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.SortedMap;

/**
 * Created by wuming on 16/10/20.
 */
public class PayUtil {
    /**
     * 商家发送统一下单支付请求
     * 商家后台使用私钥签名后请求我方后台
     *
     * @param result
     * @return
     */
    public static String buildRequestXml(SortedMap<String, String> result) {
        result.put("charset", Config.CHARSET);
        result.put("sign_type", Config.SIGN_TYPE);
        StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><xml>");
        StringBuffer buffer = new StringBuffer();
        Iterator iterator = result.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            sb.append("<" + key + ">");
            sb.append(result.get(key));
            sb.append("</" + key + ">");
            buffer.append(key).append("=").append(result.get(key)).append("&");
        }
        String signStr = buffer.substring(0, buffer.length() - 1).toString();
        String sign = RSA.sign(signStr, Config.MCH_PRIVATE_KEY, Config.CHARSET);
        sb.append("<sign>" + sign + "</sign>");
        sb.append("</xml>"); // 根元素
        return sb.toString();
    }

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

    /**
     * 验证付啦notify过来的数据
     * @param request
     * @return
     */
    public static boolean verifyNotify(HttpServletRequest request) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream(), Config.CHARSET));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            System.out.println("<<<<<<merchant notify string:" + sb.toString());
            SortedMap<String, String> param = XmlUtil.doXMLParse(sb.toString());
            System.out.println("<<<<<merchant notify param:" + param);
            if(verifyFulaParam(param)){
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
