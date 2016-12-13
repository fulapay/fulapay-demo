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

//测试专用商户私钥(pkcs8格式)
//$MCH_PRIVATE_KEY=file_get_contents('rsa_private_key.pem');
$MCH_PRIVATE_KEY="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANLwTmSU1JVOTzAH1oYMjZFo2PV2fE3gTklmk6M9nB7groYcUrDMzJiKQR3jLJyXFKJ5EhfZSBcqgyxyWmMqjB6/f/8Wba7BFhnum1mZnMyQKQIuHul4XvTE7MTBvMNsP6I8N+2aMmL9w5i6QympwRMnP7uANHIzpdNsgKxE69EzAgMBAAECgYBGKNOKNM54UrGYAiNiNq6nnL389snlhSPE2i9k7ksNIiIqTSVb1OKAIgwCitr9wmqEl8xizkxiGCvFDsvti+tKfB0oBOqdlcb0KDtfas8F0QewBezw8cJ6NnGRWyaJOgygnKev6lll3xTUEm9p5QLKMIQ0RuCpFUyfvqp6YTn/4QJBAPMq9r2pUSNJ5TghyRXbM77lfAop5mL4uYKt1ybXT/+vBvuMxkdIs2xJk/XllC7JqU+AL4w35ubirYWbiU4oyckCQQDeEfK8j9D2mdj6CHLSvhb/GG8UU3LbS7ylQIEUi0BCbavkDOHSQGD+dF4sKrRExUxxhal3HHcfsQa3uk4RjsEbAkEAmlwRmT/cE6ya51D7Fva5GFsQrFsAtp7xE/VKeIuBausuYYxxaVrLGthyJkwADttQsPjMNhRebP5D7GZZeNYHkQJAM7Dr1raHRo+jMPg8eg+jXLe8S3ftOVyUycaNBVIwoAXVSB0zh3RS34gIz7EVCxj95ULeoooutGLJlIS6XV3kVwJAXLi7xqJkOGm8QMiy9v+EPvKF+uPlDoPSTeKUhgjZAw0pXTXdILqXf6DSjcJG+ruJjUTVhBH3+7XRQcwPcOKO0w==";

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