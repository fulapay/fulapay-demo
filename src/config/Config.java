package config;

/**
 * Created by wuming on 16/10/20.
 * 调起支付前,商户需要与我方交换公钥
 */
public class Config {

    /**
     * 测试专用app_id
     */
    public static final String APP_ID = "1000015";
    /**
     * 测试专用mch_id
     */
    public static final String MCH_ID = "88793741592119799888";

    /**
     * 测试专用商户公私钥
     * 测试用商户私钥文件，请根据平台语言选择合适的编码格式，商户源公私钥原文件在项目目录下的cert目录下
     */
    // openssl工具生成商户公钥
    public static final String MCH_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDS8E5klNSVTk8wB9aGDI2RaNj1dnxN4E5JZpOjPZwe4K6GHFKwzMyYikEd4yyclxSieRIX2UgXKoMsclpjKowev3//Fm2uwRYZ7ptZmZzMkCkCLh7peF70xOzEwbzDbD+iPDftmjJi/cOYukMpqcETJz+7gDRyM6XTbICsROvRMwIDAQAB";
    // openssl工具生成商户私钥，默认pkcs1编码
    public static final String MCH_PRIVATE_KEY_ORI = "MIICXAIBAAKBgQDS8E5klNSVTk8wB9aGDI2RaNj1dnxN4E5JZpOjPZwe4K6GHFKwzMyYikEd4yyclxSieRIX2UgXKoMsclpjKowev3//Fm2uwRYZ7ptZmZzMkCkCLh7peF70xOzEwbzDbD+iPDftmjJi/cOYukMpqcETJz+7gDRyM6XTbICsROvRMwIDAQABAoGARijTijTOeFKxmAIjYjaup5y9/PbJ5YUjxNovZO5LDSIiKk0lW9TigCIMAora/cJqhJfMYs5MYhgrxQ7L7YvrSnwdKATqnZXG9Cg7X2rPBdEHsAXs8PHCejZxkVsmiToMoJynr+pZZd8U1BJvaeUCyjCENEbgqRVMn76qemE5/+ECQQDzKva9qVEjSeU4IckV2zO+5XwKKeZi+LmCrdcm10//rwb7jMZHSLNsSZP15ZQuyalPgC+MN+bm4q2Fm4lOKMnJAkEA3hHyvI/Q9pnY+ghy0r4W/xhvFFNy20u8pUCBFItAQm2r5Azh0kBg/nReLCq0RMVMcYWpdxx3H7EGt7pOEY7BGwJBAJpcEZk/3BOsmudQ+xb2uRhbEKxbALae8RP1SniLgWrrLmGMcWlayxrYciZMAA7bULD4zDYUXmz+Q+xmWXjWB5ECQDOw69a2h0aPozD4PHoPo1y3vEt37TlclMnGjQVSMKAF1UgdM4d0Ut+ICM+xFQsY/eVC3qKKLrRiyZSEul1d5FcCQFy4u8aiZDhpvEDIsvb/hD7yhfrj5Q6D0k3ilIYI2QMNKV013SC6l3+g0o3CRvq7iY1E1YQR9/u10UHMD3DijtM=";
    // 商户私钥pkcs8格式 java平台更方便全用pkcs8格式的，demo中使用此文件
    public static final String MCH_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANLwTmSU1JVOTzAH1oYMjZFo2PV2fE3gTklmk6M9nB7groYcUrDMzJiKQR3jLJyXFKJ5EhfZSBcqgyxyWmMqjB6/f/8Wba7BFhnum1mZnMyQKQIuHul4XvTE7MTBvMNsP6I8N+2aMmL9w5i6QympwRMnP7uANHIzpdNsgKxE69EzAgMBAAECgYBGKNOKNM54UrGYAiNiNq6nnL389snlhSPE2i9k7ksNIiIqTSVb1OKAIgwCitr9wmqEl8xizkxiGCvFDsvti+tKfB0oBOqdlcb0KDtfas8F0QewBezw8cJ6NnGRWyaJOgygnKev6lll3xTUEm9p5QLKMIQ0RuCpFUyfvqp6YTn/4QJBAPMq9r2pUSNJ5TghyRXbM77lfAop5mL4uYKt1ybXT/+vBvuMxkdIs2xJk/XllC7JqU+AL4w35ubirYWbiU4oyckCQQDeEfK8j9D2mdj6CHLSvhb/GG8UU3LbS7ylQIEUi0BCbavkDOHSQGD+dF4sKrRExUxxhal3HHcfsQa3uk4RjsEbAkEAmlwRmT/cE6ya51D7Fva5GFsQrFsAtp7xE/VKeIuBausuYYxxaVrLGthyJkwADttQsPjMNhRebP5D7GZZeNYHkQJAM7Dr1raHRo+jMPg8eg+jXLe8S3ftOVyUycaNBVIwoAXVSB0zh3RS34gIz7EVCxj95ULeoooutGLJlIS6XV3kVwJAXLi7xqJkOGm8QMiy9v+EPvKF+uPlDoPSTeKUhgjZAw0pXTXdILqXf6DSjcJG+ruJjUTVhBH3+7XRQcwPcOKO0w==";

