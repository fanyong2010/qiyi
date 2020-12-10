package com.offcn.project.vo.req;

import com.offcn.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("项目回报实体")
@Data
public class ProjectReturnVo extends BaseVo {

    @ApiModelProperty("项目的临时token")
    private String projectToken;

    @ApiModelProperty(value = "回报类型：0-货币回报 1-实物回报", required = true)
    private String type;

    @ApiModelProperty(value = "支持金额", required = true)
    private Integer supportmoney;

    @ApiModelProperty(value = "回报内容", required = true)
    private String content;

    @ApiModelProperty(value = "单笔限购", required = true)
    private Integer signalpurchase;

    @ApiModelProperty(value = "限购数量", required = true)
    private Integer purchase;

    @ApiModelProperty(value = "运费", required = true)
    private Integer freight;

    @ApiModelProperty(value = "发票 0-不开发票 1-开发票", required = true)
    private String invoice;

    @ApiModelProperty(value = "回报时间，天数", required = true)
    private Integer rtndate;

}
