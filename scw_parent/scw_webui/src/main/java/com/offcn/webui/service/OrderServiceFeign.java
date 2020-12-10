package com.offcn.webui.service;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.webui.service.impl.OrderServiceFeignBack;
import com.offcn.webui.vo.resp.OrderInfoSubmitVo;
import com.offcn.webui.vo.resp.TOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "SCW-ORDER", fallback = OrderServiceFeignBack.class)
public interface OrderServiceFeign {

    @PostMapping("/order/createOrder")
    public AppResponse<TOrder> createOrder(@RequestBody OrderInfoSubmitVo vo);

}
