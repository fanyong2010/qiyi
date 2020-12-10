package com.offcn.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class SessionController {

    @RequestMapping("set")
    public void setSession(HttpSession session, String userName) {

        session.setAttribute("userName", userName);
        System.out.println("登录了，放置了session");
    }

    @RequestMapping("get")
    public String getSession(HttpSession session) {
        String userName = (String) session.getAttribute("userName");
        return "登录名：" + userName;
    }

}