    /**
     * fula-pay 测试用公钥
     */
    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDkTf0Tw9kP7c5+K1Xhq0eIw3wo1TdIDjcB216f6R7kxUQ4GedsUzNFX3t2N/9rT6hIllsuBi7B7AIGYF2cphq5edYhTrjsZyFOTIM/0Nz4KfLL+hrRMfXUJ6yxD5t4nJv9shAhUi+mYS4hJ8k+kQ0o5cR/SaxNzzgxKAdFVyRCWwIDAQAB";
    // 字符集编码 无需修改 目前仅支持UTF-8
    public static final String CHARSET = "UTF-8";
    // 签名算法 无需修改 目前仅支持RSA
    public static final String SIGN_TYPE = "RSA";

    /**
     * 付啦支付测试环境联调域名及各接口请求地址
     * https://sandapi.fulapay.com
     */
    // 付啦统一支付请求地址
    public static final String UNIFIED_PAY_URL = "https://sandapi.fulapay.com/pay/unified/url";
    // 付啦统一下单接口请求地址
    public static final String UNIFIED_ORDER_URL = "https://sandapi.fulapay.com/pay/unifiedOrder";
    // 付啦订单结果查询接口请求地址
    public static final String QUERY_ORDER_URL = "https://sandapi.fulapay.com/pay/query";
    // 商户进件接口请求地址
    public static final String PAY_MERCHANT_ENTER_URL = "https://sandapi.fulapay.com/merchant/enter";
    // 用户后台接收付啦订单支付结果异步通知url，由用户自行定义，该接口为付啦系统异步主动通知用户接口，用户无需主动调用
    public static final String NOTIFY_URL = "https://sandapi.fulapay.com/mchPay/notify";


    /**
     * UNIFIED_PAY_URL
     * 付啦统一支付service 常量声明
     */
    public static final String PAY_UNIFIED_PAY = "pay.unified.pay"; // 统一支付链接
    public static final String PAY_UNIFIED_QRCODE = "pay.unified.qrcode"; // 统一支付二维码链接

    /**
     * UNIFIED_ORDER_URL
     * 付啦支付统一下单接口service 常量声明
     */
    // 统一下单
    public static final String PAY_WXPAY_QRCODE = "pay.wxpay.qrcode";
    public static final String PAY_WXPAY_SCAN = "pay.wxpay.scan";
    public static final String PAY_WXPAY_JS = "pay.wxpay.js";

    public static final String PAY_ALIPAY_QRCODE = "pay.alipay.qrcode";
    public static final String PAY_ALIPAY_SCAN = "pay.alipay.scan";
    public static final String PAY_ALIPAY_JS = "pay.alipay.js";

    /**
     * QUERY_ORDER_URL
     * 订单查询接口service 常量声明
     */
    public static final String PAY_TRADE_QUERY = "pay.trade.query";

    /**
     * TODO 开发中
     * 订单退款
      */
    public static final String PAY_TRADE_REFUND = "pay.trade.refund";

    /**
     * PAY_MERCHANT_ENTER_URL
     * 商户进件接口service 常量声明
      */
    public static final String PAY_MERCHANT_ENTER = "pay.merchant.enter";

    /**
     * PAY_MERCHANT_ENTER_URL
     * 商户进件接口type 常量声明
     * "0" 进件基本资料
     * "1" 修改基本资料
     * "2" 进件、修改图片信息
     */
    public static final String MERCHANT_ENTER_TYPE_BASE_ADD = "0";
    public static final String MERCHANT_ENTER_TYPE_BASE_UPDATE = "1";
    public static final String MERCHANT_ENTER_TYPE_IMAGE_UPDATE = "2";


    /********************************************fula 接口线上请求url*****************************************/
    /**
     * 付啦统一下单线上环境链接 无需修改
     */
    // 付啦统一支付请求地址
//    public static final String UNIFIED_PAY_URL = "https://api.fulapay.com/pay/unified/url";
//    // 付啦统一下单接口请求地址
//    public static final String UNIFIED_ORDER_URL = "https://api.fulapay.com/pay/unifiedOrder";
//    // 付啦订单结果查询接口请求地址
//    public static final String QUERY_ORDER_URL = "https://api.fulapay.com/pay/query";
//    // 商户进件接口请求地址
//    public static final String PAY_MERCHANT_ENTER_URL = "https://api.fulapay.com/merchant/enter";
//    // 用户后台接收付啦订单支付结果异步通知url，由用户自行定义，该接口为付啦系统异步主动通知用户接口，用户无需主动调用
//    public static final String NOTIFY_URL = "https://api.fulapay.com/mchPay/notify";
}
