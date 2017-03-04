using System;
using System.Collections.Generic;
using System.IO;
using System.IO.Compression;
using System.Linq;
using System.Net;
using System.Net.Security;
using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using System.Web;
using System.Xml;


#region 通道返回参数

public class 通道返回参数
{
    public 通道返回参数()
    {
        Status = 0;//0:失败；1：成功
    }

    public int Status { get; set; }

    public string Msg { get; set; }

    public string ResCode { get; set; }

    public string QrCode { get; set; }
}

#endregion

    public class FulaPayType 
    {
        

        #region Config

        protected int Time_Out = 10 * 1000;

        private string _APP_ID = "1000015";

        /// <summary>
        /// 专用app_id
        /// </summary>
        public  String APP_ID
        {
            get { return _APP_ID; }
            set { _APP_ID = value; }
        }

        /// <summary>
        /// 商户号
        /// </summary>
        private String _MCH_ID = "88793741592119799888";

        public  string MCH_ID
        {
            get { return _MCH_ID; }
            set { _MCH_ID = value; }
        }

        private String _MOBILE = "15280533697";

        /// <summary>
        /// 商户号对应的手机号
        /// </summary>
        public  string MOBILE
        {
            get { return _MOBILE; }
            set { _MOBILE = value; }
        }


        private String _MCH_PUBLIC_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDS8E5klNSVTk8wB9aGDI2RaNj1dnxN4E5JZpOjPZwe4K6GHFKwzMyYikEd4yyclxSieRIX2UgXKoMsclpjKowev3//Fm2uwRYZ7ptZmZzMkCkCLh7peF70xOzEwbzDbD+iPDftmjJi/cOYukMpqcETJz+7gDRyM6XTbICsROvRMwIDAQAB";

        /// <summary>
        /// 商户公私钥,请根据平台语言选择合适的编码格式，商户源公私钥原文件在项目目录下的cert目录下,openssl工具生成商户公钥
        /// </summary>
        public  string MCH_PUBLIC_KEY
        {
            get { return _MCH_PUBLIC_KEY; }
            set { _MCH_PUBLIC_KEY = value; }
        }


        private string _MCH_PRIVATE_KEY =
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANLwTmSU1JVOTzAH1oYMjZFo2PV2fE3gTklmk6M9nB7groYcUrDMzJiKQR3jLJyXFKJ5EhfZSBcqgyxyWmMqjB6/f/8Wba7BFhnum1mZnMyQKQIuHul4XvTE7MTBvMNsP6I8N+2aMmL9w5i6QympwRMnP7uANHIzpdNsgKxE69EzAgMBAAECgYBGKNOKNM54UrGYAiNiNq6nnL389snlhSPE2i9k7ksNIiIqTSVb1OKAIgwCitr9wmqEl8xizkxiGCvFDsvti+tKfB0oBOqdlcb0KDtfas8F0QewBezw8cJ6NnGRWyaJOgygnKev6lll3xTUEm9p5QLKMIQ0RuCpFUyfvqp6YTn/4QJBAPMq9r2pUSNJ5TghyRXbM77lfAop5mL4uYKt1ybXT/+vBvuMxkdIs2xJk/XllC7JqU+AL4w35ubirYWbiU4oyckCQQDeEfK8j9D2mdj6CHLSvhb/GG8UU3LbS7ylQIEUi0BCbavkDOHSQGD+dF4sKrRExUxxhal3HHcfsQa3uk4RjsEbAkEAmlwRmT/cE6ya51D7Fva5GFsQrFsAtp7xE/VKeIuBausuYYxxaVrLGthyJkwADttQsPjMNhRebP5D7GZZeNYHkQJAM7Dr1raHRo+jMPg8eg+jXLe8S3ftOVyUycaNBVIwoAXVSB0zh3RS34gIz7EVCxj95ULeoooutGLJlIS6XV3kVwJAXLi7xqJkOGm8QMiy9v+EPvKF+uPlDoPSTeKUhgjZAw0pXTXdILqXf6DSjcJG+ruJjUTVhBH3+7XRQcwPcOKO0w==";


        /// <summary>
        /// 商户私钥pkcs8格式 java平台更方便使用pkcs8格式的(cert目录下有测试环境公私钥源文件，请根据开发语言自行选择)
        /// </summary>
        public  string MCH_PRIVATE_KEY
        {
            get { return _MCH_PRIVATE_KEY; }
            set { _MCH_PRIVATE_KEY = value; }
        }



        private string _PUBLIC_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDkTf0Tw9kP7c5+K1Xhq0eIw3wo1TdIDjcB216f6R7kxUQ4GedsUzNFX3t2N/9rT6hIllsuBi7B7AIGYF2cphq5edYhTrjsZyFOTIM/0Nz4KfLL+hrRMfXUJ6yxD5t4nJv9shAhUi+mYS4hJ8k+kQ0o5cR/SaxNzzgxKAdFVyRCWwIDAQAB";

        /// <summary>
        /// 公钥
        /// </summary>
        public  string PUBLIC_KEY
        {
            get { return _PUBLIC_KEY; }
            set { _PUBLIC_KEY = value; }
        }

        private String _CHARSET = "UTF-8";

        /// <summary>
        /// 字符集编码
        /// </summary>
        public  string CHARSET
        {
            get { return _CHARSET; }
            set { _CHARSET = value; }
        }


        // 签名算法 无需修改 目前仅支持RSA
        private String _SIGN_TYPE = "RSA";

        public  string SIGN_TYPE
        {
            get { return _SIGN_TYPE; }
            set { _SIGN_TYPE = value; }
        }

        private string _统一支付接口请求地址 = "https://sandapi.fulapay.com/pay/unified/url";

        /// <summary>
        /// 付啦：UNIFIED_PAY_URL
        /// </summary>
        public  string 统一支付接口请求地址
        {
            get { return _统一支付接口请求地址; }
            set { _统一支付接口请求地址 = value; }
        }

        private string _统一下单接口请求地址 = "https://sandapi.fulapay.com/pay/unifiedOrder";

        /// <summary>
        /// 付啦：UNIFIED_ORDER_URL
        /// </summary>
        public  string 统一下单接口请求地址
        {
            get { return _统一下单接口请求地址; }
            set { _统一下单接口请求地址 = value; }
        }


        private string _订单结果查询接口请求地址 = "https://sandapi.fulapay.com/trade/query";


        /// <summary>
        /// 付啦：TRADE_QUERY_URL
        /// </summary>
        public  string 订单结果查询接口请求地址
        {
            get { return _订单结果查询接口请求地址; }
            set { _订单结果查询接口请求地址 = value; }
        }

        private string _订单结果查询接口请求页面 = "https://sandapi.fulapay.com/trade/query/page";

        /// <summary>
        /// 付啦：TRADE_QUERY_PAGE_URL
        /// </summary>
        public  string 订单结果查询接口请求页面
        {
            get { return _订单结果查询接口请求页面; }
            set { _订单结果查询接口请求页面 = value; }
        }


        private string _商户进件 = "https://sandapi.fulapay.com/merchant/enter";

        public  string 商户进件
        {
            get { return _商户进件; }
            set { _商户进件 = value; }
        }

        private string _商户进件接口查询 = "https://sandapi.fulapay.com/merchant/query";

        public string 商户进件接口查询
        {
            get { return _商户进件接口查询; }
            set { _商户进件接口查询 = value; }
        }

        private string _订单支付结果异步通知url = "https://sandapi.fulapay.com/mchPay/notify";

        /// <summary>
        /// 用户后台接收付啦订单支付结果异步通知url，由用户自行定义，该接口为支付系统异步主动通知用户接口，用户无需主动调用
        /// </summary>
        public  string 订单支付结果异步通知url
        {
            get { return _订单支付结果异步通知url; }
            set { _订单支付结果异步通知url = value; }
        }

        private string _RETURN_URL = "https://sandapi.fulapay.com/mchPay/return";

        /// <summary>
        /// 支付宝或支付 JS支付结果同步跳转URL，此链接仅为逻辑跳转，具体支付结果及商户订单状态修改根据notify结果处理
        /// </summary>
        public  string RETURN_URL
        {
            get { return _RETURN_URL; }
            set { _RETURN_URL = value; }
        }

        /**
         * UNIFIED_PAY_URL (service参数仅能为以下数据)
         * 付啦统一支付service 常量声明
         */
        private string _PAY_UNIFIED_PAY = "pay.unified.pay";


        /// <summary>
        /// 统一支付链接
        /// </summary>
        public  String PAY_UNIFIED_PAY
        {
            get { return _PAY_UNIFIED_PAY; }
            set { _PAY_UNIFIED_PAY = value; }
        }

        private string _PAY_UNIFIED_QRCODE = "pay.unified.qrcode"; // 

        /// <summary>
        /// 统一支付二维码链接
        /// </summary>
        public  String PAY_UNIFIED_QRCODE
        {
            get { return _PAY_UNIFIED_QRCODE; }
            set { _PAY_UNIFIED_QRCODE = value; }
        }

        /**
         * JS_PAY_URL (service参数仅能为以下数据)
         * 支付宝或微信 JS支付为同步调起支付
         */
        private String _PAY_WXPAY_JS = "pay.wxpay.js";
        public  string PAY_WXPAY_JS
        { get { return _PAY_WXPAY_JS; } set { _PAY_WXPAY_JS = value; } }

        private String _PAY_ALIPAY_JS = "pay.alipay.js";
        public  string PAY_ALIPAY_JS
        {
            get { return _PAY_ALIPAY_JS; }
            set { _PAY_ALIPAY_JS = value; }
        }

        /**
         * UNIFIED_ORDER_URL (service参数仅能为以下数据)
         * 付啦支付统一下单接口service 常量声明
         */
        // 统一下单
        private String _PAY_WXPAY_QRCODE = "pay.wxpay.qrcode";

        public  string PAY_WXPAY_QRCODE
        {
            get { return _PAY_WXPAY_QRCODE; }
            set { _PAY_WXPAY_QRCODE = value; }
        }

        private String _PAY_WXPAY_SCAN = "pay.wxpay.scan";

        public  string PAY_WXPAY_SCAN
        {
            get { return _PAY_WXPAY_SCAN; }
            set { _PAY_WXPAY_SCAN = value; }
        }


        private String _PAY_ALIPAY_QRCODE = "pay.alipay.qrcode";

        public  string PAY_ALIPAY_QRCODE
        {
            get { return _PAY_ALIPAY_QRCODE; }
            set { _PAY_ALIPAY_QRCODE = value; }
        }


        private String _PAY_ALIPAY_SCAN = "pay.alipay.scan";

        public  string PAY_ALIPAY_SCAN
        {
            get { return _PAY_ALIPAY_SCAN; }
            set { _PAY_ALIPAY_SCAN = value; }
        }



        /**
         * QUERY_ORDER_URL
         * 订单查询接口service 常量声明
         */
        private String _TRADE_QUERY = "trade.query";

        public  string TRADE_QUERY
        {
            get { return _TRADE_QUERY; }
            set { _TRADE_QUERY = value; }
        }

        private String _TRADE_QUERY_PAGE = "trade.query.page";

        public  string TRADE_QUERY_PAGE
        {
            get { return _TRADE_QUERY_PAGE; }
            set { _TRADE_QUERY_PAGE = value; }
        }



        /**
         * PAY_MERCHANT_ENTER_URL (以下service仅限调用商户进件接口使用)
         * 商户进件接口service 常量声明
          */
        private String _PAY_MERCHANT_ENTER = "pay.merchant.enter";

        public  string PAY_MERCHANT_ENTER
        {
            get { return _PAY_MERCHANT_ENTER; }
            set { _PAY_MERCHANT_ENTER = value; }
        }

        private String _PAY_MERCHANT_QUERY = "pay.merchant.query";

        public  string PAY_MERCHANT_QUERY
        {
            get { return _PAY_MERCHANT_QUERY; }
            set { _PAY_MERCHANT_QUERY = value; }
        }



        /**
         * 商户进件接口type 常量声明
         * "0" 进件基本资料
         * "1" 修改基本资料
         * "2" 进件、修改图片信息
         */
        public String MERCHANT_ENTER_TYPE_BASE_ADD = "0";
        public String MERCHANT_ENTER_TYPE_BASE_UPDATE = "1";
        public String MERCHANT_ENTER_TYPE_IMAGE_UPDATE = "2";


        /********************************************fula 接口线上环境*****************************************/
        /**
         * 付啦开放平台接口线上域名
         * 开发者调试环境测通后并且走完开发流程即可使用线上域名更换上面开发环境域名进行正式交易
         */
        protected String ONLINE_DOMAIN = "https://api.fulapay.com";

        #endregion



        public FulaPayType()
        {

        }

        public  bool Verify(TopDictionary paramers)
        {
            // 第一步：把字典按Key的字母顺序排序
            IDictionary<string, string> sortedParams = new SortedDictionary<string, string>(paramers, StringComparer.Ordinal);

            // 第二步：把所有参数名和参数值串在一起
            StringBuilder query = new StringBuilder();

            bool isVerify = false;
            string signStr = "";

            foreach (KeyValuePair<string, string> kv in sortedParams)
            {
                if (kv.Key.ToLower() == "sign")
                {
                    if (!string.IsNullOrEmpty(kv.Value))
                    {
                        signStr = kv.Value;
                        isVerify = true;
                    }
                    continue;
                }
                if (query.Length > 0)
                    query.Append("&");

                if (!string.IsNullOrEmpty(kv.Key) && !string.IsNullOrEmpty(kv.Value))
                {
                    query.Append(kv.Key).Append("=").Append(kv.Value);
                }
            }
            if (!isVerify)
                return true;
            return verify(query.ToString(), signStr, PUBLIC_KEY);
        }

        /// <summary>
        /// 验证签名
        /// </summary>
        /// <param name="content">需要验证的内容</param>
        /// <param name="signedString">签名结果</param>
        /// <param name="publicKey">公钥</param>
        /// <returns></returns>
        public static bool verify(string content, string signedString, string publicKey)
        {
            bool result = false;

            byte[] Data = Encoding.UTF8.GetBytes(content);
            byte[] data = Convert.FromBase64String(signedString);
            RSAParameters paraPub = ConvertFromPublicKey(publicKey);
            RSACryptoServiceProvider rsaPub = new RSACryptoServiceProvider();
            rsaPub.ImportParameters(paraPub);

            SHA1 sh = new SHA1CryptoServiceProvider();
            result = rsaPub.VerifyData(Data, sh, data);
            return result;
        }

        private static RSAParameters ConvertFromPublicKey(string pemFileConent)
        {

            byte[] keyData = Convert.FromBase64String(pemFileConent);
            if (keyData.Length < 162)
            {
                throw new ArgumentException("pem file content is incorrect.");
            }
            byte[] pemModulus = new byte[128];
            byte[] pemPublicExponent = new byte[3];
            Array.Copy(keyData, 29, pemModulus, 0, 128);
            Array.Copy(keyData, 159, pemPublicExponent, 0, 3);
            RSAParameters para = new RSAParameters();
            para.Modulus = pemModulus;
            para.Exponent = pemPublicExponent;
            return para;
        }

        public  string DoPost(string url, TopDictionary addParams)
        {
            TopDictionary paramers = new TopDictionary();

            paramers.Add("app_id", APP_ID);
            paramers.Add("charset", CHARSET);
            paramers.Add("sign_type", SIGN_TYPE);

            paramers.Add("notify_url", 订单支付结果异步通知url);
            paramers.Add("nonce_str", Guid.NewGuid().ToString().Replace("-", ""));
            paramers.AddAll(addParams);

            // 如果 service为fula.xxx.scan 扫码支付需要authCode 参数（支付宝或者微信扫码支付的上显示的code）
            //if(_Default.Biz.PAY_WXPAY_SCAN.IndexOf(service)!=-1 || _Default.Biz.PAY_WXPAY_SCAN.IndexOf(service)!=-1){
            //    paramers.Add("auth_code", auth_code);
            //}

            //V.Utils.HttpRequest request= new V.Utils.HttpRequest();
            //request.CharacterSet = "utf-8";

            //ErrorLog.GetInstance().Write("开始提交");

            //ErrorLog.GetInstance().Write("提交数据：" + Serialize(paramers));

            string val = doPost(url, Serialize(paramers));

            //ErrorLog.GetInstance().Write("提交返回：" + url + "," + val);

            //ErrorLog.GetInstance().Write("提交结束");

            return val;
        }

        #region HttpUtil


        private Encoding GetResponseEncoding(HttpWebResponse rsp)
        {
            string charset = rsp.CharacterSet;
            if (string.IsNullOrEmpty(charset))
            {
                charset = Constants.CHARSET_UTF8;
            }
            return Encoding.GetEncoding(charset);
        }


        /// <summary>
        /// 执行HTTP POST请求。
        /// </summary>
        /// <param name="url">请求地址</param>
        /// <param name="data">请求数据</param>
        /// <returns>HTTP响应</returns>
        protected string doPost(string url, string data)
        {
            HttpWebRequest req = GetWebRequest(url, "POST");

            //req.ContentType = "application/json;charset=utf-8";
            req.ContentType = "text/html";
            req.UserAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)";
            byte[] postData = Encoding.UTF8.GetBytes(data);
            System.IO.Stream reqStream = req.GetRequestStream();
            reqStream.Write(postData, 0, postData.Length);
            reqStream.Close();

            HttpWebResponse rsp = (HttpWebResponse)req.GetResponse();

            Encoding encoding = GetResponseEncoding(rsp);
            return GetResponseAsString(rsp, encoding);
        }

        private static bool TrustAllValidationCallback(object sender, X509Certificate certificate, X509Chain chain,
                                                   SslPolicyErrors errors)
        {
            return true; // 忽略SSL证书检查
        }

        protected HttpWebRequest GetWebRequest(string url, string method)
        {
            HttpWebRequest req = null;
            if (url.StartsWith("https", StringComparison.OrdinalIgnoreCase))
            {
                //if (1==1)
                {
                    ServicePointManager.ServerCertificateValidationCallback =
                        new RemoteCertificateValidationCallback(TrustAllValidationCallback);
                }
                req = (HttpWebRequest)WebRequest.CreateDefault(new Uri(url));
            }
            else
            {
                req = (HttpWebRequest)WebRequest.Create(url);
            }



            req.ServicePoint.Expect100Continue = false;
            req.Method = method;
            req.KeepAlive = true;
            req.Timeout = Time_Out;
            req.ReadWriteTimeout = Time_Out;

            return req;
        }

        /// <summary>
        /// 把响应流转换为文本。
        /// </summary>
        /// <param name="rsp">响应流对象</param>
        /// <param name="encoding">编码方式</param>
        /// <returns>响应文本</returns>
        protected string GetResponseAsString(HttpWebResponse rsp, Encoding encoding)
        {
            Stream stream = null;
            StreamReader reader = null;

            try
            {
                // 以字符流的方式读取HTTP响应
                stream = rsp.GetResponseStream();
                if (Constants.CONTENT_ENCODING_GZIP.Equals(rsp.ContentEncoding, StringComparison.OrdinalIgnoreCase))
                {
                    stream = new GZipStream(stream, CompressionMode.Decompress);
                }
                reader = new StreamReader(stream, encoding);
                return reader.ReadToEnd();
            }
            finally
            {
                // 释放资源
                if (reader != null) reader.Close();
                if (stream != null) stream.Close();
                if (rsp != null) rsp.Close();
            }
        }

        #endregion

        protected  string SignTopRequest(TopDictionary paramers)
        {
            // 第一步：把字典按Key的字母顺序排序
            IDictionary<string, string> sortedParams = new SortedDictionary<string, string>(paramers, StringComparer.Ordinal);

            // 第二步：把所有参数名和参数值串在一起
            StringBuilder query = new StringBuilder();

            foreach (KeyValuePair<string, string> kv in sortedParams)
            {
                if (query.Length > 0)
                    query.Append("&");

                if (!string.IsNullOrEmpty(kv.Key) && !string.IsNullOrEmpty(kv.Value))
                {
                    query.Append(kv.Key).Append("=").Append(kv.Value);
                }
            }

            return RSAFromPkcs8.sign(query.ToString(), MCH_PRIVATE_KEY);

        }



        protected  string Serialize(TopDictionary paramers)
        {
            StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml>");

            foreach (KeyValuePair<string, string> kv in paramers)
            {
                sb.Append("<" + kv.Key + ">");
                sb.Append(kv.Value);
                sb.Append("</" + kv.Key + ">");
            }
            sb.Append("<sign>" + SignTopRequest(paramers) + "</sign>");
            sb.Append("</xml>");

            return sb.ToString();
        }
    }

