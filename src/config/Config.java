package config;

/**
 * Created by wuming on 16/10/20.
 * 调起支付前,商户需要与我方交换公钥
 */
public class Config {

    /**
     * 测试专用商户公私钥
     */
    public static final String MCH_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDS8E5klNSVTk8wB9aGDI2RaNj1dnxN4E5JZpOjPZwe4K6GHFKwzMyYikEd4yyclxSieRIX2UgXKoMsclpjKowev3//Fm2uwRYZ7ptZmZzMkCkCLh7peF70xOzEwbzDbD+iPDftmjJi/cOYukMpqcETJz+7gDRyM6XTbICsROvRMwIDAQAB";

    public static final String MCH_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANLwTmSU1JVOTzAH1oYMjZFo2PV2fE3gTklmk6M9nB7groYcUrDMzJiKQR3jLJyXFKJ5EhfZSBcqgyxyWmMqjB6/f/8Wba7BFhnum1mZnMyQKQIuHul4XvTE7MTBvMNsP6I8N+2aMmL9w5i6QympwRMnP7uANHIzpdNsgKxE69EzAgMBAAECgYBGKNOKNM54UrGYAiNiNq6nnL389snlhSPE2i9k7ksNIiIqTSVb1OKAIgwCitr9wmqEl8xizkxiGCvFDsvti+tKfB0oBOqdlcb0KDtfas8F0QewBezw8cJ6NnGRWyaJOgygnKev6lll3xTUEm9p5QLKMIQ0RuCpFUyfvqp6YTn/4QJBAPMq9r2pUSNJ5TghyRXbM77lfAop5mL4uYKt1ybXT/+vBvuMxkdIs2xJk/XllC7JqU+AL4w35ubirYWbiU4oyckCQQDeEfK8j9D2mdj6CHLSvhb/GG8UU3LbS7ylQIEUi0BCbavkDOHSQGD+dF4sKrRExUxxhal3HHcfsQa3uk4RjsEbAkEAmlwRmT/cE6ya51D7Fva5GFsQrFsAtp7xE/VKeIuBausuYYxxaVrLGthyJkwADttQsPjMNhRebP5D7GZZeNYHkQJAM7Dr1raHRo+jMPg8eg+jXLe8S3ftOVyUycaNBVIwoAXVSB0zh3RS34gIz7EVCxj95ULeoooutGLJlIS6XV3kVwJAXLi7xqJkOGm8QMiy9v+EPvKF+uPlDoPSTeKUhgjZAw0pXTXdILqXf6DSjcJG+ruJjUTVhBH3+7XRQcwPcOKO0w==";

    /**
     * fula-pay 测试用公钥
     */
    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDkTf0Tw9kP7c5+K1Xhq0eIw3wo1TdIDjcB216f6R7kxUQ4GedsUzNFX3t2N/9rT6hIllsuBi7B7AIGYF2cphq5edYhTrjsZyFOTIM/0Nz4KfLL+hrRMfXUJ6yxD5t4nJv9shAhUi+mYS4hJ8k+kQ0o5cR/SaxNzzgxKAdFVyRCWwIDAQAB";

    /**
     * 字符集编码 无需修改
     */
    public static final String CHARSET = "UTF-8";

    /**
     * 付啦统一下单链接 无需修改
     */
//    public static final String UNIFIED_ORDER_URL = "https://api.fulapay.com/pay/unifiedOrder";
    public static final String UNIFIED_ORDER_URL = "http://121.43.161.81:8888/pay/unifiedOrder";

    public static final String FULA_ALIAY_QRCODE_SERVICE = "fula.alipay.qrcode";
    public static final String FULA_ALIPAY_SCAN_SERVICE = "fula.alipay.scan";
    public static final String FULA_WXPAY_SCAN_SERVICE = "fula.alipay.scan";

}
