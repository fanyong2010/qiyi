package com.offcn.order.service;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.service.impl.ProjectServiceBack;
import com.offcn.order.vo.req.TReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// vip:
@FeignClient(value = "SCW-PROJECT", fallback = ProjectServiceBack.class)
public interface ProjectServiceFeign {

    // SCW-PROJECT/project/returns/info/5
    @GetMapping("/project/returns/info/{returnId}")
    public AppResponse<TReturn> returnInfo(@PathVariable("returnId") Integer returnId);


}