public sealed class Constants
{
    public const string CHARSET_UTF8 = "utf-8";

    public const string DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public const string DATE_TIME_MS_FORMAT = "yyyy-MM-dd HH:mm:ss.fff";

    public const string SIGN_METHOD_MD5 = "md5";
    public const string SIGN_METHOD_HMAC = "hmac";

    public const string LOG_SPLIT = "^_^";
    public const string LOG_FILE_NAME = "topsdk.log";

    public const string ACCEPT_ENCODING = "Accept-Encoding";
    public const string CONTENT_ENCODING = "Content-Encoding";
    public const string CONTENT_ENCODING_GZIP = "gzip";

    public const string ERROR_RESPONSE = "error_response";
    public const string ERROR_CODE = "code";
    public const string ERROR_MSG = "msg";

    public const string SDK_VERSION = "top-sdk-net-20161114";
    public const string SDK_VERSION_CLUSTER = "top-sdk-net-cluster-20161114";

    public const string APP_KEY = "app_key";
    public const string FORMAT = "format";
    public const string METHOD = "method";
    public const string TIMESTAMP = "timestamp";
    public const string VERSION = "v";
    public const string SIGN = "sign";
    public const string SIGN_METHOD = "sign_method";
    public const string PARTNER_ID = "partner_id";
    public const string SESSION = "session";
    public const string FORMAT_XML = "xml";
    public const string FORMAT_JSON = "json";
    public const string SIMPLIFY = "simplify";
    public const string TARGET_APP_KEY = "target_app_key";

