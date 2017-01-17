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
                System.out.println(param);
                // 根据修改业务逻辑 具体参数参见API文档
                // TODO wuming 17/1/17 上午11:57
                // ...
                res.getWriter().print("success");
                res.getWriter().flush();
                res.getWriter().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

}
