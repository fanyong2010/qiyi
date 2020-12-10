package com.offcn.user.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.user.po.TMemberAddress;
import com.offcn.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "获取用户信息/获取收货地址")
public class UserInfoController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @ApiOperation(value = "获取用户收货地址")
    @ApiImplicitParam(value = "访问令牌", name = "accessToken", required = true)
    @GetMapping("/info/address")
    public AppResponse<List<TMemberAddress>> address(String accessToken) {

        //1.得到登录会员ID
        String memberId = stringRedisTemplate.opsForValue().get(accessToken);
        if (StringUtils.isEmpty(memberId)) {
            return AppResponse.fail(null);
        }

        //2.查询该会员收货地址列表
        List<TMemberAddress> addressList = userService.addressList(Integer.parseInt(memberId));

        return AppResponse.ok(addressList);
    }

}
