package com.offcn.project.vo.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProjectVo implements Serializable {
    // 会员id
    private Integer memberid;
    private Integer id;
    // 项目名称
    private String name;
    // 项目简介
    private String remark;

    // 项目头部图片
    private String headerImage;

}