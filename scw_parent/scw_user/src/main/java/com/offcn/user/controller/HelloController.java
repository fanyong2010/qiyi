package com.offcn.user.controller;

import com.offcn.user.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Hello测试控制层")
@RestController
@RequestMapping("/hello")
public class HelloController {


    @ApiOperation("测试方法hello")
    // name：表示参数的名字
    // value: 解释该参数是什么意思
    // required: 是否是 必传的
    @ApiImplicitParam(name = "name", value = "用户姓名", required = true, defaultValue = "森淇")
    @GetMapping("/showName")
    public String showName(String name) {
        return "OK: " + name;
    }

    @ApiOperation("保存用户")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "name", value = "用户姓名", required = true),
            @ApiImplicitParam(name = "email", value = "电子邮件")
    })
    @PostMapping("/user")
    public User save(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }

}
