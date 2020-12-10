package com.offcn.webui.service.impl;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.service.MemberServiceFeign;
import com.offcn.webui.vo.resp.TMemberAddress;
import com.offcn.webui.vo.resp.UserRespVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MemberServiceFeignBack implements MemberServiceFeign {

    @Override
    public AppResponse<UserRespVo> doLogin(String un, String pwd) {
        AppResponse<UserRespVo> resp = AppResponse.fail(null);
        resp.setMsg("远程调用失败：[登录功能]");
        log.error("远程调用失败：登录功能；用户名：{}，密码：{}", un, pwd);
        return resp;
    }

    @Override
    public AppResponse<List<TMemberAddress>> address(String accessToken) {
        AppResponse<List<TMemberAddress>> fail = AppResponse.fail(null);
        fail.setMsg("远程调用失败：【查询地址列表】");

        log.error("远程调用失败：【查询地址列表】");

        return fail;
    }

}
