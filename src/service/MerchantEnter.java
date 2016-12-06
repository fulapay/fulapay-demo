package service;

import config.Config;
import util.HttpsUtil;
import util.ImageUtil;
import util.PayUtil;
import util.XmlUtil;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 付啦 商户进件 接口测试
 * Created by wuming on 16/10/29.
 */
public class MerchantEnter {

    public static void main(String[] args) throws Exception {
        SortedMap<String, String> param = new TreeMap();
        param.put("service", Config.PAY_MERCHANT_ENTER);
        param.put("type", Config.MERCHANT_ENTER_TYPE_BASE_ADD);
//        param.put("type", Config.MERCHANT_ENTER_TYPE_BASE_UPDATE);
//        param.put("type", Config.MERCHANT_ENTER_TYPE_IMAGE_UPDATE);
        param.put("app_id", Config.APP_ID);
        param.put("nonce_str", "TzaETzfe4lgL2hOmfbx9XEttAEuZSuiE");
        // TODO wuming 16/12/6 下午2:54 增加支持对公帐户
        param.put("is_company", "1"); // "1" 对公帐户 "0" 对私帐户

        // 如果是更新、或者进件照片信息需要传merchant_no,
        if (!Config.MERCHANT_ENTER_TYPE_BASE_ADD.equals(param.get("type"))){
            param.put("merchant_no", "88795891822101667840"); // merchant_no为调用type=0时返回的商户号
        }

        // 设置商户图片信息
        if(Config.MERCHANT_ENTER_TYPE_IMAGE_UPDATE.equals(param.get("type"))){
            String filePath = "/Users/wuming/Pictures/cat.jpg";
            String base64Str =  ImageUtil.encodeBase64File(filePath);
            // enter merchant photo
            param.put("bank_front_image", base64Str); // 银行卡正面
            if ("1".equals(param.get("is_company"))){
                // 对公帐户需要提供营业执照正面照片
                param.put("biz_license_image", base64Str); // 营业执照照片
            } else {
                // 对私帐户需要提交身份证正反面照片、手持身份证照片和银行卡正面照片
                param.put("id_front_image", base64Str); // 身份证正面
                param.put("id_back_image", base64Str); // 身份证反而
                param.put("id_hand_image", base64Str); // 手机身份证
            }
        } else {
            // enter merchant info
            param.put("name", "无名Test"); // 请填写真实姓名,要与身份证上一致
            param.put("mobile", "15280173582"); // 商户联系方式,提交认证后不可修改
            // 商户银行信息
            param.put("bank_name", "招商银行"); // 清算银行名称
            param.put("bank_account", "6225885866688888"); // 清算银行帐户
            if ("1".equals(param.get("is_company"))){
                // 对公帐户需要提供营业执照号和组织机构码
                param.put("biz_license_no", "11223344"); // 营业执照号
                param.put("org_code", "55332211"); // 组织机构码
            } else {
                // 对私帐户需要提交开户的身份证号码和预留手机号
                param.put("id_number", "411528199901106668"); // 身份证号码
                param.put("bank_mobile", "15280173582"); // 银行预留手机号
            }
            param.put("bank_code", "1313"); // 清算银行代码
            param.put("bank_subbranch_no", "308331012167"); // 清算银行联行号 非必填
            param.put("bank_subbranch", "杭州招商银行城西支行"); // 清算银行支行
        }

        // 商户构建请求参数
        System.out.println(">>>>post sign map: " + param);
        String xmlStr = PayUtil.buildRequestXml(param);
        System.out.println(">>>>post xmlStr: " +  xmlStr);
        String resText = HttpsUtil.post(Config.PAY_MERCHANT_ENTER_URL, xmlStr, Config.CHARSET);
        System.out.println("<<<<merchant resText: " + resText);
        // 验签后取得支付数据
        if (resText != null) {
            try {
                SortedMap<String, String> result = XmlUtil.doXMLParse(resText);
                System.out.println("<<<<merchant res map: " + result);
                if (PayUtil.verifyFulaParam(result)) {
                    System.out.println("<<<<merchant res verrify success-----------------");

                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
