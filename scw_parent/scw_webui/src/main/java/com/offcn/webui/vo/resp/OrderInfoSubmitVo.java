package com.offcn.webui.vo.resp;

import com.offcn.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderInfoSubmitVo extends BaseVo {
    //收货地址id
    private String address;
    // 0代表不开发票  1-代表开发票
    private String invoice;
    //发票抬头
    private String invoictitle;
    //订单的备注
    private String remark;

    private Integer rtncount;

    private Integer projectid;
    private Integer returnid;
}