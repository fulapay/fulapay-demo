package service.merchant;

import util.PayUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.SortedMap;

/**
 * 商户状态改变异常通知接口
 * Created by wuming on 16/11/18.
 */
@WebServlet("/merchant/status/change/notify")
public class MerchantStatusChangeNotify extends HttpServlet{

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("-----merchant status change notify---------");
        try {
            SortedMap<String, String> param = PayUtil.readFulaNotify(req);
            if(PayUtil.verifyFulaParam(param)){
                // 根据修改业务逻辑 具体参数参见API文档
                System.out.println("notify param: " + param);
                res.getWriter().print("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

}
