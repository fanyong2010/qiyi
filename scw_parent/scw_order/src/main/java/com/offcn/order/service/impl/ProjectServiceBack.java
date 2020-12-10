package com.offcn.order.service.impl;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.req.TReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProjectServiceBack implements ProjectServiceFeign {

    @Override
    public AppResponse<TReturn> returnInfo(Integer returnId) {
        AppResponse<TReturn> fail = AppResponse.fail(null);
        log.warn("熔断：调用远程服务器失败【获取单个回报】");
        return fail;
    }

}
