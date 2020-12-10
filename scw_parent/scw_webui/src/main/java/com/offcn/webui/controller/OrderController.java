package com.offcn.webui.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.service.OrderServiceFeign;
import com.offcn.webui.vo.resp.OrderInfoSubmitVo;
import com.offcn.webui.vo.resp.ReturnPayConfirmVo;
import com.offcn.webui.vo.resp.TOrder;
import com.offcn.webui.vo.resp.UserRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderServiceFeign orderServiceFeign;

    @RequestMapping("/pay")
    public String OrderPay(OrderInfoSubmitVo vo, HttpSession session) {

        UserRespVo userResponseVo = (UserRespVo) session.getAttribute("sessionMember");
        if (userResponseVo == null) {
            return "redirect:/login";
        }
        // 如果登录了，获取身份令牌
        String accessToken = userResponseVo.getAccessToken();

        vo.setAccessToken(accessToken);

        ReturnPayConfirmVo confirmVo = (ReturnPayConfirmVo) session.getAttribute("returnConfirmSession");

        vo.setProjectid(confirmVo.getProjectid());
        vo.setReturnid(confirmVo.getId());
        vo.setRtncount(confirmVo.getNum());


        AppResponse<TOrder> order = orderServiceFeign.createOrder(vo);
        TOrder data = order.getData();

        System.out.println("orderNum: " + data.getOrdernum());

        return "member/minecrowdfunding";
    }

}
