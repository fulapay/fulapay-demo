#-*- encoding:utf-8 -*-
import base64,urllib2
from Crypto.PublicKey import RSA
from Crypto.Signature import PKCS1_v1_5
from Crypto.Hash import SHA
from urllib import quote

#私钥文件
priKey = '''-----BEGIN RSA PRIVATE KEY-----
MIICXQIBAAKBgQDSkgjfSgT6xy4yFJXfYxik0+xCkhKwVS3B5p0MX+5v8NzGptqW
OQKTwKkOfIpb2FLS2Arf1ngNVb09MM0o9C+W8p4Sdvq3XDyZOJEVTfm5/m9VXxOx
dxDaZTKT41qdqz+3MtjkGoLO7KynRpz3S6Vc5PYet9Q2X16c6RIVm6Lr9wIDAQAB
AoGBAI9KSvlPkGXq5rezpG04LQv1HoAKkOcnf5TETrieC05nlIUmA/dX4jYTU7eE
7k4jvfC2jWfWtSk14CnkrKeiy0Jr3Qos/lE2v/yM7zWQJtn2FTLWSVvhn5NmDIiU
ZuVcXahA531//KtaThuh59xsYiHp1FX4euMT/PybKV/UgkDZAkEA6/XfxmwomTqq
Nm1V3VCCze491ViOyt5mM6B8OjKEDztPTkkhc4wDLGnnSIrIfWYDNb2IfaSQTFBW
qcD87B5fwwJBAOR0JQ/gnS/NX63p5O2J7LE5SAMUac52yr97kFtK4zlwYgvE7i8S
PQHYwvFh09rg8B7hPl4L7HT5Ll9h10aOU70CQAiP0WDXx9lXgZmb+gOfyj9Cp8e4
AOZnj3nMFvXdo2ESG9yRVr9kkzDnXtcJAiFatD8c83jrobjDKYHbWNIQgBUCQAHz
vB1WngE6kAa8aOhetVcOVj8wfXb2LOVV+PXOvgJPo4bWN/LPBRZu87t8SoMkSOj6
o73J/EtDldHa1CwWK0ECQQDmWj/qrs09JyEozEJJL2dEIZzGlrZgGAb4ePgnI7oE
lG+/F2MDorRvMulYFMDaXtlLYm1h7+reiGQd2jLGzWD1
-----END RSA PRIVATE KEY-----'''

#公钥文件
pubKey = '''-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDSkgjfSgT6xy4yFJXfYxik0+xC
khKwVS3B5p0MX+5v8NzGptqWOQKTwKkOfIpb2FLS2Arf1ngNVb09MM0o9C+W8p4S
dvq3XDyZOJEVTfm5/m9VXxOxdxDaZTKT41qdqz+3MtjkGoLO7KynRpz3S6Vc5PYe
t9Q2X16c6RIVm6Lr9wIDAQAB
-----END PUBLIC KEY-----'''

'''*RSA签名
* data待签名数据
* 签名用商户私钥，必须是没有经过pkcs8转换的私钥
* 最后的签名，需要用base64编码
* return Sign签名
'''
def sign(data):
    key = RSA.importKey(priKey)
    h = SHA.new(data)
    signer = PKCS1_v1_5.new(key)
    signature = signer.sign(h)
    return base64.b64encode(signature).replace("\n","")
#     return base64.urlsafe_b64encode(signature)
'''*RSA验签
* data待签名数据
* signature需要验签的签名
* 验签用支付宝公钥
* return 验签是否通过 bool值
'''
def verify(data, signature):
    key = RSA.importKey(pubKey)
    h = SHA.new(data)
    verifier = PKCS1_v1_5.new(key)
    if verifier.verify(h, base64.b64decode(signature)):
        return True
    return False


def formatBizQueryParaMap(paraMap, urlencode):
        """格式化参数，签名过程需要使用"""
        slist = sorted(paraMap)#字典顺序排序
        buff = []
        for k in slist:
            v = quote(paraMap[k]) if urlencode else paraMap[k]
            buff.append("{0}={1}".format(k, v))
        return "&".join(buff)

PAY_UNIFIED_PAY = "pay.alipay.qrcode"
dict_data={
    'service':PAY_UNIFIED_PAY,
    'app_id':'1000015',
    'mch_id':'8012667850604828',
    'nonce_str':'qwertgftryhgtyhgsderf',
    'charset':"UTF-8",
    'sign_type':"RSA",
    'total_fee':'10',
    'mch_create_ip':'192.168.1.1',
    'body':'支付测试',
    'out_trade_no':'11199989988777666222',
    'notify_url':"http://wap.xxx.com/xxx.asp", #回调地址
    'buyer':'18710405368'
    }

raw_data = formatBizQueryParaMap(dict_data,None)
sign_data = sign(raw_data)
print "sign_data: ", sign_data
print verify(raw_data, sign_data)

print raw_data
dict_data['sign']=sign_data

xml_str='<?xml version="1.0" encoding="UTF-8" ?><xml>'
for key, value in dict_data.items():
    xml_str+='<'+str(key)+'>'+str(value)+'</'+str(key)+'>'

xml_str+='</xml>'

print xml_str


url='https://sandapi.fulapay.com/pay/unifiedOrder'
request = urllib2.Request(url, xml_str)
request.add_header('charset','utf-8')
request.add_header('Content-Type', 'application/xml')
request.add_header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36")
response = urllib2.urlopen(request)
print response.read()










