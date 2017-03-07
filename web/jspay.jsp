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
    <title>fula-pay-jspay</title>
</head>

<body>
<form id="form" action="/jspay" method="post" onsubmit="return checkForm();">
    支付接口
    <select name="service">
        <option value="pay.wxpay.js">微信公众号(js支付)</option>
    </select>
    <br>

    支付金额
    <input type="text" id="totalFee" name="totalFee" value="">
    <span>单位:分(整型)</span>
    <br><br>

    <span id="error" style="color: red;display: none;"></span>
    <br><br>

    <input type="submit" value="提交"/>
</form>

<div id="qrcode"></div>

<script type="text/javascript">
    function checkForm() {
        var totalFee = document.getElementById('totalFee').value;
        if (!/^\d*[1-9]\d*$/.test(totalFee)) {
            alert('请输入正整数金额！');
            return false;
        }
        return true;
    }
</script>
</body>
</html>
