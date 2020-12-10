package com.offcn.webui.service;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.service.impl.MemberServiceFeignBack;
import com.offcn.webui.vo.resp.TMemberAddress;
import com.offcn.webui.vo.resp.UserRespVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "SCW-USER", fallback = MemberServiceFeignBack.class)
public interface MemberServiceFeign {

    @PostMapping("/user/login")
    public AppResponse<UserRespVo> doLogin(@RequestParam("username") String un , @RequestParam("password") String pwd);

    @GetMapping("/user/info/address")
    public AppResponse<List<TMemberAddress>> address(@RequestParam("accessToken") String accessToken);

}