    public const string QM_ROOT_TAG_REQ = "request";
    public const string QM_ROOT_TAG_RSP = "response";
    public const string QM_CUSTOMER_ID = "customerId";
    public const string QM_CONTENT_TYPE = "text/xml;charset=utf-8";

    public const string CTYPE_DEFAULT = "application/octet-stream";
    public const string CTYPE_FORM_DATA = "application/x-www-form-urlencoded";
    public const string CTYPE_FILE_UPLOAD = "multipart/form-data";
    public const string CTYPE_TEXT_XML = "text/xml";
    public const string CTYPE_TEXT_PLAIN = "text/plain";
    public const string CTYPE_APP_JSON = "application/json";
    public const int READ_BUFFER_SIZE = 1024 * 4;
}


#region Dictionary

/// <summary>
/// 纯字符串字典结构。
/// </summary>
public class TopDictionary : Dictionary<string, string>
{
    public TopDictionary()
    {
    }

    public TopDictionary(IDictionary<string, string> dictionary)
        : base(dictionary)
    { }

    /// <summary>
    /// 添加一个新的键值对。空键或者空值的键值对将会被忽略。
    /// </summary>
    /// <param name="key">键名称</param>
    /// <param name="value">键对应的值，目前支持：string, int, long, double, bool, DateTime类型</param>
    public void Add(string key, object value)
    {
        string strValue;

        if (value == null)
        {
            strValue = null;
        }
        else if (value is string)
        {
            strValue = (string)value;
        }
        else if (value is Nullable<DateTime>)
        {
            Nullable<DateTime> dateTime = value as Nullable<DateTime>;
            strValue = dateTime.Value.ToString(Constants.DATE_TIME_FORMAT);
        }
        else if (value is Nullable<int>)
        {
            strValue = (value as Nullable<int>).Value.ToString();
        }
        else if (value is Nullable<long>)
        {
            strValue = (value as Nullable<long>).Value.ToString();
        }
        else if (value is Nullable<double>)
        {
            strValue = (value as Nullable<double>).Value.ToString();
        }
        else if (value is Nullable<bool>)
        {
            strValue = (value as Nullable<bool>).Value.ToString().ToLower();
        }
        else
        {
            strValue = value.ToString();
        }

        this.Add(key, strValue);
    }

