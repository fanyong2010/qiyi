package com.offcn.user.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.user.po.TMember;
import com.offcn.user.service.UserService;
import com.offcn.user.util.SmsUtil;
import com.offcn.user.vo.req.UserRegistVo;
import com.offcn.user.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Api(tags = "用户登录/注册模块")
@RestController
@RequestMapping("/user")
public class UserLoginController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private UserService userService;


    @ApiOperation("用户发送短信验证码")
    @ApiImplicitParam(name="phoneNo", value="用户手机号", required = true)
    @PostMapping("/sendCode")
    public AppResponse<String> sendCode(String phoneNo) {
        // 1. 产生验证码
        String code = UUID.randomUUID().toString().substring(0, 6);
        // 2. 保存验证码至redis缓存，便于注册时验证
        stringRedisTemplate.opsForValue().set(phoneNo, code, 3, TimeUnit.MINUTES);
        // 3. 发送验证码
        String result = smsUtil.sendCode(phoneNo, code);
        if(!result.equals("fail")) {
            return AppResponse.ok("发送成功");
        }
        // 发送失败
        else {
            return AppResponse.fail("发送失败");
        }
    }

    @ApiOperation("用户注册")
    @PostMapping("/regist")
    public AppResponse<String> regist(UserRegistVo registVo) {
        // 1.判断短信验证码是否正确
        String code = stringRedisTemplate.opsForValue().get(registVo.getLoginacct());

        // 不为空
        if(!StringUtils.isEmpty(code)) {

            if(code.equals(registVo.getCode())) {
                // 2. 正确：填充一些必须参数，调用服务层注册
                TMember member = new TMember();

                // 将registVo中的属性 按照 同名的规格 赋值 给 member对象
                BeanUtils.copyProperties(registVo, member);

                userService.registerUser(member);

                return AppResponse.ok("注册成功");
            }
            // 3. 错误：返回给页面结果
            else {
                return AppResponse.fail("验证码输入错误");
            }
        }
        else {
            // 为空
            return AppResponse.fail("验证码不存在");
        }
    }


    @ApiOperation("用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)
    })
    @PostMapping("/login")
    public AppResponse<UserRespVo> login(String username, String password) {
        TMember member = userService.login(username, password);
        if(member != null) {
            // 1. 产生一个随机串令牌，并存到redis中
            String token = UUID.randomUUID().toString().replace("-", "");
            // 以令牌作为key, 以登录用户的id作为值
            // (也可以使用 UserRespVo对象 作为值)

            // 登录信息：默认存储 30 分钟， 这是正确做法
            // stringRedisTemplate.opsForValue().set(token, member.getId() + "", 30, TimeUnit.MINUTES);

            // 但是为了后续测试方便，将登录信息不设置过期时间
            stringRedisTemplate.opsForValue().set(token, member.getId() + "");

            // 2. 将令牌赋值给 UserRespVo 响应对象
            UserRespVo vo = new UserRespVo();
            vo.setAccessToken(token);
            BeanUtils.copyProperties(member, vo);

            // 3. 返回给页面提示
            return AppResponse.ok(vo);

        } else {
            AppResponse<UserRespVo> fail = AppResponse.fail(null);
            fail.setMsg("用户名或密码错误");
            return fail;
        }
    }




}
