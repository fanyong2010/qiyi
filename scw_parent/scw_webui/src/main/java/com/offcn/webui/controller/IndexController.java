package com.offcn.webui.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.service.MemberServiceFeign;
import com.offcn.webui.service.ProjectServiceFeign;
import com.offcn.webui.vo.resp.ProjectVo;
import com.offcn.webui.vo.resp.UserRespVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class IndexController {

    @Autowired
    private MemberServiceFeign memberServiceFeign;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;


    @RequestMapping("/")
    public String toIndex(Model model) {

        // 获取项目列表
        // 先从redis中查询项目信息
        String projectStr = stringRedisTemplate.opsForValue().get("projectStr");

        // 如果缓存中没有
        if (StringUtils.isEmpty(projectStr) || projectStr.equals("null")) {
            // 就调用远程服务
            AppResponse<List<ProjectVo>> allProject = projectServiceFeign.all();
            List<ProjectVo> data = allProject.getData();

            projectStr = JSON.toJSONString(data);
            // 调用后，将项目信息放入redis
            stringRedisTemplate.opsForValue().set("projectStr", projectStr, 1, TimeUnit.HOURS);
        }

        model.addAttribute("projectList", JSON.parseArray(projectStr));

        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    //处理登录请求的方法
    @RequestMapping("/doLogin")
    public String doLogin(String loginacct, String userpswd, HttpSession session, Model m) {

        AppResponse<UserRespVo> login = memberServiceFeign.doLogin(loginacct, userpswd);

        UserRespVo data = login.getData();

        if (data == null) {
            //登录失败，则重新跳转到登录页
            return "login";
        }
        //登录成功, 将userVo对象共享到session中
        session.setAttribute("sessionMember", data);
        m.addAttribute("member", data);

        // 额外研究：一个实际开发的用户体验问题：
        // 登录后 回到 登录之前的页面（如果记录的有的话）， 以便提高用户体验
        String preUrl = (String) session.getAttribute("preUrl");
        if (StringUtils.isEmpty(preUrl)) {
            return "index";
        }

        return "redirect:/" + preUrl;
    }

}
