package com.offcn.webui.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.service.MemberServiceFeign;
import com.offcn.webui.service.ProjectServiceFeign;
import com.offcn.webui.vo.resp.ProjectDetailVo;
import com.offcn.webui.vo.resp.ReturnPayConfirmVo;
import com.offcn.webui.vo.resp.TMemberAddress;
import com.offcn.webui.vo.resp.UserRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {


    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Autowired
    private MemberServiceFeign memberServiceFeign;



    @RequestMapping("/projectInfo")
    public String projectInfo(Integer id, Model model, HttpSession session) {

        AppResponse<ProjectDetailVo> vo = projectServiceFeign.detailsInfo(id);

        ProjectDetailVo data = vo.getData();

        // model是往页面传递数据的
        model.addAttribute("DetailVo", data);

        // session是暂时将项目存到服务器，后续需要使用（类似将登录信息存起来）
        session.setAttribute("DetailVo", data);

        return "project/project";
    }

    /**
     * 跳转回报内容确认页面
     *
     * @param returnId
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/confirm/returns/{returnId}")
    public String returnInfo(@PathVariable("returnId") Integer returnId, Model model, HttpSession session) {

        // 从上一步的session中获取 项目详情 对象
        ProjectDetailVo projectDetailVo = (ProjectDetailVo) session.getAttribute("DetailVo");

        AppResponse<ReturnPayConfirmVo> returnInfo = projectServiceFeign.returnInfo(returnId);
        ReturnPayConfirmVo data = returnInfo.getData();

        // 设置项目的名称
        data.setProjectName(projectDetailVo.getName());


        model.addAttribute("returnConfirm", data);

        session.setAttribute("returnConfirm", data);

        return "project/pay-step-1";
    }


    /**
     * 跳转支付确认页面
     *
     * @param num
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/confirm/order/{num}")
    public String confirmOrder(@PathVariable("num") Integer num, Model model, HttpSession session) {

        //1. 查询用户是否已登录
        UserRespVo data = (UserRespVo) session.getAttribute("sessionMember");
        //用户未登录或登录超时,转发到登录页面
        if (data == null) {
            //用户登录成功后需要再跳转回当前页面

            session.setAttribute("preUrl", "project/confirm/order/" + num);

            return "redirect:/login";
        }
        String accessToken = data.getAccessToken();


        //2. 根据用户accessToken查询用户的收货地址列表
        AppResponse<List<TMemberAddress>> addressResponse = memberServiceFeign.address(accessToken);
        List<TMemberAddress> addressList = addressResponse.getData();


        //3. 将地址存入request域中
        model.addAttribute("addresses", addressList);


        ReturnPayConfirmVo confirmVo = (ReturnPayConfirmVo) session.getAttribute("returnConfirm");
        confirmVo.setNum(num);
        // 涉及到金额，在后台代码中进行计算，避免页面篡改导致数据 不安全
        confirmVo.setTotalPrice(new BigDecimal(num * confirmVo.getSupportmoney() + confirmVo.getFreight()));

        session.setAttribute("returnConfirmSession", confirmVo);

        //4. 转发到paystep2页面
        return "project/pay-step-2";
    }


}