    public new void Add(string key, string value)
    {
        if (!string.IsNullOrEmpty(key) && !string.IsNullOrEmpty(value))
        {
            base[key] = value;
        }
    }

    public void AddAll(IDictionary<string, string> dict)
    {
        if (dict != null && dict.Count > 0)
        {
            IEnumerator<KeyValuePair<string, string>> kvps = dict.GetEnumerator();
            while (kvps.MoveNext())
            {
                KeyValuePair<string, string> kvp = kvps.Current;
                Add(kvp.Key, kvp.Value);
            }
        }
    }



}

#endregion

#region RSA



public sealed class RSAFromPkcs8
{
    /// <summary>
    /// 签名
    /// </summary>
    /// <param name="content">需要签名的内容</param>
    /// <param name="privateKey">私钥</param>
    /// <returns></returns>
    public static string sign(string content, string privateKey)
    {
        byte[] Data = Encoding.UTF8.GetBytes(content);
        RSACryptoServiceProvider rsa = DecodePemPrivateKey(privateKey);
        SHA1 sh = new SHA1CryptoServiceProvider();

        byte[] signData = rsa.SignData(Data, sh);
        string val = Convert.ToBase64String(signData);

        return val;

    }
    /// <summary>
    /// 验证签名
    /// </summary>
    /// <param name="content">需要验证的内容</param>
    /// <param name="signedString">签名结果</param>
    /// <param name="publicKey">公钥</param>
    /// <returns></returns>
    public static bool verify(string content, string signedString, string publicKey)
    {
        bool result = false;

        byte[] Data = Encoding.UTF8.GetBytes(content);
        byte[] data = Convert.FromBase64String(signedString);
        RSAParameters paraPub = ConvertFromPublicKey(publicKey);
        RSACryptoServiceProvider rsaPub = new RSACryptoServiceProvider();
        rsaPub.ImportParameters(paraPub);

        SHA1 sh = new SHA1CryptoServiceProvider();
        result = rsaPub.VerifyData(Data, sh, data);
        return result;
    }

