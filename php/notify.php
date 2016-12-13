<?php
header("content-Type: text/html; charset=utf8");
include 'common.php';
//付啦测试专用公钥 $fulaPubKey=file_get_contents('fula_rsa_public_key.pem');
$fulaPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDkTf0Tw9kP7c5+K1Xhq0eIw3wo1TdIDjcB216f6R7kxUQ4GedsUzNFX3t2N/9rT6hIllsuBi7B7AIGYF2cphq5edYhTrjsZyFOTIM/0Nz4KfLL+hrRMfXUJ6yxD5t4nJv9shAhUi+mYS4hJ8k+kQ0o5cR/SaxNzzgxKAdFVyRCWwIDAQAB";
$result = xml2array(file_get_contents('php://input'));
//验证签名
$checkSign = notifySignCheck($result, $fulaPubKey);
if ($checkSign && $result['result_code'] == 'S' && $result['res_code'] == '0000') {
    //// 商户业务逻辑
    // ......
    //// 返回success字符串
    echo 'success';
} else {
    echo "Signature verification failed";
}
?>