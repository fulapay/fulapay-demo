<%--
  Created by IntelliJ IDEA.
  User: wuming
  Date: 16/10/20
  Time: 下午8:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>fula-pay-demo</title>
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script type="text/javascript" src="js/jquery.qrcode.min.js"></script>
</head>

<body>
<form id="form">
    支付接口
    <select name="service" id="service" onchange="changeService(this.value);">
        <option value="pay.alipay.qrcode">支付宝二维码支付</option>
        <option value="pay.wxpay.qrcode">微信二维码支付</option>
        <option value="pay.alipay.scan">支付宝扫码支付</option>
        <option value="pay.wxpay.scan">微信扫码支付</option>
        <option value="pay.wxpay.js">微信公众号(js支付)</option>
    </select>
    <br><br>

    <div id="authCodeDiv" style="display: none;">
        条形码
        <input type="text" name="authCode" value="">
        支付宝或者微信的条形码字符串
        <br><br>
    </div>

    <div id="userIdDiv" style="display: none;">
        用户openId
        <input type="text" name="userId" value="ouVKTvzSrvBbPrIFRGvyARpGbcJM">
        微信或者支付宝js支付时用户相对公众号的openId
        <br><br>
    </div>

    支付金额
    <input type="text" name="totalFee" value="">
    <span>单位:分(整型)</span>
    <br><br>

    <span id="error" style="color: red;display: none;"></span>
    <br><br>

    <input id="submit" type="button" value="提交"/>
</form>

<div id="qrcode"></div>

<script>
    var authCodeDiv = document.getElementById('authCodeDiv');
    var userIdDiv = document.getElementById('userIdDiv');
    function changeService(service) {
        if(service == 'pay.alipay.scan' || service == 'pay.wxpay.scan'){
            authCodeDiv.style.display = 'block';
        } else {
            authCodeDiv.style.display = 'none';
        }
        if(service == 'pay.alipay.js' || service == 'pay.wxpay.js'){
            userIdDiv.style.display = 'block';
        } else {
            userIdDiv.style.display = 'none';
        }
        document.getElementById('qrcode').innerHTML = '';
    }

    document.getElementById("submit").addEventListener('click', function () {
        document.getElementById("error").style.display = 'none';
        $.post('/fulaPay', $('#form').serialize(), function (data) {
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
