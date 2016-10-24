package service;

import util.PayUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wuming on 16/10/24.
 */
@WebServlet("/notify")
public class Notify extends HttpServlet{

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        if(PayUtil.verifyNotify(req)){
            // 商户业务逻辑
            // ...
            // 返回success字符串
            res.getWriter().print("success");
        }
        return;
    }

}
