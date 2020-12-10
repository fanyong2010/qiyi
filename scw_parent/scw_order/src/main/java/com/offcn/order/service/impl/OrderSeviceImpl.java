package com.offcn.order.service.impl;

import com.offcn.dycommon.enums.OrderStatusEnume;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.mapper.TOrderMapper;
import com.offcn.order.po.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import com.offcn.order.vo.req.TReturn;
import com.offcn.utils.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class OrderSeviceImpl implements OrderService {

    @Autowired
    private TOrderMapper orderMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Override
    public TOrder saveOrder(OrderInfoSubmitVo vo) {
        TOrder order = new TOrder();
        // 将页面的传递过来的数据，复制给 order 对象
        BeanUtils.copyProperties(vo, order);

        // 关联登录人
        String accessToken = vo.getAccessToken();
        String memberId = stringRedisTemplate.opsForValue().get(accessToken);
        order.setMemberid(Integer.parseInt(memberId));

        order.setOrdernum(UUID.randomUUID().toString().replace("-", ""));

        order.setCreatedate(DateUtil.getFormatTime());

        order.setStatus(OrderStatusEnume.UNPAY.getCode() + "");

        // 需要根据 回报tReturn数据 进行计算
        // 1. 根据vo.getReturnid() 回报id 得到回报对象
        // 1.1. scw_project提供 根据 return id 得到 return对象的 功能
        // 1.2 scw_order 远程 调用这个功能
        AppResponse<TReturn> appResp = projectServiceFeign.returnInfo(vo.getReturnid());
        TReturn tReturn = appResp.getData();

        // 2. 通过回报对象 中的 运费、 支持的金额， 结合 vo中的购买数量 ，计算金额
        Integer money = tReturn.getSupportmoney() * vo.getRtncount() + tReturn.getFreight();
        order.setMoney(money);

        orderMapper.insert(order);
        return order;
    }

}