    /// <summary>
    /// 解析java生成的pem文件私钥
    /// </summary>
    /// <param name="pemstr"></param>
    /// <returns></returns>
    private static RSACryptoServiceProvider DecodePemPrivateKey(String pemstr)
    {
        byte[] pkcs8privatekey;
        pkcs8privatekey = Convert.FromBase64String(pemstr);
        if (pkcs8privatekey != null)
        {

            RSACryptoServiceProvider rsa = DecodePrivateKeyInfo(pkcs8privatekey);
            return rsa;
        }
        else
            return null;
    }

    private static RSACryptoServiceProvider DecodePrivateKeyInfo(byte[] pkcs8)
    {

        byte[] SeqOID = { 0x30, 0x0D, 0x06, 0x09, 0x2A, 0x86, 0x48, 0x86, 0xF7, 0x0D, 0x01, 0x01, 0x01, 0x05, 0x00 };
        byte[] seq = new byte[15];

        MemoryStream mem = new MemoryStream(pkcs8);
        int lenstream = (int)mem.Length;
        BinaryReader binr = new BinaryReader(mem);    //wrap Memory Stream with BinaryReader for easy reading
        byte bt = 0;
        ushort twobytes = 0;

        try
        {

            twobytes = binr.ReadUInt16();
            if (twobytes == 0x8130) //data read as little endian order (actual data order for Sequence is 30 81)
                binr.ReadByte();    //advance 1 byte
            else if (twobytes == 0x8230)
                binr.ReadInt16();   //advance 2 bytes
            else
                return null;


            bt = binr.ReadByte();
            if (bt != 0x02)
                return null;

            twobytes = binr.ReadUInt16();

            if (twobytes != 0x0001)
                return null;

            seq = binr.ReadBytes(15);       //read the Sequence OID
            if (!CompareBytearrays(seq, SeqOID))    //make sure Sequence for OID is correct
                return null;

            bt = binr.ReadByte();
            if (bt != 0x04) //expect an Octet string
                return null;

            bt = binr.ReadByte();       //read next byte, or next 2 bytes is  0x81 or 0x82; otherwise bt is the byte count
            if (bt == 0x81)
                binr.ReadByte();
            else
                if (bt == 0x82)
                    binr.ReadUInt16();
            //------ at this stage, the remaining sequence should be the RSA private key

            byte[] rsaprivkey = binr.ReadBytes((int)(lenstream - mem.Position));
            RSACryptoServiceProvider rsacsp = DecodeRSAPrivateKey(rsaprivkey);
            return rsacsp;
        }

        catch (Exception)
        {
            return null;
        }

        finally { binr.Close(); }

    }


