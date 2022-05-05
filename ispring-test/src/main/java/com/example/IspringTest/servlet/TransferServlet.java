package com.example.IspringTest.servlet;

import com.alibaba.fastjson.JSON;
import com.example.IspringTest.dto.Result;
import com.example.IspringTest.service.TransactionalService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "transferServlet", urlPatterns = "/transferServlet")
public class TransferServlet extends HttpServlet {

    private TransactionalService transactionalService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求体的字符编码
        req.setCharacterEncoding("UTF-8");

        String fromAccountNo = req.getParameter("fromAccountNo");
        String toAccountNo = req.getParameter("toAccountNo");
        String money = req.getParameter("money");

        Result result = new Result();
        try {
            // 2. 调用service层方法
            transactionalService.handleTransactionalOperate(Long.valueOf(fromAccountNo),Long.valueOf(toAccountNo),Long.valueOf(money));
            result.setStatus("200");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus("201");
            result.setMessage(e.toString());
        }

        // 响应
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().print(JSON.toJSON(result));
    }

    @Override
    public void init() throws ServletException {
        //WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        //transferManager = (TransferManager) webApplicationContext.getBean("transferManagerImpl");
    }
}
