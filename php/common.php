<?php
function makeString($data) {
    ksort($data);
    $flag = 1;
    foreach ($data as $k => $v) {
        if ($flag) {
            $string = $k . '=' . $v;
            $flag = 0;
        } else {
            $string .= '&' . $k . '=' . $v;
        }
    }
    return $string;
}

function http($url, $params, $method = 'GET', $httpHeader = '', $ssl = false) {
    $opts = array(CURLOPT_TIMEOUT => 2, CURLOPT_RETURNTRANSFER => 1, CURLOPT_SSL_VERIFYPEER => false, CURLOPT_SSL_VERIFYHOST => false);
    /* 根据请求类型设置特定参数 */
    switch (strtoupper($method)) {
        case 'GET':
            $getQuerys = !empty($params) ? '?' . urldecode(http_build_query($params)) : '';
            $opts[CURLOPT_URL] = $url . $getQuerys;
            // Log::record($opts[CURLOPT_URL]);
            break;
        case 'POST':
            $opts[CURLOPT_URL] = $url;
            $opts[CURLOPT_POST] = 1;
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
    $data = curl_exec($ch);
    $err = curl_errno($ch);
    $errmsg = curl_error($ch);
    curl_close($ch);
    if ($err > 0) {
        return false;
    } else {
        return $data;
    }
}

function makeSign($string, $priKey) {
    $res = openssl_pkey_get_private($priKey);
    $signature = '';
    openssl_sign($string, $signature, $res);
    openssl_free_key($res);
    return base64_encode($signature);
}

//*********************签名验证，供notify使用**********
function notifySignCheck($result, $pubKey) {
    $sign = $result['sign'];
    $sign = base64_decode($sign);
    unset($result['sign']);
    $string = makeString($result);
    //获取付啦的公钥信息
    $res = openssl_pkey_get_public($pubKey);
    //用公钥验证签名
    $check = openssl_verify($string, $sign, $res);
    openssl_free_key($res);
    return $check;
}

//**********************xml与array格式转换**********
function xml2array($xml) {
    $data = (array) simplexml_load_string($xml, 'SimpleXMLElement', LIBXML_NOCDATA);
    return array_change_key_case($data, CASE_LOWER);
}

/**
 * 将数组转换成XML string
 * @param  array $array
 * @return xml
 */
function array2xml($array = []) {
    if (!is_array($array) || count($array) <= 0) {
        Log::error("array2xml --> 数组数据异常！");
    }
    $xml = "<xml>";
    foreach ($array as $key => $val) {
        if (is_numeric($val)) {
            $xml.="<" . $key . ">" . $val . "</" . $key . ">";
        } else {
            $xml.="<" . $key . "><![CDATA[" . $val . "]]></" . $key . ">";
        }
    }
    $xml.="</xml>";
    return $xml;
}

?>