    private static bool CompareBytearrays(byte[] a, byte[] b)
    {
        if (a.Length != b.Length)
            return false;
        int i = 0;
        foreach (byte c in a)
        {
            if (c != b[i])
                return false;
            i++;
        }
        return true;
    }

    private static RSACryptoServiceProvider DecodeRSAPrivateKey(byte[] privkey)
    {
        byte[] MODULUS, E, D, P, Q, DP, DQ, IQ;

        // ---------  Set up stream to decode the asn.1 encoded RSA private key  ------
        MemoryStream mem = new MemoryStream(privkey);
        BinaryReader binr = new BinaryReader(mem);    //wrap Memory Stream with BinaryReader for easy reading
        byte bt = 0;
        ushort twobytes = 0;
        int elems = 0;
        try
        {
            twobytes = binr.ReadUInt16();
            if (twobytes == 0x8130) //data read as little endian order (actual data order for Sequence is 30 81)
                binr.ReadByte();    //advance 1 byte
            else if (twobytes == 0x8230)
                binr.ReadInt16();   //advance 2 bytes
            else
                return null;

            twobytes = binr.ReadUInt16();
            if (twobytes != 0x0102) //version number
                return null;
            bt = binr.ReadByte();
            if (bt != 0x00)
                return null;


            //------  all private key components are Integer sequences ----
            elems = GetIntegerSize(binr);
            MODULUS = binr.ReadBytes(elems);

            elems = GetIntegerSize(binr);
            E = binr.ReadBytes(elems);

            elems = GetIntegerSize(binr);
            D = binr.ReadBytes(elems);

            elems = GetIntegerSize(binr);
            P = binr.ReadBytes(elems);

            elems = GetIntegerSize(binr);
            Q = binr.ReadBytes(elems);

            elems = GetIntegerSize(binr);
            DP = binr.ReadBytes(elems);

            elems = GetIntegerSize(binr);
            DQ = binr.ReadBytes(elems);

            elems = GetIntegerSize(binr);
            IQ = binr.ReadBytes(elems);

            // ------- create RSACryptoServiceProvider instance and initialize with public key -----
            RSACryptoServiceProvider RSA = new RSACryptoServiceProvider();
            RSAParameters RSAparams = new RSAParameters();
            RSAparams.Modulus = MODULUS;
            RSAparams.Exponent = E;
            RSAparams.D = D;
            RSAparams.P = P;
            RSAparams.Q = Q;
            RSAparams.DP = DP;
            RSAparams.DQ = DQ;
            RSAparams.InverseQ = IQ;
            RSA.ImportParameters(RSAparams);
            return RSA;
        }
        catch (Exception)
        {
            return null;
        }
        finally { binr.Close(); }
    }

