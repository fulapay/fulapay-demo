# -*- encoding:utf-8 -*-
import base64, urllib2
from Crypto.PublicKey import RSA
from Crypto.Signature import PKCS1_v1_5
from Crypto.Hash import SHA
from urllib import quote


# 私钥文件
priKey = '''-----BEGIN RSA PRIVATE KEY-----
MIICXAIBAAKBgQDS8E5klNSVTk8wB9aGDI2RaNj1dnxN4E5JZpOjPZwe4K6GHFKw
zMyYikEd4yyclxSieRIX2UgXKoMsclpjKowev3//Fm2uwRYZ7ptZmZzMkCkCLh7p
eF70xOzEwbzDbD+iPDftmjJi/cOYukMpqcETJz+7gDRyM6XTbICsROvRMwIDAQAB
AoGARijTijTOeFKxmAIjYjaup5y9/PbJ5YUjxNovZO5LDSIiKk0lW9TigCIMAora
/cJqhJfMYs5MYhgrxQ7L7YvrSnwdKATqnZXG9Cg7X2rPBdEHsAXs8PHCejZxkVsm
iToMoJynr+pZZd8U1BJvaeUCyjCENEbgqRVMn76qemE5/+ECQQDzKva9qVEjSeU4
IckV2zO+5XwKKeZi+LmCrdcm10//rwb7jMZHSLNsSZP15ZQuyalPgC+MN+bm4q2F
m4lOKMnJAkEA3hHyvI/Q9pnY+ghy0r4W/xhvFFNy20u8pUCBFItAQm2r5Azh0kBg
/nReLCq0RMVMcYWpdxx3H7EGt7pOEY7BGwJBAJpcEZk/3BOsmudQ+xb2uRhbEKxb
ALae8RP1SniLgWrrLmGMcWlayxrYciZMAA7bULD4zDYUXmz+Q+xmWXjWB5ECQDOw
69a2h0aPozD4PHoPo1y3vEt37TlclMnGjQVSMKAF1UgdM4d0Ut+ICM+xFQsY/eVC
3qKKLrRiyZSEul1d5FcCQFy4u8aiZDhpvEDIsvb/hD7yhfrj5Q6D0k3ilIYI2QMN
KV013SC6l3+g0o3CRvq7iY1E1YQR9/u10UHMD3DijtM=
-----END RSA PRIVATE KEY-----'''

# 公钥文件
pubKey = '''-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDS8E5klNSVTk8wB9aGDI2RaNj1
dnxN4E5JZpOjPZwe4K6GHFKwzMyYikEd4yyclxSieRIX2UgXKoMsclpjKowev3//
Fm2uwRYZ7ptZmZzMkCkCLh7peF70xOzEwbzDbD+iPDftmjJi/cOYukMpqcETJz+7
gDRyM6XTbICsROvRMwIDAQAB
-----END PUBLIC KEY-----'''

'''*RSA签名
* data待签名数据
* 签名用商户私钥，必须是没有经过pkcs8转换的私钥
* 最后的签名，需要用base64编码
* return Sign签名
'''


# RSA签名
def sign(data):
    key = RSA.importKey(priKey)
    h = SHA.new(data)
    signer = PKCS1_v1_5.new(key)
    signature = signer.sign(h)
    return base64.b64encode(signature)

'''*RSA验签
* data待签名数据
* signature需要验签的签名
* 验签用支付宝公钥
* return 验签是否通过 bool值
'''


# RSA验签
def verify(data, signature):
    key = RSA.importKey(pubKey)
    h = SHA.new(data)
    verifier = PKCS1_v1_5.new(key)
    if verifier.verify(h, base64.b64decode(signature)):
        return True
    return False


# 构建签名数据
def formatBizQueryParaMap(paraMap, urlencode):
    """格式化参数，签名过程需要使用"""
    slist = sorted(paraMap)  # 字典顺序排序
    buff = []
    for k in slist:
        v = quote(paraMap[k]) if urlencode else paraMap[k]
        buff.append("{0}={1}".format(k, v))
    return "&".join(buff)


# 接口必填参数
PAY_UNIFIED_PAY = "pay.alipay.qrcode"
dict_data = {
    'service': PAY_UNIFIED_PAY,
    'app_id': '1000015',
    'mch_id': '8012667850604828',
    'nonce_str': 'qwertgftryhgtyhgsderf',
    'charset': "UTF-8",
    'sign_type': "RSA",
    'total_fee': '10',
    'mch_create_ip': '192.168.1.1',
    'body': '支付测试',
    'out_trade_no': '11199989988777666222',
    'notify_url': "http://wap.xxx.com/xxx.asp",  # 回调地址
    'buyer': '18710405368'
}

# 构建签名数据
raw_data = formatBizQueryParaMap(dict_data, None)
print raw_data
sign_data = sign(raw_data)
print "sign_data: ", sign_data
print verify(raw_data, sign_data)
dict_data['sign'] = sign_data

# 构建接口请求数据 xml字符串
xml_str = '<?xml version="1.0" encoding="UTF-8"?><xml>'
for key, value in dict_data.items():
    xml_str += '<' + str(key) + '>' + str(value) + '</' + str(key) + '>'
xml_str += '</xml>'
print xml_str

# POST方法请求接口
url = 'https://sandapi.fulapay.com/pay/unifiedOrder'
request = urllib2.Request(url, xml_str)
request.add_header('charset', 'utf-8')
request.add_header('Content-Type', 'application/xml')
request.add_header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36")
response = urllib2.urlopen(request)
print response.read()
