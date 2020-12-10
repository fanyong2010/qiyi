package com.offcn.webui.service.impl;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.service.OrderServiceFeign;
import com.offcn.webui.vo.resp.OrderInfoSubmitVo;
import com.offcn.webui.vo.resp.TOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceFeignBack implements OrderServiceFeign {

    @Override
    public AppResponse<TOrder> createOrder(OrderInfoSubmitVo vo) {
        AppResponse<TOrder> resp = AppResponse.fail(null);
        resp.setMsg("远程调用失败：【保存订单】");
        log.error("远程调用失败：【保存订单】");
        return resp;
    }

}