    private static int GetIntegerSize(BinaryReader binr)
    {
        byte bt = 0;
        byte lowbyte = 0x00;
        byte highbyte = 0x00;
        int count = 0;
        bt = binr.ReadByte();
        if (bt != 0x02)     //expect integer
            return 0;
        bt = binr.ReadByte();

        if (bt == 0x81)
            count = binr.ReadByte();    // data size in next byte
        else
            if (bt == 0x82)
            {
                highbyte = binr.ReadByte(); // data size in next 2 bytes
                lowbyte = binr.ReadByte();
                byte[] modint = { lowbyte, highbyte, 0x00, 0x00 };
                count = BitConverter.ToInt32(modint, 0);
            }
            else
            {
                count = bt;     // we already have the data size
            }



        while (binr.ReadByte() == 0x00)
        {   //remove high order zeros in data
            count -= 1;
        }
        binr.BaseStream.Seek(-1, SeekOrigin.Current);       //last ReadByte wasn't a removed zero, so back up a byte
        return count;
    }

    #region 解析.net 生成的Pem
    private static RSAParameters ConvertFromPublicKey(string pemFileConent)
    {

        byte[] keyData = Convert.FromBase64String(pemFileConent);
        if (keyData.Length < 162)
        {
            throw new ArgumentException("pem file content is incorrect.");
        }
        byte[] pemModulus = new byte[128];
        byte[] pemPublicExponent = new byte[3];
        Array.Copy(keyData, 29, pemModulus, 0, 128);
        Array.Copy(keyData, 159, pemPublicExponent, 0, 3);
        RSAParameters para = new RSAParameters();
        para.Modulus = pemModulus;
        para.Exponent = pemPublicExponent;
        return para;
    }

