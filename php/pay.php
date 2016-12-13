<?php
header('content-type: text/html; charset=utf-8');
?>
<html>
    <head>
        <title>fula-pay-demo</title>
        <meta http-equiv="content-Type" content="text/html; charset=utf-8">
        <script src="http://code.jquery.com/jquery-latest.js"></script>
        <script type="text/javascript" src="js/jquery.qrcode.min.js"></script>
    </head>
<body>
  <!--如果用php生成二维码,改成<form action="fulaPay.php" method="POST" name="frm"  target="_blank">-->
  <form id="form">
    支付接口
    <select name="service" id="service" onchange="changeService(this.value);">
        <option value="pay.alipay.qrcode">支付宝二维码支付</option>
        <option value="pay.wxpay.qrcode">微信二维码支付</option>
    </select>
    <br><br>

    <div id="authCodeDiv" style="display: none;">
        条形码
        <input type="text" name="authCode" value="">
        支付宝或者微信的条形码字符串
        <br><br>
    </div>

    支付金额
    <input type="text" name="totalFee" value="">
    <span>元</span>
    <br><br>

    <span id="error" style="color: red;display: none;"></span>
    <br><br>
  <!--如果用php生成二维码,改成<input id="submit" type="submit" value="提交" />-->
    <input id="submit" type="button" value="提交" />
</form>

<div id="qrcode"></div>

<script>
    var authCodeDiv = document.getElementById('authCodeDiv');
    function changeService(service) {
        if(service == 'pay.alipay.scan' || service == 'pay.wxpay.scan'){
            authCodeDiv.style.display = 'block';
        } else {
            authCodeDiv.style.display = 'none';
        }
        document.getElementById('qrcode').innerHTML = '';
    }
    document.getElementById("submit").addEventListener('click', function () {
        document.getElementById("error").style.display = 'none';
        $.post('/fulaPay.php', $('#form').serialize(), function (data) {
            if (data.success) {
                var service = $("#service").val();
                if(service == 'pay.alipay.qrcode' || service == 'pay.wxpay.qrcode'){
                    $('#qrcode').qrcode(data.payInfo);
                } else{
                    // 扫码支付同步返回支付结果
                    alert('支付成功');
                }
            } else {
                document.getElementById("error").innerHTML = '错误信息: ' + data.error;
                document.getElementById("error").style.display = 'block';
            }
        }, 'json');
    }, false);
</script>
</body>
</html>