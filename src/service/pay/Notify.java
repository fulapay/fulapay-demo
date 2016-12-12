package service.pay;

import util.PayUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.SortedMap;

/**
 * 付啦 统一下单后支付回调 接口测试
 * Created by wuming on 16/10/24.
 */
@WebServlet("/notify")
public class Notify extends HttpServlet{

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        try {
            SortedMap<String, String> param = PayUtil.readFulaNotify(req);
            if(PayUtil.verifyFulaParam(param)){
                // 根据修改业务逻辑 具体参数参见API文档
                System.out.println(param);
                res.getWriter().print("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

}
