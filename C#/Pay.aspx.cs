using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Xml;


/// <summary>
/// 支付
/// </summary>
public partial class Pay : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        string callBack = Request["c"].ToLower();

        Response.Clear();



        string val = "";

        通道返回参数 obj = new 通道返回参数();

        try
        {
            if (!string.IsNullOrEmpty(callBack))
            {
                if (callBack == "getqrcode")
                {
                    string serviceType = Request["ServiceType"];

                    obj = 扫码支付(serviceType, Convert.ToInt32(Request["SaleAmt"], 1));
                }
                else if (callBack == "merchantenter")
                {
                    merchantEnter();
                }
            }
        }
       
        catch (Exception ex)
        {
            obj.Status = 0;
            obj.Msg = ex.Message;
        }
        val = Newtonsoft.Json.JsonConvert.SerializeObject(obj);

        Response.Write(val);

    }

    #region 扫码支付

    public 通道返回参数 扫码支付(string serviceType, int saleAmt)
    {
        if (saleAmt == 0)
        {
            throw new Exception("支付金额不能为零");
        }


        通道返回参数 val = null;

        string soid = Guid.NewGuid().ToString().Replace("-", "");




        if (serviceType == "pay.wxpay.qrcode")
        {
            val = 付啦微信扫码(soid, saleAmt);
        }
        else
        {
            val = 付啦支付宝扫码(soid, saleAmt);
        }



        //记录相应的扫码请求


        return val;
    }



    public 通道返回参数 付啦支付宝扫码(string soid, int saleAmt)
    {
        return 付啦扫码(soid, "pay.alipay.qrcode", saleAmt);
    }

    public 通道返回参数 付啦微信扫码(string soid, int saleAmt)
    {
        return 付啦扫码(soid, "pay.wxpay.qrcode", saleAmt);
    }





    /// <summary>
    /// 支付宝扫码支付
    /// </summary>
    /// <returns></returns>
    public 通道返回参数 付啦扫码(string soid, string serivice, int saleAmt)
    {
        if (string.IsNullOrEmpty(soid))
            soid = Guid.NewGuid().ToString().Replace("-", "");

        TopDictionary paramers = new TopDictionary();
        paramers.Add("service", serivice);
        paramers.Add("mch_create_ip", "127.0.0.1");
        paramers.Add("out_trade_no", soid);
        paramers.Add("body", "付啦扫码支付");
        paramers.Add("total_fee", saleAmt);

        FulaPayType payType = new FulaPayType();
        paramers.Add("mch_id", payType.MCH_ID);

        string a = payType.DoPost(payType.统一下单接口请求地址, paramers);

        

        XmlDocument xml = new XmlDocument();
        try
        {
            xml.LoadXml(a.Trim());
        }
        catch (Exception ex)
        {
            //ErrorLog.GetInstance().Write(ex);
        }

        paramers.Clear();
        foreach (XmlNode element in xml.ChildNodes[1].ChildNodes)
        {
            paramers.Add(element.Name, element.InnerXml);
        }

        通道返回参数 obj = new 通道返回参数();
        string result_code = xml.SelectSingleNode("//result_code").InnerText;
        string res_msg = xml.SelectSingleNode("//res_msg").InnerText;
        string res_code = xml.SelectSingleNode("//res_code").InnerText;
        string err_msg = "";
        try
        {
            err_msg = xml.SelectSingleNode("//err_msg").InnerText;
        }
        catch (Exception)
        {

        }


        if (payType.Verify(paramers))
        {
            if (res_code == "0000" && result_code == "S")
            {
                string pay_info = xml.SelectSingleNode("//pay_info").InnerText;
                obj.Status = 1;
                obj.QrCode = pay_info;

                //进行扫码支付订单保存

            }
            else
            {
                obj.Msg = err_msg;
                obj.ResCode = res_code;
            }
        }
        else
        {
            obj.Msg = err_msg;
            obj.ResCode = res_code;
        }

        return obj;
    }

    #endregion



    #region 商户进件



    /// <summary>
    /// 商户进件
    /// </summary>
    /// <returns></returns>
    public 通道返回参数 merchantEnter()
    {
        通道返回参数 obj = new 通道返回参数();
        FulaPayType payType = new FulaPayType();
        TopDictionary paramers = new TopDictionary();
        paramers.Add("service", payType.PAY_MERCHANT_ENTER);
        paramers.Add("type", Request["type"]);
        paramers.Add("is_company", Request["is_company"]);

        if (Request["type"] != payType.MERCHANT_ENTER_TYPE_BASE_ADD)
        {
            paramers.Add("merchant_no", Request["merchant_no"]); //通过客户编号读取); // merchant_no为调用type=0时返回的商户号
        }


        if (Request["type"] != payType.MERCHANT_ENTER_TYPE_IMAGE_UPDATE)
        {

            paramers.Add("name", Request["name"]); // 请填写真实姓名,要与身份证上一致
            paramers.Add("mobile", Request["mobile"]); // 商户联系方式,提交认证后不可修改


            paramers.Add("id_number", Request["id_number"]);
            // 商户联系方式,提交认证后不可修改

            paramers.Add("bank_name", Request["bank_name"]); // 清算银行名称
            paramers.Add("bank_account", Request["bank_account"]);
            // 清算银行帐户
            paramers.Add("bank_mobile", Request["bank_mobile"]);
            // 商户联系方式,提交认证后不可修改


            if (Request["is_company"] == "1")
            {
                // 对公帐户需要提供营业执照号和组织机构码
                paramers.Add("biz_license_no", Request["biz_license_no"]); // 营业执照号
                paramers.Add("org_code", Request["org_code"]); // 组织机构码
                paramers.Add("biz_license_image", Request["biz_license_image"]); //营业执照正面照，图片文件base64编码后的字符串,格式化
            }

            paramers.Add("bank_code", Request["bank_code"]); // 清算银行代码
            paramers.Add("bank_subbranch_no", Request["bank_subbranch_no"]); // 清算银行联行号 非必填
            paramers.Add("bank_subbranch", Request["bank_subbranch"]);
            // 清算银行支行
        }
        else
        {

        }

        string a = payType.DoPost(payType.商户进件, paramers);

        XmlDocument xml = new XmlDocument();
        try
        {
            xml.LoadXml(a.Trim());
        }
        catch (Exception ex)
        {
            //ErrorLog.GetInstance().Write(ex);
        }

        return obj;
    }

    #endregion
}