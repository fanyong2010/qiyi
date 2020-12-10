package com.offcn.controller;

import com.offcn.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ThyController {

    @RequestMapping("/first")
    public String first(Model m) {
        m.addAttribute("message", "我是thymeleaf的第一个测试");
        return "index";
    }

    @RequestMapping("/second")
    public String second(Model m) {
        m.addAttribute("vari", "我是个变量");

        return "index2";
    }

    @RequestMapping("/third")
    public String third(Model m) {
        User user = new User(66, "小华", 12);
        m.addAttribute("user", user);
        return "index3";
    }

    @RequestMapping("/four")
    public String four(Model m) {
        List<User> userList = new ArrayList<>();

        User u1 = new User(10, "小华", 23);
        User u2 = new User(11, "小李", 19);
        User u3 = new User(12, "小红", 24);
        User u4 = new User(13, "小王", 20);
        User u5 = new User(14, "小高", 18);

        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
        userList.add(u4);
        userList.add(u5);

        m.addAttribute("userList", userList);
        return "index4";
    }

    @RequestMapping("/five")
    public String five() {
        return "index5";
    }

    @GetMapping("/six")
    public String six(Model model) {
        //日期时间
        Date date = new Date();
        model.addAttribute("date", date);

        //小数的金额
        double price = 888128.5678;
        model.addAttribute("price", price);

        //定义大文本数据
        String str = "Thymeleaf是Web和独立环境的现代服务器端Java模板引擎";
        model.addAttribute("strText", str);

        //定义字符串
        String str1 = "JAVA-offcn";
        model.addAttribute("str1", str1);

        return "index6";
    }


}