    private static RSAParameters ConvertFromPrivateKey(string pemFileConent)
    {
        byte[] keyData = Convert.FromBase64String(pemFileConent);
        if (keyData.Length < 609)
        {
            throw new ArgumentException("pem file content is incorrect.");
        }

        int index = 11;
        byte[] pemModulus = new byte[128];
        Array.Copy(keyData, index, pemModulus, 0, 128);

        index += 128;
        index += 2;//141
        byte[] pemPublicExponent = new byte[3];
        Array.Copy(keyData, index, pemPublicExponent, 0, 3);

        index += 3;
        index += 4;//148
        byte[] pemPrivateExponent = new byte[128];
        Array.Copy(keyData, index, pemPrivateExponent, 0, 128);

        index += 128;
        index += ((int)keyData[index + 1] == 64 ? 2 : 3);//279
        byte[] pemPrime1 = new byte[64];
        Array.Copy(keyData, index, pemPrime1, 0, 64);

        index += 64;
        index += ((int)keyData[index + 1] == 64 ? 2 : 3);//346
        byte[] pemPrime2 = new byte[64];
        Array.Copy(keyData, index, pemPrime2, 0, 64);

        index += 64;
        index += ((int)keyData[index + 1] == 64 ? 2 : 3);//412/413
        byte[] pemExponent1 = new byte[64];
        Array.Copy(keyData, index, pemExponent1, 0, 64);

        index += 64;
        index += ((int)keyData[index + 1] == 64 ? 2 : 3);//479/480
        byte[] pemExponent2 = new byte[64];
        Array.Copy(keyData, index, pemExponent2, 0, 64);

        index += 64;
        index += ((int)keyData[index + 1] == 64 ? 2 : 3);//545/546
        byte[] pemCoefficient = new byte[64];
        Array.Copy(keyData, index, pemCoefficient, 0, 64);

        RSAParameters para = new RSAParameters();
        para.Modulus = pemModulus;
        para.Exponent = pemPublicExponent;
        para.D = pemPrivateExponent;
        para.P = pemPrime1;
        para.Q = pemPrime2;
        para.DP = pemExponent1;
        para.DQ = pemExponent2;
        para.InverseQ = pemCoefficient;
        return para;
    }
    #endregion

}



#endregion