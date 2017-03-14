<?php
header('content-type: text/html; charset=utf-8');
include 'common.php';

$service = $_POST['service'];
$totalFee = $_POST['totalFee'];
//商家订单号
$merOrderNum = date('YmdHis') . mt_rand(100000, 999999);
//测试专用app_id
$fula_appid="1000015";
//测试专用mch_id
$fula_mch_id="88793741592119799888";
//付啦统一下单接口测试环境地址
//付啦统一下单接口正式上线环境地址：https://api.fulapay.com/pay/unifiedOrder
$fula_url="https://sandapi.fulapay.com/pay/unifiedOrder"; 

//测试专用商户私钥
//$MCH_PRIVATE_KEY=file_get_contents('rsa_private_key.pem');
$MCH_PRIVATE_KEY="MIICXAIBAAKBgQDS8E5klNSVTk8wB9aGDI2RaNj1dnxN4E5JZpOjPZwe4K6GHFKwzMyYikEd4yyclxSieRIX2UgXKoMsclpjKowev3//Fm2uwRYZ7ptZmZzMkCkCLh7peF70xOzEwbzDbD+iPDftmjJi/cOYukMpqcETJz+7gDRyM6XTbICsROvRMwIDAQABAoGARijTijTOeFKxmAIjYjaup5y9/PbJ5YUjxNovZO5LDSIiKk0lW9TigCIMAora/cJqhJfMYs5MYhgrxQ7L7YvrSnwdKATqnZXG9Cg7X2rPBdEHsAXs8PHCejZxkVsmiToMoJynr+pZZd8U1BJvaeUCyjCENEbgqRVMn76qemE5/+ECQQDzKva9qVEjSeU4IckV2zO+5XwKKeZi+LmCrdcm10//rwb7jMZHSLNsSZP15ZQuyalPgC+MN+bm4q2Fm4lOKMnJAkEA3hHyvI/Q9pnY+ghy0r4W/xhvFFNy20u8pUCBFItAQm2r5Azh0kBg/nReLCq0RMVMcYWpdxx3H7EGt7pOEY7BGwJBAJpcEZk/3BOsmudQ+xb2uRhbEKxbALae8RP1SniLgWrrLmGMcWlayxrYciZMAA7bULD4zDYUXmz+Q+xmWXjWB5ECQDOw69a2h0aPozD4PHoPo1y3vEt37TlclMnGjQVSMKAF1UgdM4d0Ut+ICM+xFQsY/eVC3qKKLrRiyZSEul1d5FcCQFy4u8aiZDhpvEDIsvb/hD7yhfrj5Q6D0k3ilIYI2QMNKV013SC6l3+g0o3CRvq7iY1E1YQR9/u10UHMD3DijtM=";

switch ($service) {
    case 'pay.wxpay.qrcode':
        $pngDES="微信";
        break;
    case 'pay.alipay.qrcode':
        $pngDES="支付宝";
        break;
}

$data = array(
    'app_id' => $fula_appid,
    'body' => '账户充值',
    'charset' => 'UTF-8',
    'mch_create_ip' => '127.0.0.1',
    'mch_id' => $fula_mch_id,
    'nonce_str' => uniqid(),
    'notify_url' => 'http://www.xxx.com/pay/fulapay/notify.php',
    'out_trade_no' => $merOrderNum,
    'service' => $service,
    'total_fee' => $totalFee* 100,//金额转为分
);
$string = makeString($data);
$sign = makeSign($string, $MCH_PRIVATE_KEY);
$data['sign'] = $sign;
$header = array('Content-type: text/html');
$data = array2xml($data);
$result = http($fula_url, $data, 'POST', $header);
$result = xml2array($result);
if ($result['res_code'] == '0000' && $result['result_code'] == 'S') {
   
   //如果用js生成二维码，则输出以下json字符串
   //$r=array();
   //$r['success']=true;
   //$r['pay_info']=$result['pay_info'];
   //echo json_encode($r);exit;
   //////////////////////////////////////////////

   //以下为php输出二维码
    include ('phpqrcode.php');
    $pic1 = "qrcode.png";
    $pic2 = "output.png";
    $qrcode=$result['pay_info'];
    $errorCorrectionLevel = 'L';
    $matrixPointSize = 10;
    if (file_exists($pic1) or file_exists($pic2)) {
        unlink('qrcode.png');
        unlink('output.png');
    }
    QRcode::png($qrcode, 'qrcode.png', $errorCorrectionLevel, $matrixPointSize, 2);
    $QR = 'qrcode.png';
    $logo = 'logo.png';
    if ($logo !== FALSE) {
        $QR = imagecreatefromstring(file_get_contents($QR));
        $logo = imagecreatefromstring(file_get_contents($logo));
        $QR_width = imagesx($QR);
        $QR_height = imagesy($QR);
        $logo_width = imagesx($logo);
        $logo_height = imagesy($logo);
        $logo_qr_width = $QR_width / 7;
        $scale = $logo_width / $logo_qr_width;
        $logo_qr_height = $logo_height / $scale;
        $from_width = ($QR_width - $logo_qr_width) / 2;
        imagecopyresampled($QR, $logo, $from_width, $from_width, 0, 0, $logo_qr_width, $logo_qr_height, $logo_width, $logo_height);
    }
    imagepng($QR, 'output.png');
    imagedestroy($QR);
    echo $pngDES."扫码支付二维码：" . "<br>" . "<img src='output.png'>";
}
?>