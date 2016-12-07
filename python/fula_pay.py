# -*- encoding:utf-8 -*-
import base64, urllib2
from Crypto.PublicKey import RSA
from Crypto.Signature import PKCS1_v1_5
from Crypto.Hash import SHA
from urllib import quote


from util import fl_pay_https_post

# 统一下单接口
FL_URL = 'https://sandapi.fulapay.com/pay/unifiedOrder'
PAY_UNIFIED_PAY = "pay.alipay.qrcode"
dict_data = {
    'service': PAY_UNIFIED_PAY,
    'mch_id': '8012667850604828',
    'total_fee': '10',
    'mch_create_ip': '192.168.1.1',
    'body': '支付测试',
    'out_trade_no': '11199989988777666222',
    'notify_url': "http://wap.xxx.com/xxx.asp",  # 回调地址
    'buyer': '18710405368'
}

# ---------https请求----------
res_data = fl_pay_https_post(dict_data, FL_URL)
#     print res_data
# ---------响应处理------
if res_data['xml']['res_code'] == '0000':
    print '支付成功'
    print res_data['xml']['pay_info']
else:
    print '支付失败'
