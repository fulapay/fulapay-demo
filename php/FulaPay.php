<?php

namespace app\pay;

use think\Controller;
use app\index\model\Merchant;
use think\Log;

class FulaPay extends Controller {

    public static function fulaOrder($tranAmt, $merOrderNum, $orderInfo, $payparam, $payment) {
        switch ($payment) {
            case 'wx':
                $service = 'pay.wxpay.qrcode';
                break;
            case 'ali':
                $service = 'pay.alipay.qrcode';
                break;
        }
        Log::record($payparam['mid']);
        $url = 'https://sandapi.fulapay.com/pay/unifiedOrder';
        $data = array(
            'app_id' => '1000015',
            'body' => $orderInfo,
            'charset' => 'UTF-8',
            'mch_create_ip' => '121.43.161.81',
            'mch_id' => $payparam['mid'],
            'nonce_str' => uniqid(),
            'notify_url' => 'http://vince.chinese-www.com/index.php/merchant/notify/Fula',
            'out_trade_no' => $merOrderNum,
            'service' => $service,
            'total_fee' => $tranAmt,
            );
        $string = self::makeString($data);
        $sign = self::makeSign($string);

        $data['sign'] = $sign;
        $header = array('Content-type: text/xml');
        $data = array2xml($data);
        $result = http($url, $data, 'POST', $header);
        $result = xml2array($result);
        Log::record($result);
        if ($result['res_code'] == '0000' && $result['result_code'] == 'S') {
            return $result['pay_info'];
        }
    }

    public static function fulaJsOrder($tranAmt, $merOrderNum, $orderInfo, $payparam, $payment) {
        switch ($payment) {
            case 'wx':
                $service = 'pay.wxpay.js';
                break;
            case 'ali':
                $service = 'pay.alipay.js';
                break;
        }
        $url = 'https://api.fulapay.com/pay/jspay';
        $data = array(
            'app_id' => '1000015',
            'body' => $orderInfo,
            'charset' => 'UTF-8',
            'mch_create_ip' => '121.43.161.81',
            'mch_id' => $payparam['mid'],
            'nonce_str' => uniqid(),
            'notify_url' => 'http://vince.chinese-www.com/index.php/merchant/notify/Fula',
            'return_url' => 'http://vince.chinese-www.com/index.php/index/pay/returnSuccess',
            'out_trade_no' => $merOrderNum,
            'service' => $service,
            'total_fee' => $tranAmt,
            );
        $string = self::makeString($data);
        $sign = self::makeSign($string);
        $result = $url.'?'.urlencode($string.'&sign='.$sign);
        Log::record($result);
        return $result;
    }

    public function fulaEnter($cid) {
        $merInfo = Merchant::getMerForEnter($cid);
        $bankInfo = json_decode($merInfo['bank_info'], true);
		$payParams = json_decode($merInfo['pay_params'],true);
        $url = 'https://sandapi.fulapay.com/merchant/enter';
        $data = array(
            'app_id' => '1000015',
            'charset' => 'UTF-8',
            'nonce_str' => uniqid(),
            'service' => 'pay.merchant.enter',
            'bank_account' => $bankInfo['bankAccount'],
            'bank_code' => $bankInfo['bankCode'],
            'bank_mobile' => $bankInfo['bankPhone'],
            'bank_name' => $bankInfo['bankName'],
            'bank_subbranch' => $bankInfo['subBranch'],
            'bank_subbranch_no' => $bankInfo['bankNo'],
            'id_number' => $merInfo['mgrID'],
            'mobile' => $bankInfo['bankPhone'],
            'name' => $merInfo['mer_mgr'],
            'type' => '0',
            'sign_type' => 'RSA',
            );
		
        $string = $this->makeString($data);
        $sign = $this->makeSign($string);

        $data['sign'] = $sign;
        $header = array('Content-type: text/xml');
        $data = array2xml($data);
        $result = http($url, $data, 'POST', $header);
        $result = xml2array($result);
        if ($result['res_code'] == '0000' && $result['result_code'] == 'S') {
            $payParams['fulapay']['mid'] = $result['merchant_no'];
			if(!$rs = Merchant::setPayParams($cid, json_encode($payParams))){
                Log::record($rs);
				Log::record('cid_'.$cid.'_merchantNo_'.$result['merchant_no'], 'error');
			}
			return true;
        }else{
			return false;
		}
    }

    private static function makeString($data) {
        ksort($data);
        $flag = 1;
        foreach ($data as $k => $v) {
            if ($flag) {
                $string = $k.'='.$v;
                $flag = 0;
            } else {
                $string .= '&'.$k.'='.$v;
            }
        }
        return $string;
    }


    private static function makeSign($string) {
        $res = openssl_pkey_get_private(file_get_contents('private.pem'), '4008778004');
        $signature = '';
        openssl_sign($string, $signature, $res);
        openssl_free_key($res);
        return base64_encode($signature);
    }

    function http($url, $params = [], $method = 'GET', $httpHeader = '', $ssl = false)
    {
        $opts = [
        CURLOPT_TIMEOUT        => 60,
        CURLOPT_RETURNTRANSFER => 1,
        CURLOPT_SSL_VERIFYPEER => false,
        CURLOPT_SSL_VERIFYHOST => false,
        ];
        /* 根据请求类型设置特定参数 */
        switch (strtoupper($method)) {
            case 'GET':
                $getQuerys         = !empty($params) ? '?' . urldecode(http_build_query($params)) : '';
                $opts[CURLOPT_URL] = $url . $getQuerys;
                // Log::record($opts[CURLOPT_URL]);
                break;
            case 'POST':
                $opts[CURLOPT_URL]        = $url;
                $opts[CURLOPT_POST]       = 1;
                $opts[CURLOPT_POSTFIELDS] = $params;
                break;
        }
        /* 初始化并执行curl请求 */
        $ch = curl_init();
        if (!empty($httpHeader) && is_array($httpHeader)) {
            curl_setopt($ch, CURLOPT_HTTPHEADER, $httpHeader);
        }
        curl_setopt_array($ch, $opts);
        curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_0);
        $data   = curl_exec($ch);
        $err    = curl_errno($ch);
        $errmsg = curl_error($ch);
        curl_close($ch);
        if ($err > 0) {
            Log::error('CURL:' . $errmsg);
            return false;
        } else {
            return $data;
        }
    }
}
